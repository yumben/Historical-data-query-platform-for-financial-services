package cn.com.infohold.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

import bdp.commons.dataservice.param.DeteleBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.param.InsertBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.UpdateBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.bean.MicroservicesUrlBean;
import cn.com.infohold.service.ICommonsDataService;
import cn.com.infohold.service.IServiceUrlService;
import java.util.Date;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-17
 */
@Log4j2(topic = "CommonsDataServiceImpl")
@Service
public class CommonsDataServiceImpl implements ICommonsDataService {
	@Autowired
	IServiceUrlService serviceUrlServiceImpl;
	@Autowired
	MicroservicesUrlBean microservicesUrlBean;

	@Override
	public String queryDictionary(QueryBean queryBean) {
		// 请求路径：192.168.31.254:9927/metadataQuiry?json
		JSONObject jsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		try {
			jsonObject.put("param", BasicJsonUtil.getInstance().toJsonString(queryBean));
			multiValueMap = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>() {
			});
			String json = serviceUrlServiceImpl.post(microservicesUrlBean.getBdpDataService(), "query", multiValueMap);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	@Override
	public RetBean queryData(QueryBean queryBean) {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		try {
			jsonObject.put("param", BasicJsonUtil.getInstance().toJsonString(queryBean));
			multiValueMap = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>() {
			});
			String json = serviceUrlServiceImpl.post(microservicesUrlBean.getBdpDataService(), "query", multiValueMap);
			RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(json, RetBean.class);
			return retBean;
		} catch (Exception e) {
			e.printStackTrace();
			RetBean retBean = new RetBean();
			retBean.setRet_code("err");
			retBean.setRet_message(e.getMessage());
			return retBean;
		}
	}
	
	@Override
	public RetBean queryDataSql(ExecuteBySqlBean executeBySqlBean) {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		try {
			jsonObject.put("param", BasicJsonUtil.getInstance().toJsonString(executeBySqlBean));
			multiValueMap = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>() {
			});
			log.debug("请求json=" + jsonObject.toString());
			String json = serviceUrlServiceImpl.post(microservicesUrlBean.getBdpDataService(), "queryBySql", multiValueMap);
			log.debug("返回json=" + json);
			RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(json, RetBean.class);
			return retBean;
		} catch (Exception e) {
			e.printStackTrace();
			RetBean retBean = new RetBean();
			retBean.setRet_code("err");
			retBean.setRet_message(e.getMessage());
			return retBean;
		}
	}

	@Override
	public RetBean insertData(InsertBean insertBean) {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		try {
			jsonObject.put("param", BasicJsonUtil.getInstance().toJsonString(insertBean));
			multiValueMap = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>() {
			});
			log.debug("请求json=" + jsonObject.toString());
			String json = serviceUrlServiceImpl.post(microservicesUrlBean.getBdpDataService(), "insert", multiValueMap);
			log.debug("返回json=" + json);
			RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(json, RetBean.class);
			return retBean;
		} catch (Exception e) {
			e.printStackTrace();
			RetBean retBean = new RetBean();
			retBean.setRet_code("err");
			retBean.setRet_message(e.getMessage());
			return retBean;
		}
	}

	@Override
	public RetBean updateData(UpdateBean updateBean) {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		try {
			jsonObject.put("param", BasicJsonUtil.getInstance().toJsonString(updateBean));
			multiValueMap = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>() {
			});
			log.debug("请求json=" + jsonObject.toString());
			String json = serviceUrlServiceImpl.post(microservicesUrlBean.getBdpDataService(), "update", multiValueMap);
			log.debug("返回json=" + json);
			RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(json, RetBean.class);
			return retBean;
		} catch (Exception e) {
			e.printStackTrace();
			RetBean retBean = new RetBean();
			retBean.setRet_code("err");
			retBean.setRet_message(e.getMessage());
			return retBean;
		}
	}

	@Override
	public RetBean deleteData(DeteleBean deteleBean) {
		JSONObject jsonObject = new JSONObject();
		Map<String, String> multiValueMap = new HashMap<String, String>();
		try {
			jsonObject.put("param", BasicJsonUtil.getInstance().toJsonString(deteleBean));
			multiValueMap = JSON.parseObject(jsonObject.toJSONString(), new TypeReference<Map<String, String>>() {
			});
			log.debug("请求删除json=" + jsonObject.toString());
			String json = serviceUrlServiceImpl.post(microservicesUrlBean.getBdpDataService(), "delete", multiValueMap);
			log.debug("返回删除json=" + json);
			RetBean retBean = BasicJsonUtil.getInstance().toJavaBean(json, RetBean.class);
			return retBean;
		} catch (Exception e) {
			e.printStackTrace();
			RetBean retBean = new RetBean();
			retBean.setRet_code("err");
			retBean.setRet_message(e.getMessage());
			return retBean;
		}
	}
}
