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

package com.yahoo.sketches.tuple;

import com.yahoo.memory.WritableMemory;
import com.yahoo.sketches.SketchesReadOnlyException;

final class DirectArrayOfDoublesUnionR extends DirectArrayOfDoublesUnion {

  /**
   * Wraps the given Memory.
   * @param sketch the ArrayOfDoublesQuickSelectSketch
   * @param mem <a href="{@docRoot}/resources/dictionary.html#mem">See Memory</a>
   */
  DirectArrayOfDoublesUnionR(final ArrayOfDoublesQuickSelectSketch sketch, final WritableMemory mem) {
    super(sketch, mem);
  }

  @Override
  public void update(final ArrayOfDoublesSketch sketchIn) {
    throw new SketchesReadOnlyException();
  }

  @Override
  public void reset() {
    throw new SketchesReadOnlyException();
  }

}
