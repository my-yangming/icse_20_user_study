package com.myimooc.rabbitmq.ha.service;

import com.myimooc.rabbitmq.entity.Order;
import com.myimooc.rabbitmq.ha.constant.Constants;
import com.myimooc.rabbitmq.ha.dao.mapper.BrokerMessageLogMapper;
import com.myimooc.rabbitmq.ha.dao.mapper.OrderMapper;
import com.myimooc.rabbitmq.ha.dao.po.BrokerMessageLogPO;
import com.myimooc.rabbitmq.ha.producer.OrderSender;
import com.myimooc.rabbitmq.ha.util.FastJsonConvertUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <br>
 * 标题: 订�?��?务<br>
 * �??述: 订�?��?务<br>
 * 时间: 2018/09/07<br>
 *
 * @author zc
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;
    @Autowired
    private OrderSender orderSender;

    /**
     * 创建订�?�
     *
     * @param order 订�?�
     */
    public void create(Order order) {
        // 当�?时间
        Date orderTime = new Date();
        // 业务数�?�入库
        this.orderMapper.insert(order);
        // 消�?�日志入库
        BrokerMessageLogPO messageLogPO = new BrokerMessageLogPO();
        messageLogPO.setMessageId(order.getMessageId());
        messageLogPO.setMessage(FastJsonConvertUtils.convertObjectToJson(order));
        messageLogPO.setTryCount(0);
        messageLogPO.setStatus(Constants.OrderSendStatus.SENDING);
        messageLogPO.setNextRetry(DateUtils.addMinutes(orderTime, Constants.ORDER_TIMEOUT));
        this.brokerMessageLogMapper.insert(messageLogPO);
        // �?��?消�?�
        this.orderSender.send(order);
    }
}
