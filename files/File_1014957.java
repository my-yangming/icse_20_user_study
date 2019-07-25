package com.kakarote.crm9.erp.crm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Page;
import com.kakarote.crm9.common.annotation.NotNullValidate;
import com.kakarote.crm9.common.annotation.Permissions;
import com.kakarote.crm9.common.config.paragetter.BasePageRequest;
import com.kakarote.crm9.erp.admin.entity.AdminRecord;
import com.kakarote.crm9.erp.admin.service.AdminSceneService;
import com.kakarote.crm9.erp.crm.common.CrmEnum;
import com.kakarote.crm9.erp.crm.entity.CrmContract;
import com.kakarote.crm9.erp.crm.entity.CrmContractProduct;
import com.kakarote.crm9.erp.crm.entity.CrmReceivables;
import com.kakarote.crm9.erp.crm.service.CrmContractService;
import com.kakarote.crm9.erp.crm.service.CrmReceivablesPlanService;
import com.kakarote.crm9.erp.crm.service.CrmReceivablesService;
import com.kakarote.crm9.utils.AuthUtil;
import com.kakarote.crm9.utils.R;
import com.jfinal.aop.Inject;
import com.jfinal.core.Controller;
import com.jfinal.core.paragetter.Para;


public class CrmContractController extends Controller {
    @Inject
    private CrmContractService crmContractService;
    @Inject
    private CrmReceivablesService receivablesService;
    @Inject
    private CrmReceivablesPlanService receivablesPlanService;

    @Inject
    private AdminSceneService adminSceneService;

    /**
     * @author wyq
     * 查看列表页
     */
    @Permissions({"crm:contract:index"})
    public void queryPageList(BasePageRequest basePageRequest){
        JSONObject jsonObject = basePageRequest.getJsonObject().fluentPut("type",6);
        basePageRequest.setJsonObject(jsonObject);
        renderJson(adminSceneService.filterConditionAndGetPageList(basePageRequest));
    }

    /**
     * 分页�?�件查询�?��?�
     * @author zxy
     */
    public void queryPage(BasePageRequest<CrmContract> basePageRequest){
        renderJson(R.ok().put("data",crmContractService.queryPage(basePageRequest)));
    }
    /**
     * 根�?�id查询�?��?�
     * @author zxy
     */
    @Permissions("crm:contract:read")
    @NotNullValidate(value = "contractId",message = "�?��?�id�?能为空")
    public void queryById(@Para("contractId") Integer id){
        renderJson(crmContractService.queryById(id));
    }
    /**
     * 根�?�id删除�?��?�
     * @author zxy
     */
    @Permissions("crm:contract:delete")
    @NotNullValidate(value = "contractIds",message = "�?��?�id�?能为空")
    public void deleteByIds(@Para("contractIds") String contractIds){
        renderJson(crmContractService.deleteByIds(contractIds));
    }
    /**
     * @author wyq
     * �?��?�转移
     */
    @Permissions("crm:contract:transfer")
    @NotNullValidate(value = "contractIds",message = "�?��?�id�?能为空")
    @NotNullValidate(value = "newOwnerUserId",message = "负责人id�?能为空")
    @NotNullValidate(value = "transferType",message = "移除方�?�?能为空")
    public void transfer(@Para("")CrmContract crmContract){
        renderJson(crmContractService.transfer(crmContract));
    }
    /**
     * 添加或修改
     * @author zxy
     */
    @Permissions({"crm:contract:save","crm:contract:update"})
    public void saveAndUpdate(){
        String data = getRawData();
        JSONObject jsonObject = JSON.parseObject(data);
        renderJson(crmContractService.saveAndUpdate(jsonObject));
    }
    /**
     * 根�?��?�件查询�?��?�
     * @author zxy
     */
    public void queryList(@Para("")CrmContract crmContract){
        renderJson(R.ok().put("data",crmContractService.queryList(crmContract)));
    }
    /**
     * 根�?��?�件查询�?��?�
     * @author zxy
     */
    @NotNullValidate(value = "id",message = "id�?能为空")
    @NotNullValidate(value = "type",message = "类型�?能为空")
    public void queryListByType(@Para("type") String type,@Para("id")Integer id ){
        renderJson(R.ok().put("data",crmContractService.queryListByType(type,id)));
    }
    /**
     根�?��?��?�批次查询产�?
     * @param batchId
     * @author zxy
     */
    public void queryProductById(@Para("batchId") String batchId){
        renderJson(R.ok().put("data",crmContractService.queryProductById(batchId)));
    }
    /**
     * 根�?��?��?�id查询回款
     * @author zxy
     */
    public void queryReceivablesById(@Para("id") Integer id){
        renderJson(R.ok().put("data",crmContractService.queryReceivablesById(id)));
    }
    /**
     * 根�?��?��?�id查询回款计划
     * @author zxy
     */
    public void queryReceivablesPlanById(@Para("id") Integer id){
        renderJson(R.ok().put("data",crmContractService.queryReceivablesPlanById(id)));
    }

    /**
     * @author wyq
     * 查询团队�?员
     */
    public void getMembers(@Para("contractId")Integer contractId){
        boolean auth = AuthUtil.isCrmAuth(AuthUtil.getCrmTablePara(CrmEnum.CONTRACT_TYPE_KEY.getSign()), contractId);
        if(auth){renderJson(R.noAuth()); return; }
        renderJson(R.ok().put("data",crmContractService.getMembers(contractId)));
    }

    /**
     * @author wyq
     * 编辑团队�?员
     */
    public void updateMembers(@Para("")CrmContract crmContract){
        renderJson(crmContractService.addMember(crmContract));
    }

    /**
     * @author wyq
     * 添加团队�?员
     */
    @Permissions("crm:contract:teamsave")
    public void addMembers(@Para("")CrmContract crmContract){
        renderJson(crmContractService.addMember(crmContract));
    }

    /**
     * @author wyq
     * 删除团队�?员
     */
    public void deleteMembers(@Para("")CrmContract crmContract){
        renderJson(crmContractService.deleteMembers(crmContract));
    }

    /**
     * 查询�?��?�自定义字段
     * @author zxy
     */
    public void queryField(){
        renderJson(R.ok().put("data",crmContractService.queryField()));
    }

    /**
     * @author wyq
     * 添加跟进记录
     */
    @NotNullValidate(value = "typesId",message = "�?��?�id�?能为空")
    @NotNullValidate(value = "content",message = "内容�?能为空")
    @NotNullValidate(value = "category",message = "跟进类型�?能为空")
    public void addRecord(@Para("")AdminRecord adminRecord){
        boolean auth = AuthUtil.isCrmAuth(AuthUtil.getCrmTablePara(CrmEnum.CONTRACT_TYPE_KEY.getSign()), adminRecord.getTypesId());
        if(auth){renderJson(R.noAuth()); return; }
        renderJson(crmContractService.addRecord(adminRecord));
    }

    /**
     * @author wyq
     * 查看跟进记录
     */
    public void getRecord(BasePageRequest<CrmContract> basePageRequest){
        boolean auth = AuthUtil.isCrmAuth(AuthUtil.getCrmTablePara(CrmEnum.CONTRACT_TYPE_KEY.getSign()), basePageRequest.getData().getContractId());
        if(auth){renderJson(R.noAuth()); return; }
        renderJson(R.ok().put("data",crmContractService.getRecord(basePageRequest)));
    }
    /**
     * 根�?��?��?�ID查询回款
     * @author zxy
     */
    public void qureyReceivablesListByContractId(BasePageRequest<CrmReceivables> basePageRequest){
        boolean auth = AuthUtil.isCrmAuth(AuthUtil.getCrmTablePara(CrmEnum.CONTRACT_TYPE_KEY.getSign()), basePageRequest.getData().getContractId());
        if(auth){renderJson(R.noAuth()); return; }
        renderJson(receivablesService.qureyListByContractId(basePageRequest));
    }
    /**
     * 根�?��?��?�ID查询产�?
     * @author zxy
     */
    public void qureyProductListByContractId(BasePageRequest<CrmContractProduct> basePageRequest){
        boolean auth = AuthUtil.isCrmAuth(AuthUtil.getCrmTablePara(CrmEnum.CONTRACT_TYPE_KEY.getSign()), basePageRequest.getData().getContractId());
        if(auth){renderJson(R.noAuth()); return; }
        renderJson(crmContractService.qureyProductListByContractId(basePageRequest));
    }
    /**
     * 根�?��?��?�ID查询回款计划
     * @author zxy
     */
    public void qureyReceivablesPlanListByContractId(BasePageRequest<CrmReceivables> basePageRequest){
        boolean auth = AuthUtil.isCrmAuth(AuthUtil.getCrmTablePara(CrmEnum.CONTRACT_TYPE_KEY.getSign()), basePageRequest.getData().getContractId());
        if(auth){renderJson(R.noAuth()); return; }
        renderJson(receivablesPlanService.qureyListByContractId(basePageRequest));
    }

    /**
     * 查询�?��?�到期�??醒设置
     */
    public void queryContractConfig(){
        renderJson(crmContractService.queryContractConfig());
    }

    /**
     * 修改�?��?�到期�??醒设置
     */
    @NotNullValidate(value = "status",message = "status�?能为空")
    @NotNullValidate(value = "contractDay",message = "contractDay�?能为空")
    public void setContractConfig(@Para("status") Integer status,@Para("contractDay") Integer contractDay){
        renderJson(crmContractService.setContractConfig(status,contractDay));
    }
}
