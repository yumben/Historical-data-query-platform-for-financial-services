package cn.com.infohold.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface IMetadataService {
	JSONObject selectMetadataByCode(String metadataCode) throws Exception;

	JSONObject selectDataObj(String token) throws Exception;

	JSONObject selectEChartType() throws Exception;

	JSONObject selectMetadataByIds(String metadata_id) throws Exception;

	List<Map<String, Object>> selectToken(String token) throws Exception;
}
