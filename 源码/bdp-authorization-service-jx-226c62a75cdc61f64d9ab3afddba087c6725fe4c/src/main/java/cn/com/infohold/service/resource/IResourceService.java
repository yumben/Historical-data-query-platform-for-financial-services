package cn.com.infohold.service.resource;

import bdp.commons.authorization.resource.AuthResource;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.ret.RetBean;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
public interface IResourceService {

	/**
	 * 查询资源
	 * 
	 * @param queryBean
	 * @return
	 * @throws Exception
	 */
	RetBean resourceQuery(QueryBean queryBean) throws Exception;

	/**
	 * 查询资源以及下面的东西
	 * 
	 * @param queryBean
	 * @return
	 * @throws Exception
	 */
	RetBean getResourceByJson(QueryBean queryBean) throws Exception;

	/**
	 * 根据id查询资源
	 * 
	 * @param queryBean
	 * @return
	 * @throws Exception
	 */
	RetBean resourceQueryById(String Id) throws Exception;

	/**
	 * 新增资源
	 * 
	 * @param authResource
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws Exception
	 */
	RetBean resourceInsert(AuthResource authResource)
			throws IllegalArgumentException, IllegalAccessException, Exception;

	/**
	 * 修改资源
	 * 
	 * @param authResource
	 * @return
	 * @throws Exception
	 */
	RetBean resourceUpdate(AuthResource authResource) throws Exception;

	/**
	 * 删除资源
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	RetBean resourceDelete(String id) throws Exception;

	RetBean insertObj(AuthResource authResource) throws Exception;
}
