package com.github.vole.message.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.vole.common.utils.httpclient.HttpContacter;
import com.github.vole.common.utils.httpclient.HttpFeedback;
import com.github.vole.message.config.DingTalkPropertiesConfig;
import com.github.vole.message.template.DingTalkMsgTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * �?��?钉钉消�?�逻辑
 */
@Slf4j
@Component
public class DingTalkMessageHandler {
    @Autowired
    private DingTalkPropertiesConfig dingTalkPropertiesConfig;

    /**
     * 业务处�?�
     *
     * @param text 消�?�
     */
    public boolean process(String text) {
        String webhook = dingTalkPropertiesConfig.getWebhook();
        if (StringUtils.isBlank(webhook)) {
            log.error("钉钉�?置错误，webhook为空");
            return false;
        }

        DingTalkMsgTemplate dingTalkMsgTemplate = new DingTalkMsgTemplate();
        dingTalkMsgTemplate.setMsgtype("text");
        DingTalkMsgTemplate.TextBean textBean = new DingTalkMsgTemplate.TextBean();
        textBean.setContent(text);
        dingTalkMsgTemplate.setText(textBean);
        HttpFeedback result = null;
        try {
            result = HttpContacter.p().doPost(webhook,JSONObject.toJSONString(dingTalkMsgTemplate));
        } catch (Exception e) {
            log.error("钉钉�?��?异常:{}", e.getMessage());
        }
        log.info("钉钉�??醒�?功,报文�?应:{}", result.getReceiptStr());
        return true;
    }

}
