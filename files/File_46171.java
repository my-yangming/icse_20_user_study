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
package com.alipay.sofa.rpc.registry.zk;

import com.alipay.sofa.rpc.codec.common.StringSerializer;
import com.alipay.sofa.rpc.common.utils.CommonUtils;
import com.alipay.sofa.rpc.config.AbstractInterfaceConfig;
import com.alipay.sofa.rpc.listener.ConfigListener;
import com.alipay.sofa.rpc.log.Logger;
import com.alipay.sofa.rpc.log.LoggerFactory;
import com.alipay.sofa.rpc.registry.utils.RegistryUtils;
import org.apache.curator.framework.recipes.cache.ChildData;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ZookeeperObserver for config node,subscribe interface level provider/consumer config.
 *
 * @author <a href=mailto:zhanggeng.zg@antfin.com>GengZhang</a>
 */
public class ZookeeperConfigObserver {

    /**
     * slf4j Logger for this class
     */
    private final static Logger                                          LOGGER            = LoggerFactory
                                                                                               .getLogger(ZookeeperConfigObserver.class);

    /**
     * The Config listener map.
     */
    private ConcurrentMap<AbstractInterfaceConfig, List<ConfigListener>> configListenerMap = new ConcurrentHashMap<AbstractInterfaceConfig, List<ConfigListener>>();

    /**
     * Add config listener.
     *
     * @param config   the config
     * @param listener the listener
     */
    public void addConfigListener(AbstractInterfaceConfig config, ConfigListener listener) {
        if (listener != null) {
            RegistryUtils.initOrAddList(configListenerMap, config, listener);
        }
    }

    /**
     * Remove config listener.
     *
     * @param config the config
     */
    public void removeConfigListener(AbstractInterfaceConfig config) {
        configListenerMap.remove(config);
    }

    /**
     * 接�?��?置修改�?节点Data
     *
     * @param config     接�?��?置
     * @param configPath �?置Path
     * @param data       �?节点Data
     */
    public void updateConfig(AbstractInterfaceConfig config, String configPath, ChildData data) {
        if (data == null) {
            if (LOGGER.isInfoEnabled(config.getAppName())) {
                LOGGER.infoWithApp(config.getAppName(), "Receive update data is null");
            }
        } else {
            if (LOGGER.isInfoEnabled(config.getAppName())) {
                LOGGER.infoWithApp(config.getAppName(), "Receive update data: path=[" + data.getPath() + "]"
                    + ", data=[" + StringSerializer.decode(data.getData()) + "]"
                    + ", stat=[" + data.getStat() + "]");
            }
            notifyListeners(config, configPath, data, false);
        }
    }

    /**
     * 接�?��?置修改�?节点Data列表
     *
     * @param config      接�?��?置
     * @param configPath  �?置Path
     * @param currentData �?节点Data列表
     */
    public void updateConfigAll(AbstractInterfaceConfig config, String configPath, List<ChildData> currentData) {
        if (CommonUtils.isEmpty(currentData)) {
            if (LOGGER.isInfoEnabled(config.getAppName())) {
                LOGGER.infoWithApp(config.getAppName(), "Receive updateAll data is null");
            }
        } else {
            if (LOGGER.isInfoEnabled(config.getAppName())) {
                for (ChildData data : currentData) {
                    LOGGER.infoWithApp(config.getAppName(), "Receive updateAll data: path=["
                        + data.getPath() + "], data=[" + StringSerializer.decode(data.getData()) + "]"
                        + ", stat=[" + data.getStat() + "]");
                }
            }
            List<ConfigListener> configListeners = configListenerMap.get(config);
            if (CommonUtils.isNotEmpty(configListeners)) {
                List<Map<String, String>> attributes = ZookeeperRegistryHelper.convertConfigToAttributes(configPath,
                    currentData);
                for (ConfigListener listener : configListeners) {
                    for (Map<String, String> attribute : attributes) {
                        listener.configChanged(attribute);
                    }
                }
            }
        }
    }

    /**
     * 接�?��?置删除�?节点Data
     *
     * @param config     接�?��?置
     * @param configPath �?置Path
     * @param data       �?节点Data
     */
    public void removeConfig(AbstractInterfaceConfig config, String configPath, ChildData data) {
        if (data == null) {
            if (LOGGER.isInfoEnabled(config.getAppName())) {
                LOGGER.infoWithApp(config.getAppName(), "Receive remove data is null");
            }
        } else {
            if (LOGGER.isInfoEnabled(config.getAppName())) {
                LOGGER.infoWithApp(config.getAppName(), "Receive remove data: path=[" + data.getPath() + "]"
                    + ", data=[" + StringSerializer.decode(data.getData()) + "]"
                    + ", stat=[" + data.getStat() + "]");
            }
            notifyListeners(config, configPath, data, true);
        }
    }

    /**
     * 接�?��?置新增�?节点Data
     *
     * @param config     接�?��?置
     * @param configPath �?置Path
     * @param data       �?节点Data
     */
    public void addConfig(AbstractInterfaceConfig config, String configPath, ChildData data) {
        if (data == null) {
            if (LOGGER.isInfoEnabled(config.getAppName())) {
                LOGGER.infoWithApp(config.getAppName(), "Receive add data is null");
            }
        } else {
            if (LOGGER.isInfoEnabled(config.getAppName())) {
                LOGGER.infoWithApp(config.getAppName(), "Receive add data: path=[" + data.getPath() + "]"
                    + ", data=[" + StringSerializer.decode(data.getData()) + "]"
                    + ", stat=[" + data.getStat() + "]");
            }
            notifyListeners(config, configPath, data, false);
        }
    }

    private void notifyListeners(AbstractInterfaceConfig config, String configPath, ChildData data, boolean removeType) {
        List<ConfigListener> configListeners = configListenerMap.get(config);
        if (CommonUtils.isNotEmpty(configListeners)) {
            //转�?��?节点Data为接�?�级�?置<�?置属性�??,�?置属性值>,例如<timeout,200>
            Map<String, String> attribute = ZookeeperRegistryHelper.convertConfigToAttribute(configPath, data,
                removeType);
            for (ConfigListener listener : configListeners) {
                listener.configChanged(attribute);
            }
        }
    }
}
