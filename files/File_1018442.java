package com.xiaolyuh.mq.listener;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.xiaolyuh.constants.RabbitConstants;
import com.xiaolyuh.mq.message.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 死信队列处�?�消�?�
 *
 * @author yuhao.wang
 */
@Service
public class SendMessageListener {

    private final Logger logger = LoggerFactory.getLogger(SendMessageListener.class);

//    @RabbitListener(queues = RabbitConstants.QUEUE_NAME_DEAD_QUEUE)
    public void process(SendMessage sendMessage, Channel channel, Message message) throws Exception {
        logger.info("[{}]处�?�死信队列消�?�队列接收数�?�，消�?�体：{}", RabbitConstants.QUEUE_NAME_DEAD_QUEUE, JSON.toJSONString(sendMessage));

        System.out.println(message.getMessageProperties().getDeliveryTag());

        try {
            // �?�数校验
            Assert.notNull(sendMessage, "sendMessage 消�?�体�?能为NULL");

            // TODO 处�?�消�?�

            // 确认消�?�已�?消费�?功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            logger.error("MQ消�?�处�?�异常，消�?�体:{}", message.getMessageProperties().getCorrelationIdString(), JSON.toJSONString(sendMessage), e);

            // 确认消�?�已�?消费消费失败，将消�?��?�给下一个消费者
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
    }
}
