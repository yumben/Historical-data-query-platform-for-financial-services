package cn.com.infohold.util;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bdp.commons.metadata.ret.MetaData;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.basic.util.jdbc.BasicJdbcUtil;
import cn.com.infohold.basic.util.jdbc.JdbcConBean;
import cn.com.infohold.basic.util.mongo.MongoDbBean;
import cn.easybdp.basic.util.http.BasicHttpUtil;
//import cn.easybdp.basic.util.http.BasicHttpUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class MetadataUtil {

	private static final BasicHttpUtil BHU = BasicHttpUtil.getInstance();

	public static MetaData getMetadataBymetadataCode(String metadataCode, String level) throws IOException {
		String meatdataAddress = PropUtil.getProperty("bdp-metadata-service");
		String meatdataMapping = PropUtil.getProperty("meatdataMapping");
		Map<String, String> multiValueMap = new HashMap<String, String>();
		multiValueMap.put("metadataCode", metadataCode);
		multiValueMap.put("level", level);
		long s1 = System.currentTimeMillis();

		String ret = BHU.postRequst(meatdataAddress + meatdataMapping, multiValueMap);

		long s = System.currentTimeMillis();
		log.debug("post {}  use time {} param={}", meatdataAddress + meatdataMapping,
				(s - s1) + "ms-----------------------------------", multiValueMap);
		JSONObject jsonObject = JSON.parseObject(ret);
		if ("-1".equals(jsonObject.getString("code"))) {
			return null;
		}
		MetaData metadata = AnalysisUtil.toQueryBean(jsonObject.getJSONObject("metadataList").toJSONString(),
				MetaData.class);

		return metadata;
	}

	public static MetaData getMetadataBymetadataCode(String metadataCode) throws SQLException, Exception {
		String sql = "SELECT mm .metadata_id AS metadataId, mm .metadata_code AS metadataCode, mm .metadata_name AS metadataName, mm.parent_metadata AS parentMetadata, mm .catalog_id AS catalogId, mm.class_id AS classId, mc.property_code AS propertyCode, mp.property_value AS propertyValue FROM metadata M LEFT JOIN metadata mm ON M.parent_metadata= mm.metadata_id LEFT JOIN metadata_property mp ON (mm.metadata_id = mp.metadata_id OR M.metadata_id = mp.metadata_id ) LEFT JOIN metamodel_classproperty mc ON mp.class_property_id = mc.property_id  WHERE 1 = 1 AND M .metadata_code = '"
				+ metadataCode + "'";

		Map<String, Object> dataBaseInfoMap = new HashMap<String, Object>();

		dataBaseInfoMap.put("db_type", PropUtil.getProperty("db_type"));
		dataBaseInfoMap.put("db_driver", PropUtil.getProperty("db_driver"));
		dataBaseInfoMap.put("db_encoding", PropUtil.getProperty("db_encoding"));
		dataBaseInfoMap.put("db_name", PropUtil.getProperty("db_name"));
		dataBaseInfoMap.put("db_user", PropUtil.getProperty("db_user"));
		dataBaseInfoMap.put("db_port", PropUtil.getProperty("db_port"));
		dataBaseInfoMap.put("db_host", PropUtil.getProperty("db_host"));
		dataBaseInfoMap.put("db_password", PropUtil.getProperty("db_password"));

		JdbcConBean jdbcConBean = AnalysisUtil.setJdbcBeanSql(dataBaseInfoMap);

		List<Map<String, Object>> list = BasicJdbcUtil.getInstance().select(jdbcConBean, sql, new Object[] {});
		Map<String, Object> property = new HashMap<String, Object>();
		for (Map<String, Object> map : list) {
			String propertyCode = map.get("propertycode") + "";
			String propertyValue = map.get("propertyvalue") + "";
			property.put(propertyCode, propertyValue);
		}
		MetaData metadata = new MetaData();
		metadata.setProperty(property);
		return metadata;
	}

	public static void getMongoDbBean(MetaData metadata, MongoDbBean staticMongoBean) {
		MetaData parent = metadata.getParent();
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		propertyMap.putAll(metadata.getProperty());
		propertyMap.putAll(parent.getProperty());
		staticMongoBean.setDatabaseName(propertyMap.get("db_name").toString());
		staticMongoBean.setHost(propertyMap.get("db_host").toString());
		staticMongoBean.setPassword(propertyMap.get("db_password").toString());
		staticMongoBean.setPort(Integer.parseInt(propertyMap.get("db_port").toString()));
		staticMongoBean.setUsername(propertyMap.get("db_user").toString());
	}

}
