package org.springframework.cloud.alibaba.cloud.example;

import com.aliyun.mns.model.Message;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 如果需�?监�?�短信是�?�被对方�?功接收，�?�需实现这个接�?�并�?始化一个 Spring Bean �?��?�。
 */
@Component
public class SmsReportMessageListener
		implements org.springframework.cloud.alicloud.sms.SmsReportMessageListener {
	private List<Message> smsReportMessageSet = new LinkedList<>();

	@Override
	public boolean dealMessage(Message message) {
		smsReportMessageSet.add(message);
		System.err.println(this.getClass().getName() + "; " + message.toString());
		return true;
	}

	public List<Message> getSmsReportMessageSet() {

		return smsReportMessageSet;
	}
}
