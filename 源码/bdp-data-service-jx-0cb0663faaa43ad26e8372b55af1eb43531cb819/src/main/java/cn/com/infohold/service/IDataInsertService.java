package cn.com.infohold.service;

import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.param.InsertBean;
import bdp.commons.dataservice.ret.RetBean;
import java.io.IOException;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
public interface IDataInsertService {
	RetBean insertByJson(InsertBean ib) throws IOException, Exception;
	RetBean insertAuth() throws IOException, Exception;
	
	RetBean insertBatch(ExecuteBatchSqlBean executeBySqlBeans)throws IOException, Exception;
}
