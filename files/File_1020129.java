package com.myimooc.small.advanced.rest;

import java.util.Map;
import java.util.Objects;

import com.myimooc.small.advanced.domain.EventMessage;
import com.myimooc.small.advanced.domain.NewsMessage;
import com.myimooc.small.advanced.domain.TextMessage;
import com.myimooc.small.advanced.util.MessageUtils;
import com.myimooc.small.advanced.util.WeixinUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处�?�消�?�请求与�?应
 *
 * @author ZhangCheng on 2017-08-11
 */
@RestController
public class MessageRest {

    private static final Logger logger = LoggerFactory.getLogger(MessageRest.class);
    private static final String KEY_1 = "1";
    private static final String KEY_2 = "2";
    private static final String KEY_3 = "3";
    private static final String KEY_FY = "翻译";
    private static final String KEY_MENU_CN = "？";
    private static final String KEY_MENU_EN = "?";

    /**
     * 接收微信�?务器�?��?的POST请求
     *
     * @throws Exception
     */
    @PostMapping("textmessage")
    public Object textmessage(TextMessage msg) throws Exception {

        logger.info("请求�?�数：{}", msg.toString());

        // 文本消�?�
        if (Objects.equals(MessageUtils.MESSAGE_TEXT, msg.getMsgType())) {
            TextMessage textMessage = new TextMessage();
            // 关键字 1
            if (Objects.equals(KEY_1, msg.getContent())) {
                textMessage = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), MessageUtils.firstMenu());
                return textMessage;
            }
            // 关键字 2
            if (Objects.equals(KEY_2, msg.getContent())) {
                NewsMessage newsMessage = MessageUtils.initNewsMessage(msg.getToUserName(), msg.getFromUserName());
                return newsMessage;
            }
            // 关键字 3
            if (Objects.equals(KEY_3, msg.getContent())) {
                textMessage = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), MessageUtils.threeMenu());
                return textMessage;
            }
            // 关键字 翻译
            if (msg.getContent().startsWith(KEY_FY)) {
                String word = msg.getContent().replaceAll("^翻译", "").trim();
                if ("".equals(word)) {
                    textMessage = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), MessageUtils.threeMenu());
                    return textMessage;
                }
                textMessage = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), WeixinUtils.translate(word));
                return textMessage;
            }
            // 关键字 ？? 调出�?��?�
            if (Objects.equals(KEY_MENU_CN, msg.getContent()) || Objects.equals(KEY_MENU_EN, msg.getContent())) {
                textMessage = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), MessageUtils.menuText());
                return textMessage;
            }

            // �?�关键字
            textMessage.setFromUserName(msg.getToUserName());
            textMessage.setToUserName(msg.getFromUserName());
            textMessage.setMsgType(MessageUtils.MESSAGE_TEXT);
            textMessage.setCreateTime(System.currentTimeMillis() + "");
            textMessage.setContent("您�?��?的消�?�是：" + msg.getContent());
            return textMessage;
        }
        return null;
    }

    /**
     * 接收微信�?务器�?��?的POST请求
     */
    @PostMapping("eventmessage")
    public Object eventmessage(Map<String, String> param) {

        EventMessage msg = new EventMessage();
        BeanUtils.copyProperties(param, msg);
        // 事件推�?
        if (Objects.equals(MessageUtils.MESSAGE_EVENT, msg.getMsgType())) {
            // 关注
            if (Objects.equals(MessageUtils.MESSAGE_SUBSCRIBE, msg.getEvent())) {
                TextMessage text = new TextMessage();
                text = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), MessageUtils.menuText());
                return text;
            }
            // �?��?� 点击类型
            if (Objects.equals(MessageUtils.MESSAGE_CLICK, msg.getEvent())) {
                TextMessage text = new TextMessage();
                text = MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), MessageUtils.menuText());
                return text;
            }
            // �?��?� 视图类型
            if (Objects.equals(MessageUtils.MESSAGE_VIEW, msg.getEvent())) {
                String url = param.get("EventKey");
                return MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), url);
            }
            // �?��?� 扫�?事件
            if (Objects.equals(MessageUtils.MESSAGE_SCANCODE, msg.getEvent())) {
                String key = param.get("EventKey");
                return MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), key);
            }
            // �?��?� 地�?��?置
            if (Objects.equals(MessageUtils.MESSAGE_LOCATION, msg.getEvent())) {
                String label = param.get("Label");
                return MessageUtils.initText(msg.getToUserName(), msg.getFromUserName(), label);
            }
        }
        return "no message";
    }

}
