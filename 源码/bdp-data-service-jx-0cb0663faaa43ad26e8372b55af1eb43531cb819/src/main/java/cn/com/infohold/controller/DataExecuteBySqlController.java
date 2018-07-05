package cn.com.infohold.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.service.IDataExecuteBySqlService;
import cn.com.infohold.util.AnalysisUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
@RestController
@Log4j2
public class DataExecuteBySqlController {

    @Autowired
    IDataExecuteBySqlService dataExecuteBySqlServiceImpl;

    @RequestMapping(value = "/executeBySql")
    public RetBean executeBySql(@RequestParam String param) throws Exception {
        RetBean ret = new RetBean();
        try {
        	ExecuteBySqlBean executeBySqlBean = AnalysisUtil.toQueryBean(param, ExecuteBySqlBean.class);
            ret = dataExecuteBySqlServiceImpl.executeBySql(executeBySqlBean);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
    }
    
    @RequestMapping(value = "/executeBatchSql")
    public RetBean executeBatchSql(@RequestParam String param) throws Exception {
        RetBean ret = new RetBean();
        try {
        	ExecuteBatchSqlBean executeBySqlBeans = AnalysisUtil.toQueryBean(param, ExecuteBatchSqlBean.class);
            ret = dataExecuteBySqlServiceImpl.executeBatchSql(executeBySqlBeans);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
    }
}
