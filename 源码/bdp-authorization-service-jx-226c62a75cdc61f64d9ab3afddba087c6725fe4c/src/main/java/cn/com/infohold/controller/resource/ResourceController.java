package cn.com.infohold.controller.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bdp.commons.authorization.resource.AuthResource;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.service.resource.IResourceService;
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
public class ResourceController {

    @Autowired
    IResourceService resourceServiceImpl;

    @RequestMapping(value = "/resourceQuery")
    public RetBean resourceQuery(@RequestParam String param) throws Exception {
        RetBean ret = new RetBean();
        try {
        	 System.out.println("param = "+param);
             QueryBean queryBean = AnalysisUtil.toJavaBean(param, QueryBean.class);
             ret = resourceServiceImpl.resourceQuery(queryBean);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
    }
    
    @RequestMapping(value = "/getResourceByJson")
    public RetBean getResourceByJson(@RequestParam String param) throws Exception {
        RetBean ret = new RetBean();
        try {
        	 System.out.println("param = "+param);
             QueryBean queryBean = AnalysisUtil.toJavaBean(param, QueryBean.class);
             ret = resourceServiceImpl.getResourceByJson(queryBean);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
    }
    
    @RequestMapping(value = "/resourceQueryById")
    public RetBean resourceQueryById(String id) throws Exception {
        RetBean ret = new RetBean();
        try {
        	 System.out.println("param = "+id);
             ret = resourceServiceImpl.resourceQueryById(id);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
    }
    
    @RequestMapping(value = "/resourceInsert")
    public RetBean resourceInsert(@RequestParam String param) throws Exception {
        RetBean ret = new RetBean();
        try {
        	AuthResource authResource = AnalysisUtil.toJavaBean(param, AuthResource.class);
            ret = resourceServiceImpl.resourceInsert(authResource);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
    }
    
    @RequestMapping(value = "/insertObj")
    public RetBean insertObj(@RequestParam String param) throws Exception {
        RetBean ret = new RetBean();
        try {
        	AuthResource authResource = AnalysisUtil.toJavaBean(param, AuthResource.class);
            ret = resourceServiceImpl.insertObj(authResource);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
    }
    
    @RequestMapping(value = "/resourceDelete")
    public RetBean resourceDelete(@RequestParam String id) throws Exception {
        RetBean ret = new RetBean();
        try {
        	 ret = resourceServiceImpl.resourceDelete(id);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
         
    }
    
    @RequestMapping(value = "/resourceUpdate")
    public RetBean resourceUpdate(@RequestParam String param) throws Exception {
        RetBean ret = new RetBean();
        try {
        	AuthResource authResource = AnalysisUtil.toJavaBean(param, AuthResource.class);
            ret = resourceServiceImpl.resourceUpdate(authResource);
        } catch (Exception ex) {
            ret.setRet_code("-1");
            ret.setRet_message(ex.getLocalizedMessage());
            log.error(ex);
            throw ex;
        }
        return ret;
    }
    
}
