package cn.com.infohold.service;

import bdp.commons.dataservice.param.DeteleBean;
import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.ret.RetBean;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
public interface IDataDeleteService {
	RetBean deleteByJson(DeteleBean deleteBean) throws Exception;

	RetBean deleteBatch(ExecuteBatchSqlBean executeBatchSqlBean)throws Exception;
}
