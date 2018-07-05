package cn.com.infohold.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import cn.com.infohold.basic.util.jdbc.BasicJdbcUtil;
import cn.com.infohold.entity.QueryDictData;
import cn.com.infohold.mapper.MetadataDictTypeMapper;
import cn.com.infohold.service.IMetadataDictTypeService;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "MetadataDictTypeServiceImpl")
@Service
public class MetadataDictTypeServiceImpl implements IMetadataDictTypeService {
	@Autowired
	MetadataServiceImpl metadataServiceImpl;
	@Autowired
	private DataSource dataSource;
	@Autowired
	MetadataDictTypeMapper metadataDictTypeMapper;

	// 生成枚举类
	@Cacheable("geneEnumMap")
	public Map<String, List<Map<String, Object>>> geneEnumMap(List<Map<String, Object>> dictLlist) throws Exception {
		Map<String, List<Map<String, Object>>> enumMap = new HashMap<>();
		if (dictLlist == null || dictLlist.isEmpty()) {
			return enumMap;
		}
		List<String> ids = new ArrayList<>();
		for (Map<String, Object> m : dictLlist) {
			if ("enum".equals(m.get("dicti_type"))) {
				ids.add(m.get("id").toString());
			}
		}
		if (ids.size() > 0) {
			List<Map<String, Object>> enumResult = metadataDictTypeMapper.selectParameterListByIds(ids);
			// 按 字典id分组
			for (Map<String, Object> map : enumResult) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.putAll(map);
				if (enumMap.containsKey(Objects.toString(map.get("para_type")))) {
					enumMap.get(Objects.toString(map.get("para_type"))).add(tempMap);
				} else {
					List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
					tempList.add(tempMap);
					enumMap.put(Objects.toString(map.get("para_type")), tempList);
				}
			}
		}
		return enumMap;
	}

	// 字典集
	@Cacheable("geneDictLlist")
	public List<Map<String, Object>> geneDictLlist(Collection<QueryDictData> qddList) throws SQLException {
		List<String> ids = new ArrayList<>();
		for (QueryDictData qdd : qddList) {
			ids.add(qdd.getDict_id());
		}
		List<Map<String, Object>> dictLlist = metadataDictTypeMapper.selectMetadataDictTypeByIds(ids);
		return dictLlist;
	}

	// 生成外键
	public Map<String, List<Map<String, Object>>> genePkMap(List<Map<String, Object>> dictLlist) throws Exception {
		Map<String, List<Map<String, Object>>> pkMap = new HashMap<>();
		if (dictLlist == null || dictLlist.isEmpty()) {
			return pkMap;
		}
		List<Map<String, Object>> pkList = dictLlist.parallelStream()
				.filter(dict -> "pk".equals(dict.get("dicti_type"))).collect(Collectors.toList());
		if (pkList == null || pkList.isEmpty()) {
			return pkMap;
		}
		Map<String, List<Map<String, Object>>> pkMapAll = genePkMapAll();
		for (Map<String, Object> map : pkList) {
			if (pkMapAll.containsKey(map.get("id").toString())) {
				pkMap.put(map.get("id").toString(), pkMapAll.get(map.get("id").toString()));
			}
		}
		return pkMap;
	}

	@Cacheable("genePkMapAll")
	private Map<String, List<Map<String, Object>>> genePkMapAll() throws Exception {
		Map<String, List<Map<String, Object>>> pkMap = new HashMap<String, List<Map<String, Object>>>();
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			List<Map<String, Object>> pkList = metadataDictTypeMapper.selectMetadataDictType("pk");
			if (pkList == null || pkList.isEmpty()) {
				return pkMap;
			}
			// 生成sql语句
			List<String> pkSqlList = new ArrayList<>(pkList.size());
			int index = 0;
			for (Map<String, Object> tmpMap : pkList) {
				Object dicti_data = tmpMap.get("dicti_data");
				String id = tmpMap.get("id").toString();
				String pksql = Objects.toString(dicti_data, "");
				if (StringUtils.isEmpty(pksql)) {
					continue;
				}
				pksql = StringUtils.trim(pksql);
				pksql = "select * from ( select '" + id + "'::text as para_type , "
						+ pksql.substring("select".length(), pksql.length()) + " ) as lfh" + index;
				pkSqlList.add(pksql);
				index++;
			}
			String pkSql = StringUtils.join(pkSqlList, " union all ");

			List<Map<String, Object>> pkResult = BasicJdbcUtil.getInstance().select(conn, pkSql);
			for (Map<String, Object> map : pkResult) {
				Map<String, Object> tempMap = new HashMap<String, Object>();
				tempMap.putAll(map);
				if (pkMap.containsKey(Objects.toString(map.get("para_type")))) {
					pkMap.get(Objects.toString(map.get("para_type"))).add(tempMap);
				} else {
					List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
					tempList.add(tempMap);
					pkMap.put(Objects.toString(map.get("para_type")), tempList);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
		return pkMap;
	}

	public Map<String, List<Map<String, Object>>> geneTree(List<Map<String, Object>> dictLlist,
			Map<String, List<Map<String, Object>>> pkMap, String unit) {
		Map<String, List<Map<String, Object>>> retMap = new HashMap<>(pkMap.size());
		Map<String, List<Map<String, Object>>> dicMap = dictLlist.parallelStream()
				.collect(Collectors.groupingBy(map -> Objects.toString(map.get("id"))));
		for (Map.Entry<String, List<Map<String, Object>>> pk : pkMap.entrySet()) {
			String dicID = (String) pk.getKey();
			List<Map<String, Object>> resultList = pk.getValue();
			List<Map<String, Object>> list = dicMap.get(dicID);
			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for (Map<String, Object> objMap : list) {
				if (objMap.containsKey("is_tree") && "1".equals(objMap.get("is_tree").toString())) {
					Map<String, List<Map<String, Object>>> childrensMap = new HashMap<String, List<Map<String, Object>>>();
					for (Map<String, Object> map : resultList) {
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
					if (objMap.containsKey("is_unit_filter") && "1".equals(objMap.get("is_unit_filter").toString())) {
						result = intiTree(resultList, childrensMap, unit, "parent", "value", "childrens");
					} else {
						result = intiTree(resultList, childrensMap, "", "parent", "value", "childrens");
					}
				} else {
					result = resultList;
				}
			}
			retMap.put(dicID, result);
		}
		return retMap;
	}

	@Cacheable("intiTree")
	public static List<Map<String, Object>> intiTree(List<Map<String, Object>> resultList,
			Map<String, List<Map<String, Object>>> childrens, String parentValue, String parentKey, String valueKey,
			String childrensKey) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : resultList) {
			if (StringUtil.isEmpty(parentValue)) {
				if (parentValue.equals(map.get(parentKey).toString())) {
					map.put("childrens", getTreeChinlds(childrens, map.get("value").toString()));
					result.add(map);
					break;
				}
			} else {
				if (parentValue.equals(map.get(valueKey).toString())) {
					map.put("childrens", getTreeChinlds(childrens, map.get("value").toString()));
					result.add(map);
					break;
				}
			}
		}
		return result;
	}

	public static List<Map<String, Object>> getTreeChinlds(Map<String, List<Map<String, Object>>> childrens,
			String parentValue) {
		List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
		if (childrens.containsKey(parentValue)) {
			if (childrens.get(parentValue) != null && childrens.get(parentValue).size() > 0) {
				for (Map<String, Object> map : childrens.get(parentValue)) {
					map.put("childrens", getTreeChinlds(childrens, map.get("value").toString()));
					tempList.add(map);
				}
			}
		}
		return tempList;
	}

	@Override
	public Map<String, List<Map<String, Object>>> getDictData(Collection<QueryDictData> qddList, String unit)
			throws Exception {
		long s = System.currentTimeMillis();
		Map<String, List<Map<String, Object>>> retMap = new HashMap<>(qddList.size());
		try {
			// 字典集
			List<Map<String, Object>> dictLlist = geneDictLlist(qddList);

			long s2 = System.currentTimeMillis();
			log.debug("queryByList查询" + (s2 - s) + "ms");
			// 类型是enum--枚举
			Map<String, List<Map<String, Object>>> enumMap = geneEnumMap(dictLlist);
			s2 = System.currentTimeMillis();
			log.debug("geneEnumMap查询" + (s2 - s) + "ms");
			// 类型是pk
			Map<String, List<Map<String, Object>>> pkMap = genePkMap(dictLlist);
			s2 = System.currentTimeMillis();
			log.debug("genePkMap查询" + (s2 - s) + "ms");
			// 对pk进行处理
			Map<String, List<Map<String, Object>>> treeMap = geneTree(dictLlist, pkMap, unit);
			s2 = System.currentTimeMillis();
			log.debug("geneTree查询" + (s2 - s) + "ms");
			retMap.putAll(enumMap);
			retMap.putAll(treeMap);
		} catch (Exception ex) {
			throw ex;
		}
		return retMap;
	}

}
