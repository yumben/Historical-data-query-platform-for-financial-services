package cn.com.infohold.dao;

import java.util.List;
import java.util.Map;

import cn.com.infohold.core.dao.IDao;
import cn.com.infohold.entity.VMetadataProperty;

public interface IVMetadataPropertyDao extends IDao<VMetadataProperty> {

    List<Map<String, Object>> selectProperty(Map<String, Object> map, boolean isSelectChildren, boolean isSelectParent);

    List<Map<String, Object>> selectPropertyByResource(Map<String, Object> map, boolean isSelectChildren, boolean isSelectParent, String token);

    public List<VMetadataProperty> selectPropertyByIds(List<String> ids, boolean isSelectChildren,
                boolean isSelectParent);
}
