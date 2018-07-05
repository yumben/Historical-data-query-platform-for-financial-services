package cn.com.infohold.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.bean.SqlBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.core.service.IService;
import cn.com.infohold.entity.Metadata;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
public interface IMetadataService extends IService<Metadata> {
	/**
	 * 添加元数据
	 * 
	 * @param json
	 * @return
	 */
	JSONObject addMetadataList(String json);

	JSONObject addMetadataJSon(String json);

	/**
	 * 根据父级ID、元数据代码、元数据ID查询元数据
	 * 
	 * @param parentId
	 * @param code
	 * @param metadataId
	 * @return
	 */
	List<Metadata> queryMetadataVOByParentIdAndCode(String parentId, String code, String metadataId);

	/**
	 * 根据父级ID查询元数据
	 * 
	 * @param parentId
	 * @return
	 */
	List<Metadata> queryMetaDataByparentId(String parentId);

	/**
	 * 根据父ID和模型类型查询元数据
	 * 
	 * @param parentId
	 * @param classType
	 * @return
	 */
	List<Metadata> queryMetaDataByCatalogParentId(String parentId, String classType);

	/**
	 * 根据父ID，模型ID，模型类型查询元数据
	 * 
	 * @param parentId
	 * @param classId
	 * @param classType
	 * @return
	 */
	List<Metadata> queryMetaDataByParentIdAndClassId(String parentId, String classId, String classType);

	/**
	 * 根据父ID查询元数据，不包含当前元数据
	 * 
	 * @param parentId
	 * @param metadataId
	 * @return
	 */
	List<Metadata> queryMetaDataByParentIdAndNotId(String parentId, String metadataId);

	
	/**
	 * 根据元数据ID查询元数据code
	 * 
	 * @param metadataId
	 * @return
	 */
	Metadata selectMetadataCodeById(String metadataId);
	
	/**
	 * 根据元数据ID查询元数据，包括模型名和路径
	 * 
	 * @param metadataId
	 * @return
	 */
	Metadata queryMetadataById(String metadataId);

	/**
	 * 根据元数据ID获取json
	 * 
	 * @param metadataId
	 * @return
	 */
	JSONObject selectMetadataJson(String metadataId);

	/**
	 * 根据目录类型查询元数据json
	 * 
	 * @param CatalogType
	 * @return
	 */
	JSONArray selectMetadataJsonByCatalogType(String CatalogType);

	/**
	 * 根据元数据IDlist删除当前元数据及其下级元数据、及属性、关联关系，如有被关联关系，不删除
	 * 
	 * @param idlist
	 * @return
	 */
	JSONObject deleteMetadataByIdList(JSONArray idlist, String flag);

	/**
	 * 根据元数据ID删除元数据,存在子集或者关联应用则不删除
	 * 
	 * @param metadataId
	 * @return
	 */
	JSONObject deleteMetadata(String metadataId);

	/**
	 * 根据元数据json修改元数据
	 * 
	 * @param json
	 * @return
	 */
	JSONObject updateMetadataJson(String json);

	/**
	 * 根据父级ID查询元数据列表
	 * 
	 * @param parentId
	 * @return
	 */
	JSONArray selectMetadataListByParentId(String parentId);

	/**
	 * 根据元数据对象查询元数据
	 * 
	 * @param entity
	 * @return
	 */
	List<Metadata> selectMetadataList(Metadata entity);

	/**
	 * 根据元数据code或者元数据名查询元数据列表
	 * 
	 * @param codeOrName
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	JSONObject queryMetaDataListByCodeOrName(String codeOrName, int pageNo, int pageSize);

	/**
	 * //查询元数据关联关联信息
	 * 
	 * @param metadataId
	 * @return
	 */
	JSONObject queryMetadataRelationshipBymetadataId(String metadataId, int pageNo, int pageSize);

	List<Metadata> queryMetadataByCatalogBy(String catalogId, String classId, String metadatas);

	/**
	 * 根据元数据Id和relationId和classId关联metadata、 metamodel_relation
	 * 、metadata_relation查询 元数据
	 * 
	 * @param classId
	 * @param relationId
	 * @param metadataId
	 * @return
	 */
	JSONObject queryRelationAndMetaDataByClassId(String classId, String relationId, String metadataId, int pageNo,
			int pageSize);

	/**
	 * 根据classId查询元数据
	 * 
	 * @param classId-元模型ID
	 * @return
	 */
	List<Metadata> queryMataByclassId(String classId);

	/**
	 * 根据元模型类ID、元数据ID、父级元数据ID查询元数据是那种类型的元数据（库或者字段）
	 * 
	 * @param classId
	 * @return Map(dbFlag-元数据类型(db-数据库，filed-字段),dictionaryVOList-下拉列表(
	 *         数据库驱动列表或者字段类型下拉列表))
	 */
	Map<Object, Object> checkMetadataTypeByClassId(String classId, String metadataId, String parentId);

	/**
	 * 根據map刪除元數據
	 * 
	 * @param map
	 * @return
	 */
	JSONObject deleteMetadata(Map<String, Object> map);

	/**
	 * 根据元数据code获取元數據列表
	 * 
	 * @param Map("metadata_code":""),level(p-查上级，i-查本身，c-查子集,a-查上级、本身、子集)
	 * @return
	 */
	JSONObject selectMetadata(Map<String, Object> map, String level);

	List<Map<Object, Object>> selectMetadataMap(Map<String, Object> map, String level) throws IOException;

	JSONArray selectRelationMetadataJson(String idList);

	/**
	 * 根据元数据表ids查他们的外键信息
	 * @param idList
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	RetBean selectRelationInfoJson(String idList,String db_code) throws UnsupportedEncodingException, IOException;

	RetBean selectMetadataBySqlBean(SqlBean sqlBean) throws UnsupportedEncodingException, IOException;

	RetBean selectMetadataByids(SqlBean sqlBean) throws UnsupportedEncodingException, IOException;
	
}
