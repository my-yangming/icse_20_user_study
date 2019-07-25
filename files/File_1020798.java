package com.fisher.gen.controller;


import com.fisher.common.annotation.SysLog;
import com.fisher.common.constants.FisherServiceNameConstants;
import com.fisher.common.util.ApiResult;
import com.fisher.gen.model.dto.BuildConfigDTO;
import com.fisher.gen.model.query.TableInfoQuery;
import com.fisher.gen.service.SysGenService;
import com.fisher.gen.service.TableInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
//@RequestMapping("/gen")
@Api(value = "代�?生�?controller", tags = {"代�?生�?接�?�管�?�"})
public class SysGenController {

    private static final String MODULE_NAME = "代�?生�?模�?�";


    @Autowired
    private TableInfoService tableInfoService;

    @Autowired
    private SysGenService sysGenService;

    @SysLog(serviceId = FisherServiceNameConstants.FISHER_GEN_SERVICE, moduleName = MODULE_NAME, actionName = "分页查询数�?�库中所有的表信�?�")
    @ApiOperation(value = "分页查询数�?�库中所有的表信�?�", notes = "分页查询数�?�库中所有的表信�?�", httpMethod = "GET")
    @ApiImplicitParam(name = "query", value = "表信�?�查询�?�件", required = false, dataType = "TableInfoQuery")
    @ResponseBody
    @GetMapping("/code/page")
    public ApiResult<TableInfoQuery> page(TableInfoQuery query){
        return new ApiResult<>(tableInfoService.pageByQuery(query));
    }


    @SysLog(serviceId = FisherServiceNameConstants.FISHER_GEN_SERVICE, moduleName = MODULE_NAME, actionName = "根�?�表�??称生�?代�?  返回zip包")
    @ApiOperation(value = "根�?�表�??称生�?代�?", notes = "根�?�表�??称生�?代�?  返回zip包", httpMethod = "POST")
    @ApiImplicitParam(name = "buildConfigDTO", value = "表�?置", required = true, dataType = "BuildConfigDTO")
    @PostMapping("/code/build")
    public void code(@RequestBody BuildConfigDTO buildConfigDTO, HttpServletResponse response) throws IOException {

        byte[] data = sysGenService.genCodeByTableName(buildConfigDTO);
        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"code.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }

}
