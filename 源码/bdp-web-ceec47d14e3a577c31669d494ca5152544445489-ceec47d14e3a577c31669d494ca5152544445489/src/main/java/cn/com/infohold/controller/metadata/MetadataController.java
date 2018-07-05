package cn.com.infohold.controller.metadata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.entity.QueryDictData;
import cn.com.infohold.entity.VMetadataProperty;
import cn.com.infohold.service.IMetadataDictTypeService;
import cn.com.infohold.service.IMetadataService;
import cn.com.infohold.service.IServiceUrlService;
import cn.com.infohold.service.IVMetadataPropertyService;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/metadata")
public class MetadataController {
	
	private static String dict_id_code = "dict_id";
	private static String parent_metadata_code = "parent_metadata";
	@Autowired
	IMetadataService metadataServiceImpl;
	@Autowired
	IServiceUrlService serviceUrlServiceImpl;
	@Autowired
	IMetadataDictTypeService metadataDictTypeServiceImpl;
	@Autowired
	IVMetadataPropertyService vMetadataPropertyServiceImpl;

	@RequestMapping("/add")
	@ResponseBody
	public JSONObject add(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("code", "suc");
			jsonObject.put("msg", "增加成功！");
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/update")
	@ResponseBody
	public JSONObject update(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("code", "suc");
			jsonObject.put("msg", "修改成功！");
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/del")
	@ResponseBody
	public JSONObject del(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		try {

			jsonObject.put("code", "suc");
			jsonObject.put("msg", "增加成功！");
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/selectDataObj")
	@ResponseBody
	public JSONObject selectDataObj(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String token = request.getParameter("token");
		try {
			jsonObject = metadataServiceImpl.selectDataObj(token);
		} catch (Exception e) {
			jsonObject.put("code", "err");
			jsonObject.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return jsonObject;
	}

	@RequestMapping("/selectObjAndPropertie")
	@ResponseBody
	public JSONObject selectObjAndPropertie(HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		String tableName = request.getParameter("table");
		String[] tables = tableName.split(",");
		JSONArray list = new JSONArray();
		for (String table : tables) {
			JSONObject tempJsonObject = metadataServiceImpl.selectMetadataByIds(table);
			list.add(tempJsonObject);
		}
		jsonObject.put("list", list);
		return jsonObject;
	}

	@RequestMapping("/selectEChartType")
	@ResponseBody
	public JSONObject selectEChartType(HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject = metadataServiceImpl.selectEChartType();
		return jsonObject;
	}

	@RequestMapping("/selectEasyQueryType")
	@ResponseBody
	public JSONObject selectEasyQueryType(HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("metadataCode", "metaqueryType");
		multiValueMap.put("level", "c");
		String json = serviceUrlServiceImpl.post("bdp-metadata-service", "/metadata/selectMetadataJson", multiValueMap);
		jsonObject = JSONObject.parseObject(json);
		return jsonObject;
	}

	@RequestMapping("/selectMetadataJson")
	@ResponseBody
	public JSONObject selectMetadataJson(HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		String param = request.getParameter("param");
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String json = serviceUrlServiceImpl.post("bdp-metadata-service", "/metadata/selectMetadataJson/i",
				multiValueMap);
		jsonObject = JSONObject.parseObject(json);
		return jsonObject;
	}

	@RequestMapping("/selectMetadataPK")
	@ResponseBody
	public JSONObject selectMetadataPK(HttpServletRequest request) throws Exception {
		long s = System.currentTimeMillis();
		List<VMetadataProperty> mpList = new ArrayList<>();
		// 排除不含dict_id的mataid
		Collection<QueryDictData> qddList = null;
		String token = request.getParameter("token");
		JSONObject jsonObject = new JSONObject();
		String metadata_id = request.getParameter("metadata_id");
		String json = null;
		try {
			if (!StringUtil.isEmpty(metadata_id)) {
				List<Map<String, Object>> list = metadataServiceImpl.selectToken(token);

				long s2 = System.currentTimeMillis();
				log.debug("selectToken查询" + (s2 - s) + "ms");
				if (list.size() > 0) {
					String unit_id = list.get(0).get("unit_id").toString();
					String[] ids = metadata_id.split(",");
					List<String> idList = Arrays.asList(ids);
					// 获取属性
					mpList = vMetadataPropertyServiceImpl.selectPropertyByIds(idList, false, false);
					s2 = System.currentTimeMillis();
					log.debug("selectToken查询" + (s2 - s) + "ms");
					// 过滤和获取相应的信息
					qddList = geneQueryDictData(mpList);
					mpList.clear();
					// 获取字典数据
					Map<String, List<Map<String, Object>>> dicMap = metadataDictTypeServiceImpl.getDictData(qddList,
							unit_id);
					s2 = System.currentTimeMillis();
					log.debug("getDictData查询" + (s2 - s) + "ms");
					// 拼装
					Map<String, List<QueryDictData>> qddMap = qddList.parallelStream()
							.collect(Collectors.groupingBy(qd -> qd.getMataId()));

					s2 = System.currentTimeMillis();
					log.debug("groupingBy查询" + (s2 - s) + "ms");
					for (String id : ids) {
						QueryDictData dicData = qddMap.get(id).get(0);
						String dicID = dicData.getDict_id();
						List<Map<String, Object>> list1 = dicMap.get(dicID);
						jsonObject.put(id, list1);
					}

					s2 = System.currentTimeMillis();
					log.debug("for查询" + (s2 - s) + "ms");
				}
			}
		} catch (Exception ex) {
			log.error("ex={}", ex);
			log.error("json={}", json);
			log.error("jsonObject={}", jsonObject);
			throw ex;
		}
		return jsonObject;
	}
	
	 public Collection<QueryDictData> geneQueryDictData(List<VMetadataProperty> vpList) {
	        Collection<QueryDictData> ret = new ArrayList<>();
	        Map<String, QueryDictData> qdMap = new HashMap<>();
	        //对应的ids
	        for (VMetadataProperty mp : vpList) {
	            String mid = mp.getMetadataId();
	            if (dict_id_code.equals(mp.getPropertyCode())) {
	                //初始化
	                QueryDictData qdd = qdMap.get(mid);
	                if (qdd == null) {
	                    qdd = new QueryDictData();
	                    qdd.setMataId(mid);
	                    qdMap.put(mid, qdd);
	                    qdd = qdMap.get(mid);
	                }
	                qdd.setDict_id(mp.getPropertyValue());
	            }
	        }
	        //获取相应属性
	        for (Map.Entry<String, QueryDictData> qd : qdMap.entrySet()) {
	            String mid = qd.getKey();
	            QueryDictData qdd = qd.getValue();
	            for (VMetadataProperty mp : vpList) {
	                if (parent_metadata_code.equals(mp.getPropertyCode())
	                            && mid.equals(mp.getMetadataId())) {
	                    qdd.setParent_metadata(mp.getPropertyValue());
	                }
	            }
	        }
	        ret = qdMap.values();
	        return ret;
	    }


	// selectRelationMetadataJson
	@RequestMapping("/selectRelationMetadataJson")
	@ResponseBody
	public JSONObject selectRelationMetadataJson(HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		String param = request.getParameter("param");
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("param", param);
		String json = serviceUrlServiceImpl.post("bdp-metadata-service", "/metadata/selectRelationMetadataJson",
				multiValueMap);
		jsonObject = JSONObject.parseObject(json);
		return jsonObject;
	}

}
