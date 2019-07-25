package org.jeecgframework.web.cgform.engine;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.jeecgframework.core.online.util.FreemarkerHelper;
import org.jeecgframework.core.util.PropertiesUtil;
import org.jeecgframework.web.cgform.common.CgAutoListConstant;
import org.jeecgframework.web.cgform.service.config.CgFormFieldServiceI;
import org.jeecgframework.web.system.service.CacheServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveModel;

@Component("templetContext")
public class TempletContext {
	private static final Logger log = LoggerFactory.getLogger(TempletContext.class);
	
	@Resource(name = "freemarker")
	private Configuration freemarker;
	
	@Autowired
	private CgFormFieldServiceI cgFormFieldService;
	private Map<String, TemplateDirectiveModel> tags;
	private static final String ENCODING = "UTF-8";

	@Autowired
	private CacheServiceI cacheService;
	
	/**
	 * 系统模�?：
	 * PUB-生产（使用ehcache）
	 * DEV-开�?�
	 */
	public static String _sysMode = null;
	static{
		PropertiesUtil util = new PropertiesUtil("sysConfig.properties");
		_sysMode = util.readProperty(CgAutoListConstant.SYS_MODE_KEY);
	}

	@PostConstruct
	public void init() {
		if (tags == null)
			return;
		for (String key : tags.keySet()) {
			freemarker.setSharedVariable(key, tags.get(key));
		}
	}

	public Locale getLocale() {
		return freemarker.getLocale();
	}

	public Template getTemplate(String tableName, String ftlVersion) {
		Template template = null;
		if (tableName == null) {
			return null;
		}
		String oldTableName = tableName;
		//根�?�ftlVersion动�?读�?�模�?�[指定word模�?��?�]
        if (ftlVersion != null && ftlVersion.length() > 0) {
            tableName = tableName + "&ftlVersion=" + ftlVersion;
        }
        
        try {
			if(CgAutoListConstant.SYS_MODE_DEV.equalsIgnoreCase(_sysMode)){//开�?�模�?
				template = freemarker.getTemplate(tableName,freemarker.getLocale(), ENCODING);
			}else if(CgAutoListConstant.SYS_MODE_PUB.equalsIgnoreCase(_sysMode)){//�?�布模�?（缓存）
				//获�?�版本�?�
		    	String version = cgFormFieldService.getCgFormVersionByTableName(oldTableName);
				template = getTemplateFromCache(tableName, ENCODING,version);
			}else{
				throw new RuntimeException("sysConfig.properties的freeMarkerMode�?置错误：(PUB:生产模�?，DEV:开�?�模�?)");
			}
			return template;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * 从缓存中读�?�ftl模�?�
	 * @param template
	 * @param encoding
	 * @version online表�?置版本�?�
	 * @return
	 */
	public Template getTemplateFromCache(String tableName,String encoding,String version){
		Template template =  null;
		try {
			//cache的键：类�??.方法�??.�?�数�??.version
			String cacheKey = this.getClass().getSimpleName()+".getTemplateFormCache."+tableName+"."+version;;
			Object templateObj = cacheService.get(CacheServiceI.SYSTEM_BASE_CACHE,cacheKey);
			if(templateObj==null){
				template = freemarker.getTemplate(tableName,freemarker.getLocale(), ENCODING);
				cacheService.put(CacheServiceI.SYSTEM_BASE_CACHE,cacheKey,template);
				log.info("--setTemplateFromCache-------cacheKey: [{}]-------------",cacheKey);
			}else{
				log.info("--getTemplateFromCache-------cacheKey: [{}]-------------",cacheKey);
				template = (Template)templateObj;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return template;
	}
	
	/**
	 * 从缓存中读�?�ftl模�?�
	 * @param template
	 * @param encoding
	 * @return
	 */
	public void removeTemplateFromCache(String tableName){
		try {
			//获�?�版本�?�
	    	String version = cgFormFieldService.getCgFormVersionByTableName(tableName);
			//cache的键：类�??.方法�??.�?�数�??
			String cacheKey = FreemarkerHelper.class.getName()+".getTemplateFormCache."+tableName+"."+version;
			cacheService.remove(CacheServiceI.SYSTEM_BASE_CACHE,cacheKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Configuration getFreemarker() {
		return freemarker;
	}

	public void setFreemarker(Configuration freemarker) {
		this.freemarker = freemarker;
	}

	public Map<String, TemplateDirectiveModel> getTags() {
		return tags;
	}

	public void setTags(Map<String, TemplateDirectiveModel> tags) {
		this.tags = tags;
	}
	/**
	 * 清空online缓存
	 */
	public void clearCache(){
		cacheService.clean(CacheServiceI.SYSTEM_BASE_CACHE);
	}
}
