package me.zhengjie.service.impl;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import me.zhengjie.domain.AlipayConfig;
import me.zhengjie.domain.vo.TradeVo;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.repository.AlipayRepository;
import me.zhengjie.service.AlipayService;
import me.zhengjie.utils.AlipayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

/**
 * @author Zheng Jie
 * @date 2018-12-31
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AlipayServiceImpl implements AlipayService {

    @Autowired
    AlipayUtils alipayUtils;

    @Autowired
    private AlipayRepository alipayRepository;

    @Override
    public String toPayAsPC(AlipayConfig alipay, TradeVo trade) throws Exception {

        if(alipay.getId() == null){
            throw new BadRequestException("请先添加相应�?置，�?�?作");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(alipay.getGatewayUrl(), alipay.getAppID(), alipay.getPrivateKey(), alipay.getFormat(), alipay.getCharset(), alipay.getPublicKey(), alipay.getSignType());

        double money = Double.parseDouble(trade.getTotalAmount());

        /**
         * 创建API对应的request(电脑网页版)
         */
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        /**
         * 订�?�完�?�?�返回的页�?�和异步通知地�?�
         */
        request.setReturnUrl(alipay.getReturnUrl());
        request.setNotifyUrl(alipay.getNotifyUrl());
        /**
         *  填充订�?��?�数
         */
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+trade.getOutTradeNo()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+trade.getTotalAmount()+"," +
                "    \"subject\":\""+trade.getSubject()+"\"," +
                "    \"body\":\""+trade.getBody()+"\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\""+alipay.getSysServiceProviderId()+"\"" +
                "    }"+
                "  }");//填充业务�?�数
        /**
         * 调用SDK生�?表�?�
         * 通过GET方�?，�?��?�以获�?�url
         */
        return alipayClient.pageExecute(request, "GET").getBody();

    }

    @Override
    public String toPayAsWeb(AlipayConfig alipay, TradeVo trade) throws Exception {
        if(alipay.getId() == null){
            throw new BadRequestException("请先添加相应�?置，�?�?作");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(alipay.getGatewayUrl(), alipay.getAppID(), alipay.getPrivateKey(), alipay.getFormat(), alipay.getCharset(), alipay.getPublicKey(), alipay.getSignType());

        double money = Double.parseDouble(trade.getTotalAmount());
        if(money <= 0 || money >= 5000){
            throw new BadRequestException("测试金�?过大");
        }

        /**
         * 创建API对应的request(手机网页版)
         */
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();

        /**
         * 订�?�完�?�?�返回的页�?�和异步通知地�?�
         */
        request.setReturnUrl(alipay.getReturnUrl());
        request.setNotifyUrl(alipay.getNotifyUrl());
        /**
         *  填充订�?��?�数
         */
        request.setBizContent("{" +
                "    \"out_trade_no\":\""+trade.getOutTradeNo()+"\"," +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                "    \"total_amount\":"+trade.getTotalAmount()+"," +
                "    \"subject\":\""+trade.getSubject()+"\"," +
                "    \"body\":\""+trade.getBody()+"\"," +
                "    \"extend_params\":{" +
                "    \"sys_service_provider_id\":\""+alipay.getSysServiceProviderId()+"\"" +
                "    }"+
                "  }");//填充业务�?�数
        /**
         * 调用SDK生�?表�?�
         * 通过GET方�?，�?��?�以获�?�url
         */
        return alipayClient.pageExecute(request, "GET").getBody();
    }

    @Override
    public AlipayConfig find() {
        Optional<AlipayConfig> alipayConfig = alipayRepository.findById(1L);
        if (alipayConfig.isPresent()){
            return alipayConfig.get();
        } else {
            return new AlipayConfig();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AlipayConfig update(AlipayConfig alipayConfig) {
        return alipayRepository.save(alipayConfig);
    }
}
