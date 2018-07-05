package cn.com.infohold.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.entity.MetadataProperty;

public interface IMetadataDao extends IDao<Metadata> {

	/**
	 * 指标血统
	 * @param metadataId
	 * @return
	 */
	//JSONObject selectJson(String metadataId);
	
	/**
	 * 元数据血统
	 * @param metadataId
	 * @return
	 */
	//JSONObject getMetaData(String metadataId);
	/**
	 * 查询菜单
	 * @param entity 菜单实体
	 * @param pageNo 当前页码
	 * @param pageSize 每页大小
	 * @return
	 * @throws Exception
	 */
	List<Metadata> selectMetadataList(Metadata entity,int pageNo,int pageSize);
	List<Metadata> selectMetadataList(Metadata entity);
	/**
	 * 添加元数据
	 * @param json
	 * @return
	 */
	JSONObject addMetadataList(String json);
	
	List<Metadata> queryMetaDataByparentId(String parentId);
	List<Metadata> queryMetadataVOByParentIdAndCode(String parentId,String code,String metadataId);
	/**
	 * 查询元数据json
	 * @param metadataId-元数据ID
	 * @return
	 */
	JSONObject selectMetadataJson(String metadataId);
	/**
	 * 根据元数据ID查询元数据code
	 * @param metadataId
	 * @return
	 */
	Metadata selectMetadataCodeById(String metadataId);
	
	/**
	 * 查询元数据json
	 * @param metadataId-元数据ID,level：p-查上级，i-查本身，c-查子集，a-上级、本身、子集
	 * @return
	 */
	JSONObject selectMetadata(Map<String,Object>map,String level);
	
	/**
	 * 根据json修改元数据
	 * @param json
	 * @return
	 */
	JSONObject updateMetadataJson(String json);
	/**
	 * 根据元数据ID删除元数据,存在子集或者关联应用则不删除
	 * @param metadataId
	 * @return
	 */
	JSONObject deleteMetadata(String metadataId);
	
	/**
	 * 根据父Id递归查询
	 * @param parentId
	 * @param metaIdList
	 * @return
	 */
	List<String> queryMetaDataByparentId(String parentId,List<String> metaIdList);
	
	/**
	 * 根据父级ID查询元数据列表
	 * @param parentId
	 * @return
	 */
	JSONArray selectMetadataListByParentId(String parentId);
	
	JSONObject updateSubJsonObject(JSONArray childJsonObject,List<Metadata> metaIdList,List<MetadataProperty> updataMetadataPropertyList,String time);
	
	JSONArray selectMetadataJsonByCatalogType(String CatalogType);
	
	/**
	 * 根据父ID和模型类型查询元数据
	 * @param parentId
	 * @param classType
	 * @return
	 */
	List<Metadata> queryMetaDataByCatalogParentId(String parentId, String classType);
	
	/**
	 * 根据父ID，模型ID，模型类型查询元数据
	 * @param parentId
	 * @param classId
	 * @param classType
	 * @return
	 */
	List<Metadata> queryMetaDataByParentIdAndClassId(String parentId,String classId, String classType);
	
	/**
	 * 根据父ID查询元数据，不包含当前元数据
	 * @param parentId
	 * @param metadataId
	 * @return
	 */
	List<Metadata> queryMetaDataByParentIdAndNotId(String parentId,String metadataId);
	
	/**
	 * 根据父ID查询
	 * @param parentId
	 * @return
	 */
	List<Metadata> queryMetaDataByParentId(String parentId);
	
	/**
	 * 通过子集查询父级
	 * @param list
	 * @return
	 */
	List<Metadata> queryParentByChild(List<Metadata> list);
	/**
	 * 根据元数据ID查询元数据，包括模型名和路径
	 * @param metadataId
	 * @return
	 */
	Metadata queryMetadataById(String metadataId);
	
	/**
	 * 
	 * @param list
	 * @param metadataId
	 * @return
	 */
	Metadata queryMetadataVO(List list, String metadataId);
	
	/**
	 * 根据元数据code或者元数据名查询元数据列表
	 * @param codeOrName
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	JSONObject queryMetaDataListByCodeOrName(String codeOrName,int pageNo,int pageSize);
	
	/**
	 * //查询元数据关联关联信息
	 * @param metadataId
	 * @return
	 */
	JSONObject queryMetadataRelationshipBymetadataId(String metadataId,int pageNo,int pageSize);
	
	List<Metadata> queryMetaDataByInMetadataIdStr(String metadataRelationIdStr);
	
	/**
	 * 根据classId查询元数据
	 * @param classId
	 * @return
	 */
	List<Metadata> queryMataByclassId(String classId);
	
	List<Metadata> queryMetadataByCatalogBy(String catalogId,
			String classId,String metadatas);
	/**
	 * 根据元数据Id和relationId和classId关联metadata、 metamodel_relation 、metadata_relation查询 元数据
	 * @param classId
	 * @param relationId
	 * @param metadataId
	 * @return
	 */
	JSONObject queryRelationAndMetaDataByClassId(String classId,
			String relationId,String metadataId,int pageNo,int pageSize);
	
	
	List<Metadata> queryMetadataByCatalogAndCode(String catalogId,
			String code,String metadataId);
	
	/**
	 * 根据元数据IDlist删除当前元数据及其下级元数据、及属性、关联关系，如有被关联关系，不删除
	 * @param idlist
	 * @return
	 */
	JSONObject deleteMetadataByIdList(JSONArray idlist,String flag);
	/**
	 * 根据元数据ID查询上下文路径
	 * @param metadataId
	 * @param listMap
	 */
	void selectContextById(String metadataId,List<Map<String, String>> listMap);
	
	/**
	 * 根据元模型类ID、元数据ID、父级元数据ID查询元数据是那种类型的元数据（库或者字段）
	 * @param classId
	 * @return Map(dbFlag-元数据类型(db-数据库，filed-字段),dictionaryVOList-下拉列表(数据库驱动列表或者字段类型下拉列表))
	 */
	Map<Object, Object> checkMetadataTypeByClassId(String classId,String metadataId,String parentId);
	
	/**
	 * 根據map刪除元數據
	 * @param map
	 * @return
	 */
	JSONObject deleteMetadata(Map<String, Object> map);
	
	/**
	 * 根据元数据ID查询元数据
	 * @param metadataId
	 * @return
	 */
	JSONObject selectMetadataById(String metadataId);
	JSONObject selectMetadataByMap(Map<String, Object> map, String level);
	
	
	
	/**
	 * 查询元数据map
	 * @param param-元数据ID字符串，英文都好隔开
	 * @return
	 */
	JSONArray selectRelationMetadataJson(String param);
}
