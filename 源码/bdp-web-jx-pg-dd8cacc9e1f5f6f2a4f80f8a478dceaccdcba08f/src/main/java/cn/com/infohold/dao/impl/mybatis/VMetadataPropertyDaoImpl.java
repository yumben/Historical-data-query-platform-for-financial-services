package cn.com.infohold.dao.impl.mybatis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import cn.com.infohold.core.dao.impl.MyBatisDaoImpl;
import cn.com.infohold.dao.IVMetadataPropertyDao;
import cn.com.infohold.entity.VMetadataProperty;
import cn.com.infohold.mapper.VMetadataPropertyMapper;
import cn.com.infohold.tools.util.StringUtil;

@Service
public class VMetadataPropertyDaoImpl extends MyBatisDaoImpl<VMetadataPropertyMapper, VMetadataProperty>
            implements IVMetadataPropertyDao {

    @Autowired
    VMetadataPropertyMapper vMetadataPropertyMapper;
    @Override
    public List<Map<String, Object>> selectProperty(Map<String, Object> map, boolean isSelectChildren,
                boolean isSelectParent) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        List<VMetadataProperty> vMetadataPropertyList = vMetadataPropertyMapper.selectByMap(map);
        if (vMetadataPropertyList != null) {
            Map<String, Object> resultMap = null;
            Collections.sort(vMetadataPropertyList, new Comparator<VMetadataProperty>() {
                @Override
                public int compare(VMetadataProperty o1, VMetadataProperty o2) {
                    return o1.getMetadataId().compareTo(o2.getMetadataId());//升序
                }
            });
            for (VMetadataProperty vMetadataProperty : vMetadataPropertyList) {
                if (resultMap == null) {
                    resultMap = new HashMap<String, Object>();
                    resultMap.put("metadata_id", vMetadataProperty.getMetadataId());
                    resultMap.put("metadata_code", vMetadataProperty.getMetadataCode());
                    resultMap.put("metadata_name", vMetadataProperty.getMetadataName());
                    resultMap.put("parent_metadata", vMetadataProperty.getParentMetadata());
                    resultMap.put(vMetadataProperty.getPropertyCode(), vMetadataProperty.getPropertyValue());
                } else {
                    if (resultMap.get("metadata_id").equals(vMetadataProperty.getMetadataId())) {
                        resultMap.put(vMetadataProperty.getPropertyCode(), vMetadataProperty.getPropertyValue());
                    } else {
                        Map<String, Object> temp = new HashMap<String, Object>();
                        temp.putAll(resultMap);
                        result.add(temp);
                        resultMap.clear();
                        resultMap.put("metadata_id", vMetadataProperty.getMetadataId());
                        resultMap.put("metadata_code", vMetadataProperty.getMetadataCode());
                        resultMap.put("metadata_name", vMetadataProperty.getMetadataName());
                        resultMap.put("parent_metadata", vMetadataProperty.getParentMetadata());
                        resultMap.put(vMetadataProperty.getPropertyCode(), vMetadataProperty.getPropertyValue());
                    }
                }
            }
            if (resultMap != null) {
                result.add(resultMap);
                for (Map<String, Object> map2 : result) {
                    if (isSelectChildren) {
                        Map<String, Object> columnMap = new HashMap<String, Object>();
                        columnMap.put("parent_metadata", map2.get("metadata_id"));
                        List<Map<String, Object>> children = selectProperty(columnMap, false, false);
                        if (children.size() > 0) {
                            map2.put("children", children);
                        }
                    }
                    if (isSelectParent) {
                        Map<String, Object> columnMap = new HashMap<String, Object>();
                        if (map2.get("parent_metadata") != null) {
                            columnMap.put("metadata_id", map2.get("parent_metadata"));
                            List<Map<String, Object>> parent = selectProperty(columnMap, false, false);
                            if (parent.size() > 0) {
                                map2.put("parent", parent.get(0));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectPropertyByResource(Map<String, Object> map, boolean isSelectChildren,
                boolean isSelectParent, String token) {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        List<VMetadataProperty> vMetadataPropertyList = vMetadataPropertyMapper.selectByMap(map);
        if (vMetadataPropertyList != null) {
            Map<String, Object> resultMap = null;
            Collections.sort(vMetadataPropertyList, new Comparator<VMetadataProperty>() {
                @Override
                public int compare(VMetadataProperty o1, VMetadataProperty o2) {
                    return o1.getMetadataId().compareTo(o2.getMetadataId());//升序
                }
            });
            for (VMetadataProperty vMetadataProperty : vMetadataPropertyList) {
                if (resultMap == null) {
                    resultMap = new HashMap<String, Object>();
                    resultMap.put("metadata_id", vMetadataProperty.getMetadataId());
                    resultMap.put("metadata_code", vMetadataProperty.getMetadataCode());
                    resultMap.put("metadata_name", vMetadataProperty.getMetadataName());
                    resultMap.put("parent_metadata", vMetadataProperty.getParentMetadata());
                    resultMap.put(vMetadataProperty.getPropertyCode(), vMetadataProperty.getPropertyValue());
                } else {
                    if (resultMap.get("metadata_id").equals(vMetadataProperty.getMetadataId())) {
                        resultMap.put(vMetadataProperty.getPropertyCode(), vMetadataProperty.getPropertyValue());
                    } else {
                        Map<String, Object> temp = new HashMap<String, Object>();
                        temp.putAll(resultMap);
                        result.add(temp);
                        resultMap.clear();
                        resultMap.put("metadata_id", vMetadataProperty.getMetadataId());
                        resultMap.put("metadata_code", vMetadataProperty.getMetadataCode());
                        resultMap.put("metadata_name", vMetadataProperty.getMetadataName());
                        resultMap.put("parent_metadata", vMetadataProperty.getParentMetadata());
                        resultMap.put(vMetadataProperty.getPropertyCode(), vMetadataProperty.getPropertyValue());
                    }
                }
            }
            if (resultMap != null) {
                result.add(resultMap);
                List<String> objCodes = new ArrayList<String>();
                if (StringUtil.isNotEmpty(token)) {
                    objCodes = vMetadataPropertyMapper.getAuthData(token);//获取权限能查的对象code
                }
                for (Map<String, Object> map2 : result) {
                    if (isSelectChildren) {
                        Map<String, Object> columnMap = new HashMap<String, Object>();
                        columnMap.put("parent_metadata", map2.get("metadata_id"));
                        for (String string : objCodes) {
                            columnMap.put("property_value", string);
                        }
                        List<Map<String, Object>> children = selectProperty(columnMap, false, false);
                        if (children.size() > 0) {
                            map2.put("children", children);
                        }
                    }
                    if (isSelectParent) {
                        Map<String, Object> columnMap = new HashMap<String, Object>();
                        if (map2.get("parent_metadata") != null) {
                            columnMap.put("metadata_id", map2.get("parent_metadata"));
                            for (String string : objCodes) {
                                columnMap.put("property_value", string);
                            }
                            List<Map<String, Object>> parent = selectProperty(columnMap, false, false);
                            if (parent.size() > 0) {
                                map2.put("parent", parent.get(0));
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<VMetadataProperty> selectPropertyByIds(List<String> ids, boolean isSelectChildren,
                boolean isSelectParent) {
        EntityWrapper<VMetadataProperty> wrapper = new EntityWrapper<VMetadataProperty>();
        wrapper.in("metadata_id", ids);
        List<VMetadataProperty> ret = vMetadataPropertyMapper.selectList(wrapper);
        return ret;
    }
}
