package org.springframework.cloud.alibaba.cloud.example;

import org.springframework.stereotype.Component;

import com.aliyun.mns.model.Message;

/**
 * @author 如果�?��?的短信需�?接收对方回�?的状�?消�?�，�?�需实现该接�?�并�?始化一个 Spring Bean �?��?�。
 */
@Component
public class SmsUpMessageListener
		implements org.springframework.cloud.alicloud.sms.SmsUpMessageListener {

	@Override
	public boolean dealMessage(Message message) {
		System.err.println(this.getClass().getName() + "; " + message.toString());
		return true;
	}
}
