package cn.com.infohold.service;

import java.sql.SQLException;

import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.ret.RetBean;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
public interface IDataExecuteBySqlService {
	RetBean executeBySql(ExecuteBySqlBean executeBySqlBean) throws Exception;

	RetBean executeBatchSql(ExecuteBatchSqlBean executeBatchSqlBean)throws SQLException, Exception;
}
