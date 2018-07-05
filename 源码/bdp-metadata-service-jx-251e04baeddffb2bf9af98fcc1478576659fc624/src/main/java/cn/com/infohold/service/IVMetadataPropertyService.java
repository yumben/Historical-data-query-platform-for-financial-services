package cn.com.infohold.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.service.IService;
import cn.com.infohold.entity.VMetadataProperty;

public interface IVMetadataPropertyService extends IService<VMetadataProperty> {

    /**
     * 根据元数据代码（metadata_code）查询元数据属性信息及其所有子集属性信息
     *
     * @param metadata_code
     * @return json串
     */
    String selectPropertyByMetadataCode(String metadata_code);

    /**
     * 根据元数据代码（metadata_code）查询父级属性列表
     *
     * @param matedataCode
     * @return
     */
    List<Map<String, Object>> queryParentPropertyByMatedata(String matedataCode);

    /**
     * 根据元数据代码（matedataId）查询自身属性列表
     *
     * @param matedataId
     * @return {metadata_id:"",metadata_code:"",key1:"",key2:""} key为属性code
     */
    List<Map<String, Object>> queryPropertyByMatedataId(String matedataId);

    /**
     * 根据Map查询该模型类下的所有属性列表
     *
     * @param classId
     * @return
     */
    List<Map<String, Object>> selectPropertyByClassId(Map<String, Object> map);

    List<Map<String, Object>> selectProperty(Map<String, Object> map, boolean isSelectChildren, boolean isSelectParent);

    /**
     * 做权限查询对象表数据
     *
     * @param map
     * @param isSelectChildren
     * @param isSelectParent
     * @return
     */
    List<Map<String, Object>> selectPropertyByResource(Map<String, Object> map, boolean isSelectChildren, boolean isSelectParent, String token);

    public List<VMetadataProperty> selectPropertyByIds(List<String> ids, boolean isSelectChildren,
                boolean isSelectParent);

}
