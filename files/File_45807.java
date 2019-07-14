/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.rpc.client.lb;

import com.alipay.sofa.rpc.bootstrap.ConsumerBootstrap;
import com.alipay.sofa.rpc.client.AbstractLoadBalancer;
import com.alipay.sofa.rpc.client.ProviderInfo;
import com.alipay.sofa.rpc.core.request.SofaRequest;
import com.alipay.sofa.rpc.ext.Extension;

import java.util.List;
import java.util.Random;

/**
 * 负载�?�衡�?机算法:全部列表按�?��?�?机选择
 *
 * @author <a href=mailto:zhanggeng.zg@antfin.com>GengZhang</a>
 */
@Extension("random")
public class RandomLoadBalancer extends AbstractLoadBalancer {

    /**
     * �?机
     */
    private final Random random = new Random();

    /**
     * 构造函数
     *
     * @param consumerBootstrap �?务消费者�?置
     */
    public RandomLoadBalancer(ConsumerBootstrap consumerBootstrap) {
        super(consumerBootstrap);
    }

    @Override
    public ProviderInfo doSelect(SofaRequest invocation, List<ProviderInfo> providerInfos) {
        ProviderInfo providerInfo = null;
        int size = providerInfos.size(); // 总个数
        int totalWeight = 0; // 总�?��?
        boolean isWeightSame = true; // �?��?是�?�都一样
        for (int i = 0; i < size; i++) {
            int weight = getWeight(providerInfos.get(i));
            totalWeight += weight; // 累计总�?��?
            if (isWeightSame && i > 0 && weight != getWeight(providerInfos.get(i - 1))) {
                isWeightSame = false; // 计算所有�?��?是�?�一样
            }
        }
        if (totalWeight > 0 && !isWeightSame) {
            // 如果�?��?�?相�?�且�?��?大于0则按总�?��?数�?机
            int offset = random.nextInt(totalWeight);
            // 并确定�?机值�?�在哪个片断上
            for (int i = 0; i < size; i++) {
                offset -= getWeight(providerInfos.get(i));
                if (offset < 0) {
                    providerInfo = providerInfos.get(i);
                    break;
                }
            }
        } else {
            // 如果�?��?相�?�或�?��?为0则�?�等�?机
            providerInfo = providerInfos.get(random.nextInt(size));
        }
        return providerInfo;
    }
}
