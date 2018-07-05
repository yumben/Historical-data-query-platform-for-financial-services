package cn.com.infohold.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.core.controller.BaseController;
import cn.com.infohold.entity.QueryDictData;
import cn.com.infohold.entity.VMetadataProperty;
import cn.com.infohold.service.IMetadataDictTypeService;
import cn.com.infohold.service.IVMetadataPropertyService;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/metadataDictType")
public class MetadataDictTypeController extends BaseController {

    private static String dict_id_code = "dict_id";
    private static String parent_metadata_code = "parent_metadata";
    @Autowired
    IVMetadataPropertyService vMetadataPropertyServiceImpl;
    @Autowired
    IMetadataDictTypeService MetadataDictTypeServiceImpl;

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

    @RequestMapping(value = "/selectMetadataPK", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject selectMetadataPK(@RequestParam String metadata_id, HttpServletRequest request) {
        //获取属性
        List<VMetadataProperty> mpList = new ArrayList<>();
        //排除不含dict_id的mataid
        Collection<QueryDictData> qddList = null;
        //根据 metadata_id , dict_id ,unit_id 查出各个属性 
        Map<String, List<Map<String, Object>>> retMap = new HashMap<>();

        String token = request.getParameter("token");
        JSONObject tokenjson = JSONObject.parseObject(token);
        String unit_id = tokenjson.getString("unit_id");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", 0);
        try {
            String[] ids = metadata_id.split(",");
            List<String> idList = Arrays.asList(ids);
            //获取属性
            mpList = vMetadataPropertyServiceImpl.selectPropertyByIds(idList, false, false);
            //log.debug("mpList={}", mpList);
            //过滤和获取相应的信息
            qddList = geneQueryDictData(mpList);
            log.debug("qddList={}", qddList);
            mpList.clear();
            //获取字典数据
            Map<String, List<Map<String, Object>>> dicMap = MetadataDictTypeServiceImpl.getDictData(qddList, unit_id);
            log.debug("dicMap={}", dicMap);
            //拼装
            Map<String, List<QueryDictData>> qddMap = qddList.parallelStream().collect(Collectors.groupingBy(qd -> qd.getMataId()));
            //log.debug("qddMap={}", qddMap);
            for (String id : ids) {
                QueryDictData dicData = qddMap.get(id).get(0);
                String dicID = dicData.getDict_id();
                List<Map<String, Object>> list1 = dicMap.get(dicID);
                jsonObject.put(id, list1);
            }
            //log.debug("jsonObject={}", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
            jsonObject.put("code", -2);
        }
        return jsonObject;
    }

}
