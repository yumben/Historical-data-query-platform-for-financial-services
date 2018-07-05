package cn.com.infohold.controller;

import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.UpdateBean;
import bdp.commons.dataservice.ret.RetBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.com.infohold.service.IDataQueryService;
import cn.com.infohold.service.IDataUpdateService;
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
public class DataUpdateController {

    @Autowired
    IDataUpdateService dataUpdateServiceImpl;

    @RequestMapping(value = "/update")
    public RetBean dataUpdate(@RequestParam String param) throws Exception {
        RetBean ret = new RetBean();
        try {
            UpdateBean updateBean = AnalysisUtil.toQueryBean(param, UpdateBean.class);
            ret = dataUpdateServiceImpl.updateByJson(updateBean);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
    }
}
