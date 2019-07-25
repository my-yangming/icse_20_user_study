package com.github.vole.message.handler;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.github.vole.common.constants.CommonConstant;
import com.github.vole.common.validate.Assert;
import com.github.vole.message.config.SmsAliyunPropertiesConfig;
import com.github.vole.message.template.MobileMsgTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 阿里大鱼短�?��?务处�?�
 */
@Slf4j
@Component(CommonConstant.ALIYUN_SMS)
public class SmsAliyunMessageHandler extends AbstractMessageHandler {
    @Autowired
    private SmsAliyunPropertiesConfig smsAliyunPropertiesConfig;
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    /**
     * 数�?�校验
     *
     * @param mobileMsgTemplate 消�?�
     */
    @Override
    public void check(MobileMsgTemplate mobileMsgTemplate) {
        Assert.isBlank(mobileMsgTemplate.getMobile(), "手机�?��?能为空");
        Assert.isBlank(mobileMsgTemplate.getContext(), "短信内容�?能为空");
    }

    /**
     * 业务处�?�
     *
     * @param mobileMsgTemplate 消�?�
     */
    @Override
    public boolean process(MobileMsgTemplate mobileMsgTemplate) {
        //�?�自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //�?始化acsClient,暂�?支�?region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsAliyunPropertiesConfig.getAccessKey(), smsAliyunPropertiesConfig.getSecretKey());
        try {
            DefaultProfile.addEndpoint("cn-hou", "cn-hangzhou", PRODUCT, DOMAIN);
        } catch (ClientException e) {
            log.error("�?始化SDK 异常", e);
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体�??述�?控制�?�-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待�?��?手机�?�
        request.setPhoneNumbers(mobileMsgTemplate.getMobile());

        //必填:短信签�??-�?�在短信控制�?�中找到
        request.setSignName(mobileMsgTemplate.getSignName());

        //必填:短信模�?�-�?�在短信控制�?�中找到
        request.setTemplateCode(smsAliyunPropertiesConfig.getChannels().get(mobileMsgTemplate.getTemplate()));

        //�?�选:模�?�中的�?��?替�?�JSON串,如模�?�内容为"亲爱的${name},您的验�?�?为${code}"
        request.setTemplateParam(mobileMsgTemplate.getContext());
        request.setOutId(mobileMsgTemplate.getMobile());

        //hint 此处�?�能会抛出异常，注�?catch
        try {
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            log.info("短信�?��?完毕，手机�?�：{}，返回状�?：{}", mobileMsgTemplate.getMobile(), sendSmsResponse.getCode());
        } catch (ClientException e) {
            log.error("�?��?异常");
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 失败处�?�
     *
     * @param mobileMsgTemplate 消�?�
     */
    @Override
    public void fail(MobileMsgTemplate mobileMsgTemplate) {
        log.error("短信�?��?失败 -> 网关：{} -> 手机�?�：{}", mobileMsgTemplate.getType(), mobileMsgTemplate.getMobile());
    }
}
