package cn.com.infohold.service.auth.impl;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.crypto.Cipher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.authorization.auth.LoginBean;
import bdp.commons.dataservice.param.BatchOperationBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.param.InsertBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.param.QueryBeanField;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.common.DateUtil;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.bean.AuthDataBean;
import cn.com.infohold.bean.AuthKeyPairBean;
import cn.com.infohold.bean.AuthKeyPairHisBean;
import cn.com.infohold.service.auth.ILoginService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.AnalysisUtil;
import cn.com.infohold.util.CommonUtil;
import lombok.extern.log4j.Log4j2;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;



@Log4j2
@Service
public class LoginServiceImpl implements ILoginService {

	@Value("${dataUrl}")
	String dataUrl;
	@Value("${auth_token}")
	String auth_token;
	@Value("${auth_user}")
	String auth_user;
	@Value("${db_code}")
	String db_code;
	@Value("${unit_info_define}")
	String unit_info_define;
	@Value("${auth_key_pair}")
	String auth_key_pair;
	
	public static final String ENCRYPTION_ALGORITHM = "RSA";
	
	@Override
	public LoginBean login(LoginBean loginBean) throws Exception {
		List<Map<String, Object>> list = selectUsers(loginBean.getUserName());
		if (list.size() > 0) {
			Map<String, Object> user = list.get(0);
			if (loginBean.getPassword().equals(user.get("user_password"))) {
				loginBean.setToken(UUID.randomUUID().toString() + "_" + DateUtil.getServerTime(null));
				loginBean.setLoginTime(DateUtil.getServerTime(null));
				loginBean.setUserCNName(user.get("user_cn_name").toString());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", UUID.randomUUID().toString());
				map.put("token", loginBean.getToken());
				map.put("user_id", user.get("user_id").toString());
				map.put("unit_id", user.get("org_id").toString());
				map.put("login_time", loginBean.getLoginTime());
				map.put("expiry_time", loginBean.getLoginTime());
				map.put("last_opt_time", loginBean.getLoginTime());
				List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
				data.add(map);
				InsertBean insertBean = new InsertBean();
				insertBean.setData(data);
				insertBean.setTableCode(auth_token);
				RetBean retBeanAuth = CommonUtil.post(dataUrl + "/insert",
						JSONObject.parseObject(BasicJsonUtil.getInstance().toJsonString(insertBean)));
				if (retBeanAuth.getRet_code().equals("0")) {
					loginBean.setLoginStatus("0");
					JSONObject tokenJsonObject = new JSONObject();
					tokenJsonObject.putAll(map);
					loginBean.setMsg(tokenJsonObject.toJSONString());
				} else {
					loginBean.setLoginStatus("-1");
					loginBean.setMsg("生成TOKEN异常");
				}
			} else {
				loginBean.setLoginStatus("-1");
				loginBean.setMsg("密码不正确");
			}
		} else {
			loginBean.setLoginStatus("-1");
			loginBean.setMsg("用户名不存在");
		}
		return loginBean;
	}

	@Override
	public List<Map<String, Object>> selectUsers(String userName) throws Exception {
		ExecuteBySqlBean execute = new ExecuteBySqlBean();
		StringBuffer sb = new StringBuffer(" SELECT * FROM auth_user WHERE user_code='" + userName + "' ");
		execute.setDb_code(db_code);
		execute.setSql(sb.toString());
		RetBean retBean = CommonUtil.post(dataUrl + "/queryBySql", JSON.parseObject(JSON.toJSONString(execute)));
		return retBean.getResults();
	}

	@Override
	public LoginBean getUser(LoginBean loginBean) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetBean getToken(String token) {
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		// 条件 查询模板
		QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
		queryBeanCondition.setField1("token");
		queryBeanCondition.setCond("=");
		queryBeanCondition.setValue1(token);
		conditions.add(queryBeanCondition);
		queryBean.setConditions(conditions);
		// 表
		queryBean.setTable(auth_token);
		RetBean retBean = CommonUtil.post(dataUrl + "/query", JSON.parseObject(JSON.toJSONString(queryBean)));
		return retBean;
	}

	@Override
	public RetBean getUnitList(LoginBean loginBean) {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		String token = loginBean.getToken();
		RetBean result = new RetBean();
		BatchOperationBean batchOperationBean = new BatchOperationBean();
		List<QueryBean> queryBeans = new ArrayList<QueryBean>();
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		// 条件 查询模板
		QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
		queryBeanCondition.setField1("token");
		queryBeanCondition.setCond("=");
		queryBeanCondition.setValue1(token);
		conditions.add(queryBeanCondition);
		queryBean.setConditions(conditions);
		// 表
		queryBean.setTable(auth_token);

		List<QueryBeanField> fields1 = new ArrayList<QueryBeanField>();
		QueryBeanField field2 = new QueryBeanField();
		field2.setName("unit_id");
		fields1.add(field2);
		queryBean.setFields(fields1);
		queryBeans.add(queryBean);

		QueryBean queryBean1 = new QueryBean();
		// 表
		// 查询bean字段list
		List<QueryBeanField> fields = new ArrayList<QueryBeanField>();
		QueryBeanField field = new QueryBeanField();
		field.setName("unit_id");
		fields.add(field);
		QueryBeanField field1 = new QueryBeanField();
		field1.setName("manage_unit");
		QueryBeanField unit_name_field = new QueryBeanField();
		unit_name_field.setName("unit_name");
		fields.add(unit_name_field);
		fields.add(field1);
		QueryBeanField is_remove = new QueryBeanField();
		unit_name_field.setName("is_remove");
		fields.add(is_remove);
		fields.add(field1);
		queryBean1.setTable(unit_info_define);
		queryBean1.setFields(fields);
		queryBeans.add(queryBean1);
		batchOperationBean.setQueryBeans(queryBeans);

		RetBean retBean = CommonUtil.post(dataUrl + "/queryByList",
				JSON.parseObject(JSON.toJSONString(batchOperationBean)));

		endTime = System.currentTimeMillis();
		log.debug("getUnitList use {} ms", endTime - startTime);
		List<Map<String, Object>> unitList = new ArrayList<Map<String, Object>>();
		if (!"-1".equals(retBean.getRet_code())) {
			Map<String, List<Map<String, Object>>> map = retBean.getBatchResults();
			if (null != map.get(auth_token) && map.get(auth_token).size() > 0) {
				String unit_id = StringUtil.isNotEmpty(loginBean.getUnitId()) ? loginBean.getUnitId()
						: map.get(auth_token).get(0).get("unit_id").toString();// 页面选了机构就以页面为准
				if (null != map.get(unit_info_define)) {
					List<Map<String, Object>> list = map.get(unit_info_define);
					for (Map<String, Object> m : list) {
						m.put("user_unit_id", unit_id);
						if (unit_id.equals(m.get("unit_id"))) {
							unitList.add(m);
							Map<Object, List<Map<String, Object>>> maps = list.stream()
									.collect(Collectors.groupingBy(e -> e.get("manage_unit")));
							getChinlds(maps, unitList, unit_id);
							break;
						}
					}
				}
			}
		}
		result.setResults(unitList);
		endTime = System.currentTimeMillis();
		log.debug("getChinlds use {} ms", endTime - startTime);
		return result;
	}

	void getChinlds(Map<Object, List<Map<String, Object>>> maps, List<Map<String, Object>> result, String unitId) {
		List<Map<String, Object>> list = maps.get(unitId);
		if (list!=null&&list.size() > 0) {
			result.addAll(list);
			for (Map<String, Object> m : list) {
				if (m.get("unit_id") != null) {
					getChinlds(maps, result, m.get("unit_id").toString());
				}
			}
		}
	}
	
	@Override
	public LoginBean logByRsa(LoginBean loginBean,AuthDataBean authDataBean) throws Exception{		
		//获取私钥
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
		queryBeanCondition.setField1("system_code");
		queryBeanCondition.setCond("=");
		queryBeanCondition.setValue1(authDataBean.getSystemCode());	
		conditions.add(queryBeanCondition);
		queryBean.setConditions(conditions);			
		queryBean.setTable(auth_key_pair);
		RetBean retAuthBean = CommonUtil.post(dataUrl + "/query", JSON.parseObject(JSON.toJSONString(queryBean)));
		
		List<Map<String, Object>> listmap = retAuthBean.getResults();
		if(listmap.size()>0){
			Map<String, Object> mapKey = listmap.get(0);
			if(mapKey.size()>0){
				//获取密钥
				String privateKey = mapKey.get("private_key").toString();
				//String格式的加密数据先解密
				byte[] bs = decryptBASE64(authDataBean.getDataEncode());
				//通过私钥解密加密数据
				byte[] decodedData = decrypt(bs, privateKey, false);
				String decodeUserId = new String(decodedData);
				decodeUserId = decodeUserId.trim();
				
				queryBean = new QueryBean();
				conditions = new ArrayList<QueryBeanCondition>();
				queryBeanCondition = new QueryBeanCondition();
				queryBeanCondition.setField1("user_code");
				queryBeanCondition.setCond("=");
				queryBeanCondition.setValue1(decodeUserId);
				conditions.add(queryBeanCondition);
				queryBean.setConditions(conditions);
				queryBean.setTable(auth_user);
				RetBean retBean = CommonUtil.post(dataUrl + "/query", JSON.parseObject(JSON.toJSONString(queryBean)));
				
				if(retBean.getResults().size()>0){
					Map<String, Object> userMap = retBean.getResults().get(0);
					String userId = userMap.get("user_code").toString();
					loginBean.setUserName(userId);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					AuthKeyPairHisBean authKeyPairHisBean = new AuthKeyPairHisBean();
					authKeyPairHisBean.setSystemCodeHis(mapKey.get("system_code").toString());
					authKeyPairHisBean.setPublicKeyHis(mapKey.get("public_key").toString());
					authKeyPairHisBean.setPrivateKeyHis(mapKey.get("private_key").toString());
					authKeyPairHisBean.setUserIdHis(userId);
					authKeyPairHisBean.setDataHis(authDataBean.getDataEncode());
					authKeyPairHisBean.setCreateTimeHis(sdf.format(new Date()));
					//将公钥私钥记录数据
					CommonUtil.post(dataUrl + "/insert",JSONObject.parseObject(BasicJsonUtil.getInstance().toJsonString(authKeyPairHisBean)));
					
					List<Map<String, Object>> listLogin = selectUsers(loginBean.getUserName());
					Map<String, Object> user = listLogin.get(0);
					loginBean.setToken(UUID.randomUUID().toString() + "_" + DateUtil.getServerTime(null));
					loginBean.setLoginTime(DateUtil.getServerTime(null));
					loginBean.setUserCNName(user.get("user_cn_name").toString());
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", UUID.randomUUID().toString());
					map.put("token", loginBean.getToken());
					map.put("user_id", user.get("user_id").toString());
					map.put("unit_id", user.get("org_id").toString());
					map.put("login_time", loginBean.getLoginTime());
					map.put("expiry_time", loginBean.getLoginTime());
					map.put("last_opt_time", loginBean.getLoginTime());
					List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
					data.add(map);
					InsertBean insertBean = new InsertBean();
					insertBean.setData(data);
					insertBean.setTableCode(auth_token);
					RetBean retBeanAuth = CommonUtil.post(dataUrl + "/insert",JSONObject.parseObject(BasicJsonUtil.getInstance().toJsonString(insertBean)));
					if (retBeanAuth.getRet_code().equals("0")) {
						loginBean.setLoginStatus("0");
						JSONObject tokenJsonObject = new JSONObject();
						tokenJsonObject.putAll(map);
						loginBean.setMsg(tokenJsonObject.toJSONString());
					} else {
						loginBean.setLoginStatus("-1");
						loginBean.setMsg("生成TOKEN异常");
					}
				}else{
					loginBean.setLoginStatus("-1");
					loginBean.setMsg("用户名不匹配");
				}
			}
		}
		return loginBean;
	}
	
	//生成公钥和密钥对
	@Override
	public String getKeyMap(String systemCode) throws Exception{
		QueryBean queryBean = new QueryBean();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
		queryBeanCondition.setField1("system_code");
		queryBeanCondition.setCond("=");
		//假设system_code为5db56cfd-c57a-4c32-bc8c-c2a6c4bd41ed
		queryBeanCondition.setValue1(systemCode.trim());
		conditions.add(queryBeanCondition);
		queryBean.setConditions(conditions);
		queryBean.setTable(auth_key_pair);
		RetBean retAuthBean = CommonUtil.post(dataUrl + "/query", JSON.parseObject(JSON.toJSONString(queryBean)));	        
		List<Map<String, Object>> keymap = retAuthBean.getResults();
		String publicKey = null ;
        if(keymap.size()>0){
        	Map<String, Object> mapKey = keymap.get(0);    	
        	publicKey = mapKey.get("public_key").toString();
        }
		//返回公钥
        return publicKey;
	}

    public static byte[] decrypt(byte[] data, String keyString, boolean isPublic) throws Exception {
        Map<String, Object> keyAndFactoryMap = generateKeyAndFactory(keyString, isPublic);
        KeyFactory keyFactory = getKeyFactory(keyAndFactoryMap);
        Key key = getKey(keyAndFactoryMap);
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);

        return cipher.doFinal(data);
    }
	
    /**
     * 从指定对象中获取钥匙
     */
    public static Key getKey(Map<String, Object> map) {
        if (map.get("key") == null) {
            return null;
        }
        return (Key)map.get("key");
    }
    /**
     * 从指定对象中获取钥匙工厂
     */
    public static KeyFactory getKeyFactory(Map<String, Object> map) {
        if (map.get("keyFactory") == null) {
            return null;
        }
        return (KeyFactory)map.get("keyFactory");
    }
    /**
     * 生成钥匙
     */
    public static Map<String, Object> generateKeyAndFactory(String keyString, boolean isPublic) throws Exception {
        byte[] keyBytes = decryptBASE64(keyString);
        
        KeyFactory keyFactory = KeyFactory.getInstance(ENCRYPTION_ALGORITHM);
        Key key = null;
        if (isPublic) {
            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
            key = keyFactory.generatePublic(x509KeySpec);
        } else {
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
            key = keyFactory.generatePrivate(pkcs8KeySpec);
        }
        
        Map<String, Object> keyAndFactoryMap = new HashMap<String, Object>(2);
        keyAndFactoryMap.put("key", key);
        keyAndFactoryMap.put("keyFactory", keyFactory);
        
        return keyAndFactoryMap;
    }
    
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }
}
