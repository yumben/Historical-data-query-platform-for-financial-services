package cn.com.infohold.dao;

import java.util.List;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.MetadataPropertyKey;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetadataPropertyKeyDao extends IDao<MetadataPropertyKey> {
	/**
	 * 根据code查询元数据属性字典对照表
	 * @param code
	 * @return
	 */
	List<MetadataPropertyKey> queryMetadataPropertyKeyVOByCode(String code);
}
