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
package com.roncoo.pay.account.entity;

import com.roncoo.pay.account.enums.AccountFundDirectionEnum;
import com.roncoo.pay.common.core.entity.BaseEntity;
import com.roncoo.pay.common.core.utils.DateUtils;
import com.roncoo.pay.trade.enums.TrxTypeEnum;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 账户历�?�信�?�
 * 龙果学院：www.roncoo.com
 * @author：zenghao
 */
public class RpAccountHistory extends BaseEntity implements Serializable {

	/** 账户编�?� **/
    private String accountNo;

    /** 金�? **/
    private BigDecimal amount;

    /** 账户余�? **/
    private BigDecimal balance;

    /** 资金�?�动方�?� **/
    private String fundDirection;

    /** 是�?��?许结算 **/
    private String isAllowSett;

    /** 是�?�完�?结算 **/
    private String isCompleteSett;

    /** 请求�?� **/
    private String requestNo;

    /** 银行�?水�?� **/
    private String bankTrxNo;

    /** 业务类型 **/
    private String trxType;

    /** 风险预存期 **/
    private Integer riskDay;

    /** 用户编�?� **/
    private String userNo;

    private static final long serialVersionUID = 1L;

    /** 用户�?? **/
    private String userName;

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getFundDirection() {
        return fundDirection;
    }

    public void setFundDirection(String fundDirection) {
        this.fundDirection = fundDirection;
    }
    
    public String getFundDirectionDesc() {
    	return AccountFundDirectionEnum.getEnum(this.getFundDirection()).getLabel();
    }

    public String getIsAllowSett() {
        return isAllowSett;
    }

    public void setIsAllowSett(String isAllowSett) {
        this.isAllowSett = isAllowSett == null ? null : isAllowSett.trim();
    }

    public String getIsCompleteSett() {
        return isCompleteSett;
    }

    public void setIsCompleteSett(String isCompleteSett) {
        this.isCompleteSett = isCompleteSett == null ? null : isCompleteSett.trim();
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo == null ? null : requestNo.trim();
    }

    public String getBankTrxNo() {
        return bankTrxNo;
    }

    public void setBankTrxNo(String bankTrxNo) {
        this.bankTrxNo = bankTrxNo == null ? null : bankTrxNo.trim();
    }

    public String getTrxType() {
        return trxType;
    }

    public void setTrxType(String trxType) {
        this.trxType = trxType == null ? null : trxType.trim();
    }
    
    public String getTrxTypeDesc() {
    	return TrxTypeEnum.getEnum(this.getTrxType()).getDesc();
    }

    public Integer getRiskDay() {
        return riskDay;
    }

    public void setRiskDay(Integer riskDay) {
        this.riskDay = riskDay;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo == null ? null : userNo.trim();
    }
    
    public String getAmountDesc() {
    	if(this.getFundDirection().equals(AccountFundDirectionEnum.ADD.name())){
    		return "<span style=\"color: blue;\">+"+this.amount.doubleValue()+"</span>";
    	}else{
    		return "<span style=\"color: red;\">-"+this.amount.doubleValue()+"</span>";
    	}
    }
    
    public String getCreateTimeDesc() {
    	return DateUtils.formatDate(this.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
    }
}
