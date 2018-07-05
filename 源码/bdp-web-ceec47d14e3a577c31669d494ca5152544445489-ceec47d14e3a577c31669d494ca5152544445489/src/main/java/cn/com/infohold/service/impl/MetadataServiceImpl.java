package cn.com.infohold.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.mapper.ServiceUrlMapper;
import cn.com.infohold.service.IMetadataService;
import cn.com.infohold.service.IServiceUrlService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MetadataServiceImpl implements IMetadataService {
	@Autowired
	IServiceUrlService serviceUrlService;
	@Autowired
	private ServiceUrlMapper serviceUrlMapper;

	@Cacheable("selectMetadataByMetadataCode")
	@Override
	public JSONObject selectMetadataByCode(String metadataCode) throws Exception {
		JSONObject paramJsonObject = new JSONObject();
		Map<String, String> params = new HashMap<String, String>();
		paramJsonObject.put("metadata_code", metadataCode);
		params.put("json", paramJsonObject.toJSONString());
		String json = serviceUrlService.post("bdp-metadata-service", "/metadata/selectProperty/c", params);
		JSONObject jsonObject = JSONObject.parseObject(json);
		return jsonObject;
	}

	@Cacheable("selectDataObj")
	@Override
	public JSONObject selectDataObj(String token) throws Exception {
		JSONObject paramJsonObject = new JSONObject();
		Map<String, String> params = new HashMap<String, String>();
		paramJsonObject.put("class_id", "ace87e76-95d3-42f2-b723-f27494b44007");
		params.put("json", paramJsonObject.toJSONString());
		String json = serviceUrlService.post("bdp-metadata-service", "/metadata/selectPropertyByResource", params);
		JSONObject jsonObject = JSONObject.parseObject(json);
		return jsonObject;
	}

	@Cacheable("selectEChartType")
	@Override
	public JSONObject selectEChartType() throws Exception {
		JSONObject paramJsonObject = new JSONObject();
		Map<String, String> params = new HashMap<String, String>();
		paramJsonObject.put("class_id", "180");
		params.put("json", paramJsonObject.toJSONString());
		String json = serviceUrlService.post("bdp-metadata-service", "/metadata/selectProperty/i", params);
		JSONObject jsonObject = JSONObject.parseObject(json);
		return jsonObject;
	}

	@Override
	public JSONObject selectMetadataByIds(String metadata_id) throws Exception {
		JSONObject jsonObject = new JSONObject();
		JSONObject paramJsonObject = new JSONObject();
		Map<String, String> params = new HashMap<String, String>();
		paramJsonObject.put("metadata_id", metadata_id);
		params.put("json", paramJsonObject.toJSONString());
		String json = serviceUrlService.post("bdp-metadata-service", "/metadata/selectProperty/c", params);
		JSONObject jsonObject11 = JSONObject.parseObject(json);

		JSONArray jsonArray2 = jsonObject11.getJSONArray("list");
		if (jsonArray2.size() > 0) {
			JSONObject jsonObject1 = (JSONObject) jsonArray2.get(0);
			JSONArray jsonArray = jsonObject1.getJSONArray("children");
			JSONArray jsonArray1 = new JSONArray();
			for (Object object : jsonArray) {
				JSONObject obj = (JSONObject) object;
				JSONObject jsonObject2 = new JSONObject();
				jsonObject2.put("column_name", obj.getString("column_comment"));
				jsonObject2.put("column_code", obj.getString("column_name"));
				obj.remove("column_name");
				obj.remove("column_comment");
				jsonObject2.putAll(obj);
				jsonArray1.add(jsonObject2);
			}
			jsonObject.put("columns", jsonArray1);
			jsonObject.put("table_name", jsonObject1.getString("table_comment"));
			jsonObject.put("table_code", jsonObject1.getString("table_name"));
			jsonObject1.remove("children");
			jsonObject1.remove("table_name");
			jsonObject1.remove("table_comment");
			jsonObject.putAll(jsonObject1);
		}
		return jsonObject;

	}



	@Cacheable("selectToken")
	@Override
	public List<Map<String, Object>> selectToken(String token) throws Exception {
		return serviceUrlMapper.selectToken(token);
	}

}
