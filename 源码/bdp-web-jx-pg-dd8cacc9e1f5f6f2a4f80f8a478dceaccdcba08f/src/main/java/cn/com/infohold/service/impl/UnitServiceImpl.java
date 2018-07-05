package cn.com.infohold.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.com.infohold.mapper.ServiceUrlMapper;
import cn.com.infohold.service.IUnitService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UnitServiceImpl implements IUnitService {

	@Autowired
	ServiceUrlMapper serviceUrlMapper;

	@Override
	public Map<String, Object> getDefineUnitList(String token) {
		List<Map<String, Object>> tokens = serviceUrlMapper.selectToken(token);
		List<Map<String, Object>> unitList = new ArrayList<Map<String, Object>>();
		if (tokens != null && tokens.size() > 0) {
			String unit_id = tokens.get(0).get("unit_id").toString();
			getDefineUnitList(unitList, unit_id);
		}
		List<String> unit_ids = new ArrayList<String>();
		StringBuffer unit_names = new StringBuffer();
		for (Map<String, Object> m : unitList) {
			unit_ids.add(m.get("value").toString());
			unit_names.append(m.get("label") + ",");
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("unit_ids", StringUtils.join(unit_ids, ","));
		result.put("unit_names", unit_names.substring(0, unit_names.length() - 1));
		return result;
	}

	@Cacheable("getDefineUnitList")
	private void getDefineUnitList(List<Map<String, Object>> unitList, String unit_id) {
		List<Map<String, Object>> list = getUnitList();
		for (Map<String, Object> m : list) {
			if (unit_id.equals(m.get("value"))) {
				unitList.add(m);
				Map<String, List<Map<String, Object>>> childrensMap = new HashMap<String, List<Map<String, Object>>>();
				for (Map<String, Object> map : list) {
					Map<String, Object> tempMap = new HashMap<String, Object>();
					tempMap.putAll(map);
					if (childrensMap.containsKey(Objects.toString(map.get("parent")))) {
						childrensMap.get(Objects.toString(map.get("parent"))).add(tempMap);
					} else {
						List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
						tempList.add(tempMap);
						childrensMap.put(Objects.toString(map.get("parent")), tempList);
					}
				}
				getChinlds(childrensMap, unitList, unit_id);
				break;
			}
		}
	}

	public void getChinlds(Map<String, List<Map<String, Object>>> maps, List<Map<String, Object>> result,
			String unitId) {
		List<Map<String, Object>> list = maps.get(unitId);
		if (list != null && list.size() > 0) {
			result.addAll(list);
			for (Map<String, Object> m : list) {
				if (m.get("value") != null) {
					getChinlds(maps, result, m.get("value").toString());
				}
			}
		}
	}

	@Cacheable("getDefineUnitList")
	@Override
	public List<Map<String, Object>> getUnitList() {
		return serviceUrlMapper.getUnitList();
	}

}
