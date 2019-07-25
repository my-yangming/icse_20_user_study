package com.myimooc.small.access.rest;

import java.util.Objects;

import com.myimooc.small.access.domain.EventMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myimooc.small.access.domain.TextMessage;
import com.myimooc.small.access.util.MessageUtils;

/**
 * 处�?�消�?�请求与�?应
 * @author ZhangCheng on 2017-08-11
 *
 */
@RestController
public class MessageRest {

	private static final String KEY_1 = "1";
	private static final String KEY_2 = "2";
	private static final String KEY_MENU_CN = "？";
	private static final String KEY_MENU_EN = "?";

	/**
	 * 接收微信�?务器�?��?的POST请求
	 */
	@PostMapping("textmessage")
	public Object textmessage(TextMessage msg){
		// 文本消�?�
		if(Objects.equals(MessageUtils.MESSAGE_TEXT, msg.getMsgType())){
			TextMessage textMessage = new TextMessage();
			// 关键字 1
			if(Objects.equals(KEY_1, msg.getContent())){
				textMessage = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), MessageUtils.firstMenu());
				return textMessage;
			}
			// 关键字 2
			if(Objects.equals(KEY_2, msg.getContent())){
				textMessage = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), MessageUtils.secondMenu());
				return textMessage;
			}
			// 关键字 ？? 调出�?��?�
			if(Objects.equals(KEY_MENU_CN, msg.getContent()) || Objects.equals(KEY_MENU_EN, msg.getContent())){
				textMessage = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), MessageUtils.menuText());
				return textMessage;
			}
			
			// �?�关键字
			textMessage.setFromUserName(msg.getToUserName());
			textMessage.setToUserName(msg.getFromUserName());
			textMessage.setMsgType(MessageUtils.MESSAGE_TEXT);
			textMessage.setCreateTime(System.currentTimeMillis());
			textMessage.setContent("您�?��?的消�?�是：" + msg.getContent());
			return textMessage;
		}
		return null;
	}
	
	/**
	 * 接收微信�?务器�?��?的POST请求
	 */
	@PostMapping("eventmessage")
	public Object eventmessage(EventMessage msg){
		// 事件推�?
		if(Objects.equals(MessageUtils.MESSAGE_EVENT, msg.getMsgType())){
			// 关注
			if(Objects.equals(MessageUtils.MESSAGE_SUBSCRIBE, msg.getEvent())){
				TextMessage text = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), MessageUtils.menuText());
				return text;
			}
		}
		return null;
	}

}
