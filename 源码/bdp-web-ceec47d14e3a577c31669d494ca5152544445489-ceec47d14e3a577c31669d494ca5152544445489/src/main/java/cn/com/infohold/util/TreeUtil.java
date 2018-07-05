package cn.com.infohold.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeUtil {

	public static List<Map<String, Object>> intiTree(List<Map<String, Object>> resultList, String parentValue,
			String parentKey, String valueKey, String childrensKey) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : resultList) {
			if (parentValue.equals(map.get(parentKey).toString())) {
				if (map.containsKey(valueKey)) {
					List<Map<String, Object>> childrens = intiTree(resultList, map.get(valueKey).toString(), parentKey,
							valueKey, childrensKey);
					if (childrens != null && childrens.size() > 0) {
						map.put(childrensKey, childrens);
					}
				}
				result.add(map);
			}
		}
		return result;
	}
}
