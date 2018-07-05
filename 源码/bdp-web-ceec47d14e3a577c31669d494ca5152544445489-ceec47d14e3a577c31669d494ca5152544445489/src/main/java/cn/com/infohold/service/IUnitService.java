package cn.com.infohold.service;

import java.util.List;
import java.util.Map;

public interface IUnitService {

	Map<String, Object> getDefineUnitList(String token);

	List<Map<String, Object>> getUnitList();
	

}
