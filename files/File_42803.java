/*
 * Copyright 2015-2102 RonCoo(http://www.roncoo.com) Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.roncoo.pay.controller;

/**
 * <b>功能说明:�?��?�通知结果控制类
 * </b>
 *
 * @author Peter
 * <a href="http://www.roncoo.com">龙果学院(www.roncoo.com)</a>
 */

import com.roncoo.pay.common.core.enums.PayWayEnum;
import com.roncoo.pay.common.core.utils.StringUtil;
import com.roncoo.pay.trade.service.RpTradePaymentManagerService;
import com.roncoo.pay.trade.utils.WeiXinPayUtils;
import com.roncoo.pay.trade.utils.alipay.util.AliPayUtil;
import com.roncoo.pay.trade.vo.OrderPayResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping(value = "/scanPayNotify")
public class ScanPayNotifyController {

    @Autowired
    private RpTradePaymentManagerService rpTradePaymentManagerService;

    @RequestMapping("/notify/{payWayCode}")
    public void notify(@PathVariable("payWayCode") String payWayCode, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        Map<String, String> notifyMap = new HashMap<String, String>();
        if (PayWayEnum.WEIXIN.name().equals(payWayCode) || "WEIXIN_PROGRAM".equals(payWayCode)) {
            InputStream inputStream = httpServletRequest.getInputStream();// 从request中�?�得输入�?
            notifyMap = WeiXinPayUtils.parseXml(inputStream);
        } else if (PayWayEnum.ALIPAY.name().equals(payWayCode)) {
            Map<String, String[]> requestParams = httpServletRequest.getParameterMap();
            notifyMap = AliPayUtil.parseNotifyMsg(requestParams);
        }

        String completeWeiXinScanPay = rpTradePaymentManagerService.completeScanPay(payWayCode, notifyMap);
        if (!StringUtil.isEmpty(completeWeiXinScanPay)) {
            if (PayWayEnum.WEIXIN.name().equals(payWayCode)) {
                httpServletResponse.setContentType("text/xml");
            }
            httpServletResponse.getWriter().print(completeWeiXinScanPay);
        }
    }

    @RequestMapping("/result/{payWayCode}")
    public String result(@PathVariable("payWayCode") String payWayCode, HttpServletRequest httpServletRequest, Model model) throws Exception {

        Map<String, String> resultMap = new HashMap<String, String>();
        Map requestParams = httpServletRequest.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱�?解决，这段代�?在出现乱�?时使用。如果mysign和sign�?相等也�?�以使用这段代�?转化
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            valueStr = new String(valueStr);
            resultMap.put(name, valueStr);
        }

        OrderPayResultVo scanPayByResult = rpTradePaymentManagerService.completeScanPayByResult(payWayCode, resultMap);
        model.addAttribute("scanPayByResult", scanPayByResult);

        return "PayResult";
    }

}
