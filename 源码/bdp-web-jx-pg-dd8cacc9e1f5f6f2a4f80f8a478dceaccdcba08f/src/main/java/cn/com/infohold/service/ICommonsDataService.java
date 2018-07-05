package cn.com.infohold.service;

import bdp.commons.dataservice.param.DeteleBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.param.InsertBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.UpdateBean;
import bdp.commons.dataservice.ret.RetBean;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-17
 */
public interface ICommonsDataService {
	String queryDictionary(QueryBean queryBean);

	RetBean queryData(QueryBean queryBean);

	RetBean insertData(InsertBean insertBean);

	RetBean updateData(UpdateBean updateBean);

	RetBean deleteData(DeteleBean deteleBean);

	RetBean queryDataSql(ExecuteBySqlBean executeBySqlBean);

}
