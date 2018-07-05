package cn.com.infohold.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bdp.commons.dataservice.param.DeteleBean;
import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.service.IDataDeleteService;
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
public class DataDeleteController {

    @Autowired
    IDataDeleteService dataDeleteServiceImpl; 


    @RequestMapping(value = "/delete")
    public RetBean dataDelete(@RequestParam String param) throws Exception {
        RetBean ret = new RetBean();
        try {
            DeteleBean deteleBean = AnalysisUtil.toQueryBean(param, DeteleBean.class);
            ret = dataDeleteServiceImpl.deleteByJson(deteleBean);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
         
    }
    @RequestMapping(value = "/deleteBatch")
    public RetBean deleteBatch(@RequestParam String param) throws Exception {
        RetBean ret = new RetBean();
        try {
        	ExecuteBatchSqlBean executeBatchSqlBean = AnalysisUtil.toQueryBean(param, ExecuteBatchSqlBean.class);
            ret = dataDeleteServiceImpl.deleteBatch(executeBatchSqlBean);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
         
    }
    
}
