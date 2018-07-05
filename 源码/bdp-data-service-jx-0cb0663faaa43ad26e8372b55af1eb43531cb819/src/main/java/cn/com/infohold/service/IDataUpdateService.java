package cn.com.infohold.service;

import bdp.commons.dataservice.param.UpdateBean;
import bdp.commons.dataservice.ret.RetBean;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
public interface IDataUpdateService {
	RetBean updateByJson(UpdateBean updateBean  ) throws Exception;
}
