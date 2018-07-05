package cn.com.infohold.service;

import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.bean.SqlBean;
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
public interface IDataQueryService {

	RetBean queryListByJson(QueryBean queryBean) throws Exception;

	RetBean queryBySql(ExecuteBySqlBean queryBean) throws Exception;

	RetBean queryBatchSql(ExecuteBatchSqlBean executeBatchSqlBean) throws Exception;

	RetBean queryMultiTable(SqlBean sqlBean) throws Exception;
}
