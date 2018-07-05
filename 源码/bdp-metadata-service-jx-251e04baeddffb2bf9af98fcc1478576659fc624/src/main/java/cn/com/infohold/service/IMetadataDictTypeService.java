package cn.com.infohold.service;

import cn.com.infohold.entity.QueryDictData;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IMetadataDictTypeService {


    Map<String, List<Map<String, Object>>> getDictData(Collection<QueryDictData> qddList, String unit) throws Exception;

}
