package cn.com.infohold.service;

import cn.com.infohold.entity.MetamodelClassgroup;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetamodelClassgroupService extends IService<MetamodelClassgroup> {
	/**
	 * 根据Map查询模型组合列表
	 * @param map<String,Object>(classgroup_id-组合ID，class_id-模型类ID，relationship_code-组合关系代码，
	 * relationship_name-组合关系名称，
	 * classgrouped_id-被组合模型类ID，describes-描述，create_date-创建时间)
	 * @return
	 */
	List<MetamodelClassgroup> selectList(Map<String, Object> map);
	
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	List<MetamodelClassgroup> selectMetamodelClassgroupList(MetamodelClassgroup entity,int pageNo,int pageSize);
	
	/**
	 * 根据Map查询模型组合列表
	 * @param map<String,Object>(classgroup_id-组合ID，class_id-模型类ID，relationship_code-组合关系代码，
	 * relationship_name-组合关系名称，
	 * classgrouped_id-被组合模型类ID，describes-描述，create_date-创建时间),map不为空
	 * @return
	 */
	List<MetamodelClassgroup> selectMetamodelClassgroupListByMap(Map<String, Object> map); 
	/**
	 * 根据ID删除组合关系
	 * @param id
	 * @return
	 */
	JSONObject delMetamodelClassgroupById(String id);
}
