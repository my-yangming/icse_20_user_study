package org.hswebframework.web.controller.datasource;

import io.swagger.annotations.Api;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.commons.entity.param.QueryParamEntity;
import org.hswebframework.web.controller.SimpleGenericEntityController;
import org.hswebframework.web.entity.datasource.DataSourceConfigEntity;
import org.hswebframework.web.logging.AccessLogger;
import  org.hswebframework.web.service.datasource.DataSourceConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  数�?��?�?置
 *
 * @author hsweb-generator-online
 */
@RestController
@RequestMapping("datasource/config")
@Authorize(permission = "data-source-config",description = "动�?数�?��?管�?�")
@Api(value = "数�?��?�?置",tags = "动�?数�?��?-数�?��?�?置")
public class DataSourceConfigController implements SimpleGenericEntityController<DataSourceConfigEntity, String, QueryParamEntity> {

    private DataSourceConfigService dataSourceConfigService;
  
    @Autowired
    public void setDataSourceConfigService(DataSourceConfigService dataSourceConfigService) {
        this.dataSourceConfigService = dataSourceConfigService;
    }
  
    @Override
    public DataSourceConfigService getService() {
        return dataSourceConfigService;
    }
}
