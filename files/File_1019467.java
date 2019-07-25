/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.yahoo.sketches.hll;

import static com.yahoo.sketches.hll.HllUtil.VAL_MASK_6;
import static com.yahoo.sketches.hll.HllUtil.noWriteAccess;
import static com.yahoo.sketches.hll.PreambleUtil.HLL_BYTE_ARR_START;

import com.yahoo.memory.Memory;
import com.yahoo.memory.WritableMemory;

/**
 * @author Lee Rhodes
 */
class DirectHll6Array extends DirectHllArray {

  //Called by HllSketch.writableWrap(), DirectCouponList.promoteListOrSetToHll
  DirectHll6Array(final int lgConfigK, final WritableMemory wmem) {
    super(lgConfigK, TgtHllType.HLL_6, wmem);
  }

  //Called by HllSketch.wrap(Memory)
  DirectHll6Array(final int lgConfigK, final Memory mem) {
    super(lgConfigK, TgtHllType.HLL_6, mem);
  }

  @Override
  HllSketchImpl copy() {
    return Hll6Array.heapify(mem);
  }

  @Override
  HllSketchImpl couponUpdate(final int coupon) {
    if (wmem == null) { noWriteAccess(); }
    final int configKmask = (1 << getLgConfigK()) - 1;
    final int slotNo = HllUtil.getLow26(coupon) & configKmask;
    final int newVal = HllUtil.getValue(coupon);
    assert newVal > 0;

    final int curVal = getSlot(slotNo);
    if (newVal > curVal) {
      putSlot(slotNo, newVal);
      hipAndKxQIncrementalUpdate(this, curVal, newVal);
      if (curVal == 0) {
        decNumAtCurMin(); //overloaded as num zeros
        assert getNumAtCurMin() >= 0;
      }
    }
    return this;
  }

  @Override
  int getHllByteArrBytes() {
    return hll6ArrBytes(lgConfigK);
  }

  @Override
  PairIterator iterator() {
    return new DirectHll6Iterator(1 << lgConfigK);
  }

  @Override
  final int getSlot(final int slotNo) {
    return Hll6Array.get6Bit(mem, HLL_BYTE_ARR_START, slotNo);
  }

  @Override
  final void putSlot(final int slotNo, final int value) {
    Hll6Array.put6Bit(wmem, HLL_BYTE_ARR_START, slotNo, value);
  }

  //ITERATOR

  final class DirectHll6Iterator extends HllPairIterator {
    int bitOffset;

    DirectHll6Iterator(final int lengthPairs) {
      super(lengthPairs);
      bitOffset = -6;
    }

    @Override
    int value() {
      bitOffset += 6;
      final int tmp = mem.getShort(HLL_BYTE_ARR_START + (bitOffset / 8));
      final int shift = (bitOffset % 8) & 0X7;
      return (tmp >>> shift) & VAL_MASK_6;
    }
  }

}
