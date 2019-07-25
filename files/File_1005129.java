package com.jeecg.demo.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.formula.functions.T;
import org.jeecgframework.core.beanvalidator.BeanValidators;
import org.jeecgframework.core.common.controller.BaseController;
import org.jeecgframework.core.common.exception.BusinessException;
import org.jeecgframework.core.common.hibernate.qbc.CriteriaQuery;
import org.jeecgframework.core.common.model.json.AjaxJson;
import org.jeecgframework.core.common.model.json.DataGrid;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.ExceptionUtil;
import org.jeecgframework.core.util.MyBeanUtils;
import org.jeecgframework.core.util.ReflectHelper;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.StringUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.entity.vo.NormalExcelConstants;
import org.jeecgframework.tag.core.easyui.TagUtil;
import org.jeecgframework.web.cgform.entity.upload.CgUploadEntity;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.system.pojo.base.TSDepart;
import org.jeecgframework.web.system.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSONObject;
import com.jeecg.demo.entity.JformOrderCustomerEntity;
import com.jeecg.demo.entity.JformOrderMainEntity;
import com.jeecg.demo.entity.JformOrderTicketEntity;
import com.jeecg.demo.page.JformOrderCustomerPage;
import com.jeecg.demo.page.JformOrderMainPage;
import com.jeecg.demo.service.JformOrderMainServiceI;
/**   
 * @Title: Controller
 * @Description: 订�?�主信�?�
 * @author onlineGenerator
 * @date 2017-09-17 11:49:08
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/jformOrderMainController")
@Api(value="orderMainRest",description="一对多订�?�管�?�",tags="JformOrderMainController")
public class JformOrderMainController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(JformOrderMainController.class);

	@Autowired
	private JformOrderMainServiceI jformOrderMainService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private Validator validator;
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;

	/**
	 * 订�?�主信�?�列表 页�?�跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "list")
	public ModelAndView list(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/demo/orderOne2Many/jformOrderMainList");
	}
	
	/**
	 * 订�?�主信�?�列表 页�?�跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "mainlist")
	public ModelAndView mainlist(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/demo/orderOne2Many/jformOrderMainListBase");
	}
	
	/**
	 * 订�?�主信�?�列表 页�?�跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "customerlist")
	public ModelAndView customerlist(HttpServletRequest request) {
		return new ModelAndView("com/jeecg/demo/orderOne2Many/jformOrderCustomerListBase");
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
	public void datagrid(JformOrderMainEntity jformOrderMain,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformOrderMainEntity.class, dataGrid);
		//查询�?�件组装器
		org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformOrderMain);
		try{
		//自定义追加查询�?�件
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.jformOrderMainService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * easyui AJAX请求数�?�
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */
	
	@RequestMapping(params = "customerDatagrid")
	public void customerDatagrid(JformOrderCustomerEntity jformCustomer,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(JformOrderCustomerEntity.class, dataGrid);
		if(jformCustomer.getFkId() == null || "".equals(jformCustomer.getFkId())){
		}else{
			//查询�?�件组装器
			org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformCustomer);
			cq.add();
			this.jformOrderMainService.getDataGridReturn(cq, true);
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除订�?�主信�?�
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(JformOrderMainEntity jformOrderMain, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		jformOrderMain = systemService.getEntity(JformOrderMainEntity.class, jformOrderMain.getId());
		String message = "订�?�主信�?�删除�?功";
		try{
			jformOrderMainService.delMain(jformOrderMain);
			systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "订�?�主信�?�删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批�?删除订�?�主信�?�
	 * 
	 * @return
	 */
	 @RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		String message = "订�?�主信�?�删除�?功";
		try{
			for(String id:ids.split(",")){
				JformOrderMainEntity jformOrderMain = systemService.getEntity(JformOrderMainEntity.class,
				id
				);
				jformOrderMainService.delMain(jformOrderMain);
				systemService.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			}
		}catch(Exception e){
			e.printStackTrace();
			message = "订�?�主信�?�删除失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加订�?�主信�?�
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(JformOrderMainEntity jformOrderMain,JformOrderMainPage jformOrderMainPage, HttpServletRequest request) {
		List<JformOrderCustomerEntity> jformOrderCustomerList =  jformOrderMainPage.getJformOrderCustomerList();
		List<JformOrderTicketEntity> jformOrderTicketList =  jformOrderMainPage.getJformOrderTicketList();
		AjaxJson j = new AjaxJson();
		String message = "添加�?功";
		try{
			jformOrderMainService.addMain(jformOrderMain, jformOrderCustomerList,jformOrderTicketList);
			systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "订�?�主信�?�添加失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		j.setObj(jformOrderMain);
		return j;
	}
	/**
	 * 更新订�?�主信�?�
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(JformOrderMainEntity jformOrderMain,JformOrderMainPage jformOrderMainPage, HttpServletRequest request) {
		List<JformOrderCustomerEntity> jformOrderCustomerList =  jformOrderMainPage.getJformOrderCustomerList();
		List<JformOrderTicketEntity> jformOrderTicketList =  jformOrderMainPage.getJformOrderTicketList();
		AjaxJson j = new AjaxJson();
		String message = "更新�?功";
		try{
			jformOrderMainService.updateMain(jformOrderMain, jformOrderCustomerList,jformOrderTicketList);
			systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
		}catch(Exception e){
			e.printStackTrace();
			message = "更新订�?�主信�?�失败";
			throw new BusinessException(e.getMessage());
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 订�?�主信�?�新增页�?�跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(JformOrderMainEntity jformOrderMain, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformOrderMain.getId())) {
			jformOrderMain = jformOrderMainService.getEntity(JformOrderMainEntity.class, jformOrderMain.getId());
			req.setAttribute("jformOrderMainPage", jformOrderMain);
		}
		return new ModelAndView("com/jeecg/demo/orderOne2Many/jformOrderMain-add");
	}
	
	/**
	 * 订�?�主信�?�编辑页�?�跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(JformOrderMainEntity jformOrderMain, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(jformOrderMain.getId())) {
			jformOrderMain = jformOrderMainService.getEntity(JformOrderMainEntity.class, jformOrderMain.getId());
			req.setAttribute("jformOrderMainPage", jformOrderMain);
		}
		return new ModelAndView("com/jeecg/demo/orderOne2Many/jformOrderMain-update");
	}
	
	
	/**
	 * 加载明细列表[JformOrderMain�?表]
	 * 
	 * @return
	 */
	@RequestMapping(params = "jformOrderCustomerList")
	public ModelAndView jformOrderCustomerList(JformOrderMainEntity jformOrderMain, HttpServletRequest req) {
	
		//===================================================================================
		//获�?��?�数
		Object id0 = jformOrderMain.getId();
		//===================================================================================
		//查询-JformOrderMain�?表
	    String hql0 = "from JformOrderCustomerEntity where 1 = 1 AND fK_ID = ? ";
	    try{
	    	List<JformOrderCustomerEntity> jformOrderCustomerEntityList = systemService.findHql(hql0,id0);
			req.setAttribute("jformOrderCustomerList", jformOrderCustomerEntityList);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("com/jeecg/demo/orderOne2Many/jformOrderCustomerList");
	}
	/**
	 * 加载明细列表[JformOrderMain�?表]
	 * 
	 * @return
	 */
	@RequestMapping(params = "jformOrderTicketList")
	public ModelAndView jformOrderTicketList(JformOrderMainEntity jformOrderMain, HttpServletRequest req) {
	
		//===================================================================================
		//获�?��?�数
		Object id1 = jformOrderMain.getId();
		//===================================================================================
		//查询-JformOrderMain�?表
	    String hql1 = "from JformOrderTicketEntity where 1 = 1 AND fCK_ID = ? ";
	    try{
	    	List<JformOrderTicketEntity> jformOrderTicketEntityList = systemService.findHql(hql1,id1);
			req.setAttribute("jformOrderTicketList", jformOrderTicketEntityList);
		}catch(Exception e){
			logger.info(e.getMessage());
		}
		return new ModelAndView("com/jeecg/demo/orderOne2Many/jformOrderTicketList");
	}

    /**
    * 导出excel
    *
    * @param request
    * @param response
    */
    @RequestMapping(params = "exportXls")
    public String exportXls(JformOrderMainEntity jformOrderMain,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid,ModelMap map) {
    	CriteriaQuery cq = new CriteriaQuery(JformOrderMainEntity.class, dataGrid);
    	//查询�?�件组装器
    	org.jeecgframework.core.extend.hqlsearch.HqlGenerateUtil.installHql(cq, jformOrderMain);
    	try{
    	//自定义追加查询�?�件
    	}catch (Exception e) {
    		throw new BusinessException(e.getMessage());
    	}
    	cq.add();
    	List<JformOrderMainEntity> list=this.jformOrderMainService.getListByCriteriaQuery(cq, false);
    	List<JformOrderMainPage> pageList=new ArrayList<JformOrderMainPage>();
        if(list!=null&&list.size()>0){
        	for(JformOrderMainEntity entity:list){
        		try{
        		JformOrderMainPage page=new JformOrderMainPage();
        		   MyBeanUtils.copyBeanNotNull2Bean(entity,page);
            	    Object id0 = entity.getId();
				    String hql0 = "from JformOrderCustomerEntity where 1 = 1 AND fK_ID = ? ";
        	        List<JformOrderCustomerEntity> jformOrderCustomerEntityList = systemService.findHql(hql0,id0);
            		page.setJformOrderCustomerList(jformOrderCustomerEntityList);
            	    Object id1 = entity.getId();
				    String hql1 = "from JformOrderTicketEntity where 1 = 1 AND fCK_ID = ? ";
        	        List<JformOrderTicketEntity> jformOrderTicketEntityList = systemService.findHql(hql1,id1);
            		page.setJformOrderTicketList(jformOrderTicketEntityList);
            		pageList.add(page);
            	}catch(Exception e){
            		logger.info(e.getMessage());
            	}
            }
        }
        map.put(NormalExcelConstants.FILE_NAME,"订�?�主信�?�");
        map.put(NormalExcelConstants.CLASS,JformOrderMainPage.class);
        map.put(NormalExcelConstants.PARAMS,new ExportParams("订�?�主信�?�列表", "导出人:Jeecg",
            "导出信�?�"));
        map.put(NormalExcelConstants.DATA_LIST,pageList);
        return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}

    /**
	 * 通过excel导入数�?�
	 * @param request
	 * @param
	 * @return
	 */
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
			params.setHeadRows(2);
			params.setNeedSave(true);
			try {
				List<JformOrderMainPage> list =  ExcelImportUtil.importExcel(file.getInputStream(), JformOrderMainPage.class, params);
				JformOrderMainEntity entity1=null;
				for (JformOrderMainPage page : list) {
					entity1=new JformOrderMainEntity();
					MyBeanUtils.copyBeanNotNull2Bean(page,entity1);
		            jformOrderMainService.addMain(entity1, page.getJformOrderCustomerList(),page.getJformOrderTicketList());
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
	/**
	* 导出excel 使模�?�
	*/
	@RequestMapping(params = "exportXlsByT")
	public String exportXlsByT(ModelMap map) {
		map.put(NormalExcelConstants.FILE_NAME,"订�?�主信�?�");
		map.put(NormalExcelConstants.CLASS,JformOrderMainPage.class);
		map.put(NormalExcelConstants.PARAMS,new ExportParams("订�?�主信�?�列表", "导出人:"+ ResourceUtil.getSessionUser().getRealName(),
		"导出信�?�"));
		map.put(NormalExcelConstants.DATA_LIST,new ArrayList());
		return NormalExcelConstants.JEECG_EXCEL_VIEW;
	}
	/**
	* 导入功能跳转
	*
	* @return
	*/
	@RequestMapping(params = "upload")
	public ModelAndView upload(HttpServletRequest req) {
		req.setAttribute("controller_name", "jformOrderMainController");
		return new ModelAndView("common/upload/pub_excel_upload");
	}

 	
 	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="订�?�列表信�?�",produces="application/json",httpMethod="GET")

 	public List<JformOrderMainPage> list() {
		List<JformOrderMainEntity> list= jformOrderMainService.getList(JformOrderMainEntity.class);
    	List<JformOrderMainPage> pageList=new ArrayList<JformOrderMainPage>();
        if(list!=null&&list.size()>0){
        	for(JformOrderMainEntity entity:list){
        		try{
        			JformOrderMainPage page=new JformOrderMainPage();
        		   MyBeanUtils.copyBeanNotNull2Bean(entity,page);
					Object id0 = entity.getId();
					Object id1 = entity.getId();
				     String hql0 = "from JformOrderCustomerEntity where 1 = 1 AND fK_ID = ? ";
	    			List<JformOrderCustomerEntity> jformOrderCustomerOldList = this.jformOrderMainService.findHql(hql0,id0);
            		page.setJformOrderCustomerList(jformOrderCustomerOldList);
				     String hql1 = "from JformOrderTicketEntity where 1 = 1 AND fCK_ID = ? ";
	    			List<JformOrderTicketEntity> jformOrderTicketOldList = this.jformOrderMainService.findHql(hql1,id1);
            		page.setJformOrderTicketList(jformOrderTicketOldList);
            		pageList.add(page);
            	}catch(Exception e){
            		logger.info(e.getMessage());
            	}
            }
        }
		return pageList;

	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation(value="根�?�ID获�?�订�?�信�?�",notes="根�?�ID获�?�订�?�信�?�",httpMethod="GET",produces="application/json")
	public ResponseEntity<?> get(@PathVariable("id") String id) {
		JformOrderMainEntity task = jformOrderMainService.get(JformOrderMainEntity.class, id);
		if (task == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}

		JformOrderMainPage page = new JformOrderMainPage();
		try {
			MyBeanUtils.copyBeanNotNull2Bean(task, page);
			String hql0 = "from JformOrderCustomerEntity where 1 = 1 AND fK_ID = ? ";
 			List<JformOrderCustomerEntity> jformOrderCustomerOldList = this.jformOrderMainService.findHql(hql0,id);
     		page.setJformOrderCustomerList(jformOrderCustomerOldList);
			String hql1 = "from JformOrderTicketEntity where 1 = 1 AND fCK_ID = ? ";
 			List<JformOrderTicketEntity> jformOrderTicketOldList = this.jformOrderMainService.findHql(hql1,id);
     		page.setJformOrderTicketList(jformOrderTicketOldList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity(page, HttpStatus.OK);

	}
 	
 	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="创建订�?�")
	public ResponseEntity<?> create(@ApiParam(value="订�?�信�?�")@RequestBody JformOrderMainPage jformOrderMainPage, UriComponentsBuilder uriBuilder) {
		//调用JSR303 Bean Validator进行校验，如果出错返回�?�400错误�?�?�json格�?的错误信�?�.
		Set<ConstraintViolation<JformOrderMainPage>> failures = validator.validate(jformOrderMainPage);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//�?存
		List<JformOrderCustomerEntity> jformOrderCustomerList =  jformOrderMainPage.getJformOrderCustomerList();
		List<JformOrderTicketEntity> jformOrderTicketList =  jformOrderMainPage.getJformOrderTicketList();
		
		JformOrderMainEntity jformOrderMain = new JformOrderMainEntity();
		try{

			MyBeanUtils.copyBeanNotNull2Bean(jformOrderMainPage,jformOrderMain);

		}catch(Exception e){
            logger.info(e.getMessage());
        }
		jformOrderMainService.addMain(jformOrderMain, jformOrderCustomerList,jformOrderTicketList);

		//按照Restful风格约定，创建指�?�新任务的url, 也�?�以直接返回id或对象.
		String id = jformOrderMainPage.getId();
		URI uri = uriBuilder.path("/rest/jformOrderMainController/" + id).build().toUri();
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(uri);

		return new ResponseEntity(headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	@ApiOperation(value="更新订�?�",notes="更新订�?�")
	public ResponseEntity<?> update(@ApiParam(value="订�?�信�?�")@RequestBody JformOrderMainPage jformOrderMainPage) {
		//调用JSR303 Bean Validator进行校验，如果出错返回�?�400错误�?�?�json格�?的错误信�?�.
		Set<ConstraintViolation<JformOrderMainPage>> failures = validator.validate(jformOrderMainPage);
		if (!failures.isEmpty()) {
			return new ResponseEntity(BeanValidators.extractPropertyAndMessage(failures), HttpStatus.BAD_REQUEST);
		}

		//�?存
		List<JformOrderCustomerEntity> jformOrderCustomerList =  jformOrderMainPage.getJformOrderCustomerList();
		List<JformOrderTicketEntity> jformOrderTicketList =  jformOrderMainPage.getJformOrderTicketList();
		
		JformOrderMainEntity jformOrderMain = new JformOrderMainEntity();
		try{

			MyBeanUtils.copyBeanNotNull2Bean(jformOrderMainPage,jformOrderMain);

		}catch(Exception e){
            logger.info(e.getMessage());
        }
		jformOrderMainService.updateMain(jformOrderMain, jformOrderCustomerList,jformOrderTicketList);

		//按Restful约定，返回204状�?�?, 无内容. 也�?�以返回200状�?�?.
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="删除订�?�")
	public void delete(@PathVariable("id") String id) {
		JformOrderMainEntity jformOrderMain = jformOrderMainService.get(JformOrderMainEntity.class, id);
		jformOrderMainService.delMain(jformOrderMain);
	}
	/**
	 * 获�?�文件附件信�?�
	 * 
	 * @param id jformOrderMain主键id
	 */
	@RequestMapping(params = "getFiles")
	@ResponseBody
	public AjaxJson getFiles(String id){
		List<CgUploadEntity> uploadBeans = cgFormFieldService.findByProperty(CgUploadEntity.class, "cgformId", id);
		List<Map<String,Object>> files = new ArrayList<Map<String,Object>>(0);
		for(CgUploadEntity b:uploadBeans){
			String title = b.getAttachmenttitle();//附件�??
			String fileKey = b.getId();//附件主键
			String path = b.getRealpath();//附件路径
			String field = b.getCgformField();//表�?�中作为附件控件的字段
			Map<String, Object> file = new HashMap<String, Object>();
			file.put("title", title);
			file.put("fileKey", fileKey);
			file.put("path", path);
			file.put("field", field==null?"":field);
			files.add(file);
		}
		AjaxJson j = new AjaxJson();
		j.setObj(files);
		return j;
	}

	/**
	 * 行编辑�?存�?作
	 * @param page
	 * @return
	 */
	@RequestMapping(params = "saveRows")
	@ResponseBody
	public AjaxJson saveRows(JformOrderCustomerPage page){
		String message = "�?作�?功�?";
		List<JformOrderCustomerEntity> demos=page.getDemos();
		AjaxJson j = new AjaxJson();
		if(CollectionUtils.isNotEmpty(demos)){
			for(JformOrderCustomerEntity jeecgDemo:demos){
				if (StringUtil.isNotEmpty(jeecgDemo.getId())) {
					JformOrderCustomerEntity t =this.systemService.get(JformOrderCustomerEntity.class, jeecgDemo.getId());
					try {
						MyBeanUtils.copyBeanNotNull2Bean(jeecgDemo, t);
						systemService.saveOrUpdate(t);
						systemService.addLog(message, Globals.Log_Type_UPDATE, Globals.Log_Leavel_INFO);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						//jeecgDemo.setStatus("0");
						systemService.save(jeecgDemo);
						systemService.addLog(message, Globals.Log_Type_INSERT, Globals.Log_Leavel_INFO);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		return j;
	}
	
	/**
	 * 树选择页�?�跳转
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "departSelect")
    public String departSelect(HttpServletRequest req) {
    	req.setAttribute("defaultName", req.getParameter("name"));
        return "com/jeecg/demo/orderOne2Many/departSelect";
    }
	
	/**
	 * 树加载
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "getDepartInfo3")
	@ResponseBody
	public AjaxJson getDepartInfo3(HttpServletRequest request, HttpServletResponse response){
		AjaxJson j = new AjaxJson();
		net.sf.json.JSONArray jsonArray = new net.sf.json.JSONArray();
		//String parentid = request.getParameter("parentid");
		String sql = "select id,departname as name,ifnull(parentdepartid,0) as ppp_id,org_code as code from t_s_depart where 1=1 ";
		List<Map<String,Object>> dateList = this.systemService.findForJdbc(sql);
		Map<String,Map<String,Object>> dataMap = new HashMap<String,Map<String,Object>>();
		//TODO �?应该�?次都需�?查询 建议从缓存中�?�到所有的list
		String name = request.getParameter("name");
		if(oConvertUtils.isNotEmpty(name)){
			for (Map<String, Object> map : dateList) {
				String temp = map.get("name").toString();
				String id = map.get("id").toString();
				if(temp.indexOf(name)>=0){
					Object pid = map.get("ppp_id");
					if(temp.equals(name)){
						map.put("checked", true);
					}
					//判断是�?�有�?节点 �?�用isleaf判断
					sql = "select count(1) from t_s_depart t where t.parentdepartid = ?";
					long count = this.systemService.getCountForJdbcParam(sql, new Object[]{id});
					if(count>0){
						map.put("isParent",true);
					}
					dataMap.put(id, map);
					upwardQueryParents(dataMap, dateList, pid==null?"":pid.toString());
				}
			}
			jsonArray =  net.sf.json.JSONArray.fromObject(dataMap.values());
		}else{
			jsonArray =  net.sf.json.JSONArray.fromObject(dateList);
		}
		
		j.setMsg(jsonArray.toString().replace("ppp_id", "pId"));
		return j;
	}
	
	/**
	 * 获�?��?节点
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "getSubContent")
	@ResponseBody
	public AjaxJson getSubContent(HttpServletRequest request, HttpServletResponse response){
		
		AjaxJson j = new AjaxJson();
		String parentid = request.getParameter("parentid");
		List<TSDepart> tSDeparts = new ArrayList<TSDepart>();
		StringBuffer hql = new StringBuffer(" from TSDepart t where 1=1 ");
		if(oConvertUtils.isNotEmpty(parentid)){
			TSDepart dePart = this.systemService.getEntity(TSDepart.class, parentid);
			hql.append(" and TSPDepart = ?");
			tSDeparts = this.systemService.findHql(hql.toString(), dePart);
		}
		//TODO �?应该�?次都需�?查询 建议从缓存中�?�到所有的list �?筛选
		List<Map<String,Object>> dateList = new ArrayList<Map<String,Object>>();
		if(tSDeparts.size()>0){
			Map<String,Object> map = null;
			String sql = null;
			 Object[] params = null;
			for(TSDepart depart:tSDeparts){
				map = new HashMap<String,Object>();
				map.put("id", depart.getId());
				map.put("name", depart.getDepartname());

				map.put("code",depart.getOrgCode());

				TSDepart pdepart = depart.getTSPDepart();
				if(pdepart!=null){
					map.put("pId", pdepart.getId());
				} else{
					map.put("pId", "0");
				}
				//根�?�id判断是�?�有�?节点
				sql = "select count(1) from t_s_depart t where t.parentdepartid = ?";
				params = new Object[]{depart.getId()};
				long count = this.systemService.getCountForJdbcParam(sql, params);
				if(count>0){
					map.put("isParent",true);
				}
				dateList.add(map);
			}
		}
		net.sf.json.JSONArray jsonArray = net.sf.json.JSONArray.fromObject(dateList);
		j.setMsg(jsonArray.toString());
		return j;
	}
	
	/**
	 * �?�上查找父节点
	 */
	private void upwardQueryParents(Map<String,Map<String,Object>> dataMap,List<Map<String,Object>> dateList,String pid){
		String pid_next = null;
		for (Map<String, Object> map : dateList) {
			String id = map.get("id").toString();
			if(pid.equals(id)){
				pid_next = map.get("ppp_id").toString();
				dataMap.put(id, map);
				break;
			}
		}
		if(pid_next!=null && !pid_next.equals("0")){
			upwardQueryParents(dataMap, dateList, pid_next);
		}
	}

}
