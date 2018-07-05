package cn.com.infohold.dao.impl;

import org.springframework.stereotype.Service;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IMetamodelDatatypeEnumitemsDao;
import cn.com.infohold.entity.MetamodelDatatypeEnumitems;
import cn.com.infohold.mapper.MetamodelDatatypeEnumitemsMapper;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetamodelDatatypeEnumitemsDaoImpl extends MyBatisDaoImpl<MetamodelDatatypeEnumitemsMapper, MetamodelDatatypeEnumitems> implements IMetamodelDatatypeEnumitemsDao {
	
}
