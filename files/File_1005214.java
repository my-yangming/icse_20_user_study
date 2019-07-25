package org.jeecgframework.web.black.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.jwt.util.GsonUtil;
import org.jeecgframework.jwt.util.ResponseMessage;
import org.jeecgframework.jwt.util.Result;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.black.entity.TsBlackListEntity;
import org.jeecgframework.web.black.service.TsBlackListServiceI;
import org.jeecgframework.web.system.enums.InterfaceEnum;
import org.jeecgframework.web.system.pojo.base.InterfaceRuleDto;
import org.jeecgframework.web.system.service.SystemService;
import org.jeecgframework.web.system.util.InterfaceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**   
 * @Title: Controller  
 * @Description: 黑�??�?�
 * @author onlineGenerator
 * @date 2017-05-18 22:33:13
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/tsBlackListController")
@Api(value = "测试用黑�??�?��?务", description = "测试用黑�??�?��?务接�?�", tags = "sysBlackAPI")
public class TsBlackListController extends BaseController {
	private static final Logger logger = LoggerFactory.getLogger(TsBlackListController.class);
	
	@Autowired
	private TsBlackListServiceI tsBlackListService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	


	/**
	 * 黑�??�?�列表 页�?�跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("jeecg/black/tsBlackListList");
	}

	/**
	 * easyui AJAX请求数�?�
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(TsBlackListEntity tsBlackList,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TsBlackListEntity.class, dataGrid);
		//查询�?�件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tsBlackList, request.getParameterMap());
		try{
		//自定义追加查询�?�件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.tsBlackListService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 删除黑�??�?�
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(TsBlackListEntity tsBlackList, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		tsBlackList = systemService.getEntity(TsBlackListEntity.class, tsBlackList.getId());
		message = "黑�??�?�删除�?功";
		try{
			tsBlackListService.delete(tsBlackList);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "黑�??�?�删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批�?删除黑�??�?�
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "黑�??�?�删除�?功";
		try{
			for(String id:ids.split(",")){
				TsBlackListEntity tsBlackList = systemService.getEntity(TsBlackListEntity.class, 
				id
				);
				tsBlackListService.delete(tsBlackList);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "黑�??�?�删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}


	/**
	 * 添加黑�??�?�
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(TsBlackListEntity tsBlackList, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "黑�??�?�添加�?功";
		try{
			tsBlackListService.save(tsBlackList);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);

		}catch(DataIntegrityViolationException ce){
			j.setSuccess(false);
			message = "该IP:"+tsBlackList.getIp()+"已存在!";

		}catch(Exception e){
			e.printStackTrace();
			message = "黑�??�?�添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 更新黑�??�?�
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(TsBlackListEntity tsBlackList, HttpServletRequest request) {
		String message = null;
		AjaxJson j = new AjaxJson();
		message = "黑�??�?�更新�?功";
		TsBlackListEntity t = tsBlackListService.get(TsBlackListEntity.class, tsBlackList.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(tsBlackList, t);
			tsBlackListService.saveOrUpdate(t);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);

		} catch(DataIntegrityViolationException ce){
			j.setSuccess(false);
			message = "该IP:"+tsBlackList.getIp()+"已存在!";

		}catch (Exception e) {
			e.printStackTrace();
			message = "黑�??�?�更新失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}
	

	/**
	 * 黑�??�?�新增页�?�跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(TsBlackListEntity tsBlackList, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tsBlackList.getId())) {
			tsBlackList = tsBlackListService.getEntity(TsBlackListEntity.class, tsBlackList.getId());
			req.setAttribute("tsBlackListPage", tsBlackList);
		}
		return new ModelAndView("jeecg/black/tsBlackList-add");
	}
	/**
	 * 黑�??�?�编辑页�?�跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(TsBlackListEntity tsBlackList, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(tsBlackList.getId())) {
			tsBlackList = tsBlackListService.getEntity(TsBlackListEntity.class, tsBlackList.getId());
			req.setAttribute("tsBlackListPage", tsBlackList);
		}
		return new ModelAndView("jeecg/black/tsBlackList-update");
	}
	
	/**
	 * 导入功能跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name","tsBlackListController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}
	
	/**
	 * 导出excel
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXls")
	public String exportXls(TsBlackListEntity tsBlackList,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
		CriteriaQuery cq = new CriteriaQuery(TsBlackListEntity.class, dataGrid);
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, tsBlackList, request.getParameterMap());
		List<TsBlackListEntity> tsBlackLists = this.tsBlackListService.getListByCriteriaQuery(cq,false);
		modelMap.put(NormalExcelConstants.FILE_NAME,"黑�??�?�");
		modelMap.put(NormalExcelConstants.CLASS,TsBlackListEntity.class);
		modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("黑�??�?�列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
			"导出信�?�"));
		modelMap.put(NormalExcelConstants.DATA_LIST,tsBlackLists);
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	 * 导出excel 使模�?�
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(TsBlackListEntity tsBlackList,HttpServletRequest request,HttpServletResponse response
			, DataGrid dataGrid,ModelMap modelMap) {
    	modelMap.put(NormalExcelConstants.FILE_NAME,"黑�??�?�");
    	modelMap.put(NormalExcelConstants.CLASS,TsBlackListEntity.class);
    	modelMap.put(NormalExcelConstants.PARAMS,new ExportParams("黑�??�?�列表", "导出人:"+ResourceUtil.getSessionUser().getRealName(),
    	"导出信�?�"));
    	modelMap.put(NormalExcelConstants.DATA_LIST,new ArrayList());
    	return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "importExcel", method = RequestMethod.POST)
	@ResponseBody
	public AjaxJson importExcel(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile file = entity.getValue();// 获�?�上传文件对象
			ImportParams params = new ImportParams();
			params.setTitleRows(2);
			params.setHeadRows(1);
			params.setNeedSave(true);
			try {
				List<TsBlackListEntity> listTsBlackListEntitys = ExcelImportUtil.importExcel(file.getInputStream(),TsBlackListEntity.class,params);
				for (TsBlackListEntity tsBlackList : listTsBlackListEntitys) {
					tsBlackListService.save(tsBlackList);
				}
				j.setMsg("文件导入�?功�?");
			} catch (Exception e) {
				j.setMsg("文件导入失败�?");
				logger.error(ExceptionUtil.getExceptionMessage(e));
			}finally{
				try {
					file.getInputStream().close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return j;
	}
	
	
	@ApiOperation(value = "黑�??�?�列表数�?�", produces = "application/json", httpMethod = "GET")
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseMessage<List<TsBlackListEntity>> list(HttpServletRequest request, HttpServletResponse response) {
		InterfaceRuleDto interfaceRuleDto = InterfaceUtil.getInterfaceRuleDto(request, InterfaceEnum.blacklist_list);
		if(interfaceRuleDto==null){
			return Result.error("您没有该接�?�的�?��?�?");
		}
		CriteriaQuery query=new CriteriaQuery(TsBlackListEntity.class);
		InterfaceUtil.installCriteriaQuery(query, interfaceRuleDto, InterfaceEnum.blacklist_list);
		query.add();
		List<TsBlackListEntity> listTsBlackLists = this.tsBlackListService.getListByCriteriaQuery(query, false);
		return Result.success(listTsBlackLists);
	}

	@ApiOperation(value = "根�?�ID获�?�黑�??�?�信�?�", notes = "根�?�ID获�?�黑�??�?�信�?�", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseMessage<?> get(@PathVariable("id") String id,HttpServletRequest request) {
		InterfaceRuleDto interfaceRuleDto = InterfaceUtil.getInterfaceRuleDto(request, InterfaceEnum.blacklist_get);
		if(interfaceRuleDto==null){
			return Result.error("您没有该接�?�的�?��?�?");
		}
		// 验�?
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID�?能为空");
		}
		TsBlackListEntity task = tsBlackListService.get(TsBlackListEntity.class, id);
		return Result.success(task);
	}

	@ApiOperation(value = "创建黑�??�?�")
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseMessage<?> create(@RequestBody TsBlackListEntity tsBlackList,HttpServletRequest request) {
		InterfaceRuleDto interfaceRuleDto = InterfaceUtil.getInterfaceRuleDto(request, InterfaceEnum.blacklist_add);
		if(interfaceRuleDto==null){
			return Result.error("您没有该接�?�的�?��?�?");
		}
		logger.info("create[{}]" , GsonUtil.toJson(tsBlackList));
		//调用JSR303 Bean Validator进行校验，如果出错返回1000错误�?�?�json格�?的错误信�?�.
		Set<ConstraintViolation<TsBlackListEntity>> failures = validator.validate(tsBlackList);
		if (!failures.isEmpty()) {
			return Result.errorValid(BeanValidators.extractPropertyAndMessage(failures));
		}
		
		// 验�?
//		if (StringUtils.isEmpty(tsBlackList.getIp())) {
//			return Result.error("IP�?能为空");
//		}
		// �?存
		try {
			tsBlackListService.save(tsBlackList);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("黑�??�?��?存失败");
		}
		return Result.success(tsBlackList);
	}

	@ApiOperation(value = "更新黑�??�?�", notes = "更新黑�??�?�")
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseMessage<?> update(@RequestBody TsBlackListEntity tsBlackList,HttpServletRequest request) {
		InterfaceRuleDto interfaceRuleDto = InterfaceUtil.getInterfaceRuleDto(request, InterfaceEnum.blacklist_edit);
		if(interfaceRuleDto==null){
			return Result.error("您没有该接�?�的�?��?�?");
		}
		logger.info("update[{}]" , GsonUtil.toJson(tsBlackList));
		//调用JSR303 Bean Validator进行校验，如果出错返回1000错误�?�?�json格�?的错误信�?�.
		Set<ConstraintViolation<TsBlackListEntity>> failures = validator.validate(tsBlackList);
		if (!failures.isEmpty()) {
			return Result.errorValid(BeanValidators.extractPropertyAndMessage(failures));
		}

		// 验�?
		if (StringUtils.isEmpty(tsBlackList.getId())) {
			return Result.error("ID�?能为空");
		}

		// 更新
		try {
			tsBlackListService.saveOrUpdate(tsBlackList);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("黑�??�?�更新失败");
		}
		return Result.success(tsBlackList);
	}

	@ApiOperation(value = "删除黑�??�?�")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseMessage<?> delete(@PathVariable("id") String id,HttpServletRequest request) {
		InterfaceRuleDto interfaceRuleDto = InterfaceUtil.getInterfaceRuleDto(request, InterfaceEnum.blacklist_delete);
		if(interfaceRuleDto==null){
			return Result.error("您没有该接�?�的�?��?�?");
		}
		logger.info("delete[{}]" , id);
		// 验�?
		if (StringUtils.isEmpty(id)) {
			return Result.error("ID�?能为空");
		}
		try {
			tsBlackListService.deleteEntityById(TsBlackListEntity.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("黑�??�?�删除失败");
		}

		return Result.success();
	}
}
