/**
 * Copyright (c) 2015-2019, Michael Yang �?��?海 (fuhai999@gmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jboot.components.mq.aliyunmq;

import com.aliyun.openservices.ons.api.*;
import io.jboot.Jboot;
import io.jboot.components.mq.Jbootmq;
import io.jboot.components.mq.JbootmqBase;

import java.util.Properties;


public class JbootAliyunmqImpl extends JbootmqBase implements Jbootmq, MessageListener {

    private Producer producer;
    private Consumer consumer;

    public JbootAliyunmqImpl() {
        super();

        Properties properties = createProperties();
        producer = ONSFactory.createProducer(properties);
        producer.start();

    }


    @Override
    protected void onStartListening() {

        Properties properties = createProperties();

        consumer = ONSFactory.createConsumer(properties);
        for (String c : channels) {
            consumer.subscribe(c, "*", this);
        }
        consumer.start();
    }

    @Override
    public void enqueue(Object message, String toChannel) {
        throw new RuntimeException("not finished!");
    }

    @Override
    public void publish(Object message, String toChannel) {
        byte[] bytes = getSerializer().serialize(message);
        Message onsMessage = new Message(toChannel, "*", bytes);
        producer.send(onsMessage);
    }

    @Override
    public Action consume(Message message, ConsumeContext context) {
        byte[] bytes = message.getBody();
        Object object = getSerializer().deserialize(bytes);
        notifyListeners(message.getTopic(), object);
        return Action.CommitMessage;
    }


    private Properties createProperties() {
        JbootAliyunmqConfig aliyunmqConfig = Jboot.config(JbootAliyunmqConfig.class);

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.AccessKey, aliyunmqConfig.getAccessKey());//AccessKey 阿里云身份验�?，在阿里云�?务器管�?�控制�?�创建
        properties.put(PropertyKeyConst.SecretKey, aliyunmqConfig.getSecretKey());//SecretKey 阿里云身份验�?，在阿里云�?务器管�?�控制�?�创建
        properties.put(PropertyKeyConst.ProducerId, aliyunmqConfig.getProducerId());//您在控制�?�创建的Producer ID
        properties.put(PropertyKeyConst.ONSAddr, aliyunmqConfig.getAddr());
        properties.setProperty(PropertyKeyConst.SendMsgTimeoutMillis, aliyunmqConfig.getSendMsgTimeoutMillis());//设置�?��?超时时间，�?��?毫秒
        return properties;
    }
}
