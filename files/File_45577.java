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
package com.alipay.sofa.rpc.context;

/**
 * RPC 框架�?行状�?
 * 
 * @author <a href="mailto:zhanggeng.zg@antfin.com">GengZhang</a>
 */
public final class RpcRunningState {

    /**
     * 是�?�正在关闭
     */
    static boolean shuttingDown = false;

    /**
     * 是�?��?�元测试（跳过一些加载或者�?�载）
     */
    static boolean unitTestMode = false;

    /**
     * 是�?�debug模�?，开�?��?�，会打�?�一些�?外的调试日志，�?过还是�?�slf4j的日志级别�?制
     */
    static boolean debugMode    = false;

    /**
     * 是�?�正在关闭
     *
     * @return 是�?�关闭
     */
    public static boolean isShuttingDown() {
        return shuttingDown;
    }

    /**
     * 设置是�?�正在关闭
     *
     * @param shuttingDown 是�?�正在关闭
     */
    static void setShuttingDown(boolean shuttingDown) {
        RpcRunningState.shuttingDown = shuttingDown;
    }

    /**
     * 是�?��?�元测试模�?
     *
     * @return 是�?��?�元测试模�?
     */
    public static boolean isUnitTestMode() {
        return unitTestMode;
    }

    /**
     * 设置是�?��?�元测试模�?
     *
     * @param unitTestMode �?�元测试模�?
     */
    public static void setUnitTestMode(boolean unitTestMode) {
        RpcRunningState.unitTestMode = unitTestMode;
    }

    /**
     * 是�?�调试模�?
     *
     * @return 是�?�调试模�?
     */
    public static boolean isDebugMode() {
        return debugMode;
    }

    /**
     * 设置是�?�调试模�?
     *
     * @param debugMode 是�?�调试模�?
     */
    public static void setDebugMode(boolean debugMode) {
        RpcRunningState.debugMode = debugMode;
    }
}
