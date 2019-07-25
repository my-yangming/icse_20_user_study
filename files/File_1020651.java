package com.github.vole.message.listener;

import com.github.vole.common.constants.MqQueueConstant;
import com.github.vole.message.handler.SmsMessageHandler;
import com.github.vole.message.template.MobileMsgTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 监�?��?务状�?改�?��?��?请求
 */
@Slf4j
@Component
@RabbitListener(queues = MqQueueConstant.MOBILE_SERVICE_STATUS_CHANGE)
public class MobileServiceChangeReceiveListener {
    @Autowired
    private Map<String, SmsMessageHandler> messageHandlerMap;


    @RabbitHandler
    public void receive(MobileMsgTemplate mobileMsgTemplate) {
        long startTime = System.currentTimeMillis();
        log.info("消�?�中心接收到短信�?��?请求-> 手机�?�：{} -> 信�?�体：{} ", mobileMsgTemplate.getMobile(), mobileMsgTemplate.getContext());
        String channel = mobileMsgTemplate.getChannel();
        SmsMessageHandler messageHandler = messageHandlerMap.get(channel);
        if (messageHandler == null) {
            log.error("没有找到指定的路由通�?�，�?进行�?��?处�?�完毕�?");
            return;
        }

        messageHandler.execute(mobileMsgTemplate);
        long useTime = System.currentTimeMillis() - startTime;
        log.info("调用 {} 短信网关处�?�完毕，耗时 {}毫秒", mobileMsgTemplate.getType(), useTime);
    }
}
