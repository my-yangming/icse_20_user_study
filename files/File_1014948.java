package com.kakarote.crm9.erp.admin.entity;

import com.kakarote.crm9.erp.admin.entity.base.BaseAdminRecord;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class AdminRecord extends BaseAdminRecord<AdminRecord> {
	public static final AdminRecord dao = new AdminRecord().dao();
	//是�?�添加至日程�??醒（0.�?�，1.是）
	private Integer isEvent;

	public Integer getIsEvent() {
		return isEvent;
	}

	public void setIsEvent(Integer isEvent) {
		this.isEvent = isEvent;
	}

	public AdminRecord build(){
		AdminRecord record=new AdminRecord();
		return record._setAttrs(this);
	}
}
