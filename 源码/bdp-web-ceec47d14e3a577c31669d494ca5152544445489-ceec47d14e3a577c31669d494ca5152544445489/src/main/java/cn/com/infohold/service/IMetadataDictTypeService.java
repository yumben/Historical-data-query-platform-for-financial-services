package cn.com.infohold.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import cn.com.infohold.entity.QueryDictData;

public interface IMetadataDictTypeService {


    Map<String, List<Map<String, Object>>> getDictData(Collection<QueryDictData> qddList, String unit) throws Exception;


}
