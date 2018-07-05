package cn.com.infohold.service;

import java.util.List;
import java.util.Map;

public interface IMetamodelService {
	List<Map<String, Object>> getPackage(Map<String, Object> columnMap);
}
