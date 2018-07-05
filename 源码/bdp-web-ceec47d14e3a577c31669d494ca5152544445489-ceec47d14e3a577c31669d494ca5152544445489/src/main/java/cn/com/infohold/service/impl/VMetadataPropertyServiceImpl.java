package cn.com.infohold.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.core.service.impl.ServiceImpl;
import cn.com.infohold.dao.IVMetadataPropertyDao;
import cn.com.infohold.entity.VMetadataProperty;
import cn.com.infohold.service.IVMetadataPropertyService;

@Service
public class VMetadataPropertyServiceImpl extends ServiceImpl<IVMetadataPropertyDao, VMetadataProperty>
		implements IVMetadataPropertyService {

	@Override
	public String selectPropertyByMetadataCode(String metadata_code) {
		try {
			Map<String, Object> columnMap = new HashMap<String, Object>();
			columnMap.put("metadata_code", metadata_code);
			List<Map<String, Object>> list = selectProperty(columnMap, true, false);
			return BasicJsonUtil.getInstance().toJsonString(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "[]";
	}

	@Override
	public List<Map<String, Object>> selectPropertyByClassId(Map<String, Object> map) {
		List<Map<String, Object>> list = selectProperty(map, false, false);
		return list;
	}

	public List<Map<String, Object>> queryParentPropertyByMatedata(String matedataCode) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("metadata_code", matedataCode);
		List<Map<String, Object>> list = selectProperty(columnMap, false, false);
		return list;
	}

	public List<Map<String, Object>> queryPropertyByMatedataId(String matedataId) {
		Map<String, Object> columnMap = new HashMap<String, Object>();
		columnMap.put("metadata_id", matedataId);
		List<Map<String, Object>> list = selectProperty(columnMap, false, false);
		return list;
	}

	@Override
	public List<Map<String, Object>> selectProperty(Map<String, Object> map, boolean isSelectChildren,
			boolean isSelectParent) {
		return dao.selectProperty(map, isSelectChildren, isSelectParent);
	}

	@Override
	public List<Map<String, Object>> selectPropertyByResource(Map<String, Object> map, boolean isSelectChildren,
			boolean isSelectParent,String token) {
		return dao.selectPropertyByResource(map, isSelectChildren, isSelectParent,token);
	}
        
    @Override
    public List<VMetadataProperty> selectPropertyByIds(List<String> ids, boolean isSelectChildren, boolean isSelectParent) {
        return dao.selectPropertyByIds(ids, isSelectChildren, isSelectParent);
    }

}
