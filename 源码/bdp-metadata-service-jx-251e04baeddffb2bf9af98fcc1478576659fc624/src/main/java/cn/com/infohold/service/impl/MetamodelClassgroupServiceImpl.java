package cn.com.infohold.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.impl.MetamodelClassgroupDaoImpl;
import cn.com.infohold.entity.MetamodelClassgroup;
import cn.com.infohold.service.IMetamodelClassgroupService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Service
public class MetamodelClassgroupServiceImpl extends ServiceImpl<MetamodelClassgroupDaoImpl, MetamodelClassgroup> implements IMetamodelClassgroupService {
	
	@Override
	public List<MetamodelClassgroup> selectList(Map<String, Object> map){
		return dao.selectByMap(map);
	}
	
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<MetamodelClassgroup> selectMetamodelClassgroupList(MetamodelClassgroup entity,int pageNo,int pageSize){
		return dao.selectMetamodelClassgroupList(entity, pageNo, pageSize);
	}
	
	/**
	 * 根据Map查询模型组合列表
	 * @param map<String,Object>(classgroup_id-组合ID，class_id-模型类ID，relationship_code-组合关系代码，
	 * relationship_name-组合关系名称，
	 * classgrouped_id-被组合模型类ID，describes-描述，create_date-创建时间),map不为空
	 * @return
	 */
	@Override
	public List<MetamodelClassgroup> selectMetamodelClassgroupListByMap(Map<String, Object> map){
		return dao.selectMetamodelClassgroupListByMap(map);
	}
	/**
	 * 根据ID删除组合关系
	 * @param id
	 * @return
	 */
	@Override
	public JSONObject delMetamodelClassgroupById(String id){
		return dao.delMetamodelClassgroupById(id);
	}
}
