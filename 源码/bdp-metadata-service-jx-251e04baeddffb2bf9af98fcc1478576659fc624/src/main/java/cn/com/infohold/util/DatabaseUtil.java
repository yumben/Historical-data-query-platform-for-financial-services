package cn.com.infohold.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.basic.util.jdbc.BasicJdbcUtil;
import cn.com.infohold.basic.util.jdbc.JdbcConBean;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.entity.MetadataProperty;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.service.IVMetadataPropertyService;
import cn.com.infohold.service.impl.AppparServiceImpl;
import cn.com.infohold.service.impl.MetadataPropertyServiceImpl;
import cn.com.infohold.service.impl.MetadataServiceImpl;
import cn.com.infohold.service.impl.MetamodelClasspropertyServiceImpl;
import cn.com.infohold.tools.util.StringUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2(topic = "DatabaseUtil")
@Service(value = "DatabaseUntil")
public class DatabaseUtil {
	@Autowired
	MetadataServiceImpl metadataServiceImpl;
	@Autowired
	AppparServiceImpl appparServiceImpl;
	@Autowired
	MetamodelClasspropertyServiceImpl metamodelClasspropertyServiceImpl;
	@Autowired
	MetadataPropertyServiceImpl metadataPropertyServiceImpl;
	@Autowired
	IVMetadataPropertyService vMetadataPropertyServiceImpl;

	public DatabaseUtil() {

	}

	public JdbcConBean initDb(Metadata metadataVO) throws Exception {
		JdbcConBean jdbcConBean = new JdbcConBean();
		String dbHosts = "";
		String bd_name = "";
		String connectTimeout = "10000";
		Map<String, Object> colunmMap = new HashMap<String, Object>();
		colunmMap.put("metadata_id", metadataVO.getMetadataId());
		List<Map<String, Object>> properties = vMetadataPropertyServiceImpl.selectProperty(colunmMap, false, false);
		String driver = (String) properties.get(0).get("db_driver");
//		String dbType = (String) properties.get(0).get("db_type");
		String host = (String) properties.get(0).get("db_host");
		String port = (String) properties.get(0).get("db_port");
		bd_name = (String) properties.get(0).get("db_name");
		String user = (String) properties.get(0).get("db_user");
		String password = (String) properties.get(0).get("db_password");
		dbHosts = host + ":" + port;

		jdbcConBean.setJdbcDriver(driver);
		jdbcConBean.setJdbcUserName(user);
		jdbcConBean.setJdbcPassword(password);
		if ("com.mysql.jdbc.Driver".equals(driver)) {
			jdbcConBean.setJdbcURL("jdbc:mysql://" + dbHosts + "/" + bd_name + "?useUnicode=true&characterEncoding=UTF-8");
		} else if ("oracle.jdbc.driver.OracleDriver".equals(driver)) {//
			jdbcConBean.setJdbcURL("jdbc:oracle:thin:@" + dbHosts + ":" + bd_name);
		} else if ("org.postgresql.Driver".equals(driver)) {//
			jdbcConBean.setJdbcURL("jdbc:postgresql://" + dbHosts + "/" + bd_name);
		} else {
			log.debug("暂时不支持该数据类型");
			return null;
		}
		jdbcConBean.setMaxWait(Integer.parseInt(connectTimeout));// 如果没设置则默认10秒
		return jdbcConBean;
	}

	public JdbcConBean initDb(Metadata metadataVO, String databaseName) throws Exception {
		JdbcConBean jdbcConBean = new JdbcConBean();

		String dbHosts = "";
		String dbDirver = "";
		String connectTimeout = "10000";
		Map<String, Object> colunmMap = new HashMap<String, Object>();
		colunmMap.put("metadata_id", metadataVO.getMetadataId());
		List<Map<String, Object>> properties = vMetadataPropertyServiceImpl.selectProperty(colunmMap, false, false);
		String driver = (String) properties.get(0).get("db_driver");
//		String dbType = (String) properties.get(0).get("db_type");
		String host = (String) properties.get(0).get("db_host");
		String port = (String) properties.get(0).get("db_port");
		String user = (String) properties.get(0).get("db_user");
		String password = (String) properties.get(0).get("db_password");
		dbHosts = host + ":" + port;

		jdbcConBean.setJdbcDriver(driver);
		jdbcConBean.setJdbcUserName(user);
		jdbcConBean.setJdbcPassword(password);
		if ("com.mysql.jdbc.Driver".equals(dbDirver)) {
			jdbcConBean.setJdbcURL("jdbc:mysql://" + dbHosts + "/" + databaseName + "?useUnicode=true&characterEncoding=UTF-8");
		} else if ("oracle.jdbc.driver.OracleDriver".equals(dbDirver)) {//
			jdbcConBean.setJdbcURL("jdbc:oracle:thin:@" + dbHosts + ":" + databaseName);
		} else if ("org.postgresql.Driver".equals(dbDirver)) {//
			jdbcConBean.setJdbcURL("jdbc:postgresql://" + dbHosts + "/" + databaseName);
		} else {
			log.debug("暂时不支持该数据类型");
			return null;
		}
		jdbcConBean.setMaxWait(Integer.parseInt(connectTimeout));// 如果没设置则默认10秒
		return jdbcConBean;
	}

	// 根据字段元素据ID查它所属数据库类型
	public String getDbTypeByfield(String metadataId) throws Exception {

		String dbType = "";
		Metadata metadataVO = metadataServiceImpl.selectById(metadataId);
		dbType = getDbTypeByTable(metadataVO.getParentMetadata() + "");
		return dbType;
	}

	/**
	 * 查询表类型元数据对应的数据
	 * 
	 * @param id
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getDataByMetadataId(String id, String json) throws Exception {
		Metadata metadata = metadataServiceImpl.selectById(id);
		Metadata parentMetadata = metadataServiceImpl.selectById(metadata.getParentMetadata());
		JdbcConBean conBean = initDb(parentMetadata);
		List<Map<String, Object>> list = BasicJdbcUtil.getInstance().select(conBean, SqlGenerator(id, json));
		return list;
	}

	/**
	 * 生成SQL
	 * 
	 * @param id
	 *            表元数据ID
	 * @param json
	 *            查询条件JSON字符串
	 * @return
	 */
	private String SqlGenerator(String id, String json) {
		JSONObject jsonObject = JSONObject.parseObject(json);
		String sql = "select * from ";
		String whereStr = " where 1=1 ";
		Map<String, Object> columMap = new HashMap<String, Object>();
		columMap.put("metadata_id", id);
		List<MetadataProperty> properties = metadataPropertyServiceImpl.selectByMap(columMap);
		for (MetadataProperty metadataProperty : properties) {
			MetamodelClassproperty classproperty = metamodelClasspropertyServiceImpl
					.selectById(metadataProperty.getClassPropertyId());
			if (classproperty.getPropertyCode().equals("table_name")) {
				sql += metadataProperty.getPropertyValue() + "";
			}
		}
		for (String key : jsonObject.keySet()) {
			whereStr += " and " + key + "='" + jsonObject.getString(key) + "'";
		}
		sql = sql + whereStr;
		return sql;
	}

	public List<JSONObject> getTableByMetadataId(String id, String tableName,String dbType) throws Exception {
		Metadata metadataVO = metadataServiceImpl.selectById(id);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("metadata_id", id);
		map.put("property_code", "db_name");
		List<Map<String, Object>> maps = vMetadataPropertyServiceImpl.selectProperty(map, false, false);
		if (maps.size() <= 0) {
			return null;
		}
		String sql="";
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		JdbcConBean conBean = initDb(metadataVO);
		if ("postgresql".equals(dbType)) {
			sql = "SELECT table_name ,table_name as table_comment FROM INFORMATION_SCHEMA.tables WHERE table_schema = 'public' AND table_type IN ('BASE TABLE', 'VIEW') AND table_name=?";
			list = BasicJdbcUtil.getInstance().select(conBean, sql,
					tableName);
		}else {
			sql = "select table_name,table_comment from information_schema.tables where table_schema=? and table_type='base table' ";
			if (!StringUtil.isEmpty(tableName)) {
				sql +=" AND table_name='"+tableName+"'";
			}
			list = BasicJdbcUtil.getInstance().select(conBean, sql,
					maps.get(0).get("db_name").toString());
		}
		
		
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		for (Map<String, Object> map1 : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("classId", "228");
			jsonObject.put("catalogId", metadataVO.getCatalogId());
			jsonObject.put("parentMetadata", metadataVO.getMetadataId());
			jsonObject.put("metadataCode", metadataVO.getMetadataCode() + "_" + map1.get("table_name"));
			if (!StringUtil.isEmpty(map1.get("table_comment").toString())) {
				jsonObject.put("metadataName", map1.get("table_comment"));
			} else {
				jsonObject.put("metadataName", map1.get("table_name"));
			}
			List<JSONObject> propertys = new ArrayList<JSONObject>();
			JSONObject property = new JSONObject();
			property.putAll(map1);
			propertys.add(property);
			jsonObject.put("property", propertys);
			jsonObject.put("childrens", getFieldByMetadataId(id, map1.get("table_name").toString(),dbType));
			jsonObjects.add(jsonObject);
		}
		return jsonObjects;
	}

	public List<JSONObject> getFieldByMetadataId(String id, String tableName,String dbType) throws Exception {
		Metadata metadataVO = metadataServiceImpl.selectById(id);
		Map<String, Object> map1 = new HashMap<String, Object>();
		map1.put("metadata_id", id);
		map1.put("property_code", "db_name");
		List<Map<String, Object>> maps = vMetadataPropertyServiceImpl.selectProperty(map1, false, false);
		if (maps.size() <= 0) {
			return null;
		}
		JdbcConBean conBean = initDb(metadataVO);
		String sql = "";
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		if ("postgresql".equals(dbType)) {
			
			sql = "select column_name,column_name AS column_comment,data_type,is_nullable from information_schema.columns where  table_name=?";
			list = BasicJdbcUtil.getInstance().select(conBean, sql,
					 tableName);
		}else {
			sql = "select column_name,column_comment,character_maximum_length,data_type,is_nullable from information_schema.columns where table_schema=? and table_name=?";
			list = BasicJdbcUtil.getInstance().select(conBean, sql,
					maps.get(0).get("db_name").toString(), tableName);
		}
		
		List<JSONObject> jsonObjects = new ArrayList<JSONObject>();
		for (Map<String, Object> map : list) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("classId", "230");
			jsonObject.put("catalogId", metadataVO.getCatalogId());
			jsonObject.put("parentMetadata", metadataVO.getMetadataId());
			jsonObject.put("metadataCode",
					metadataVO.getMetadataCode() + "_" + tableName + "_" + map.get("column_name"));
			if (!StringUtil.isEmpty(map.get("column_comment").toString())) {
				jsonObject.put("metadataName", map.get("column_comment"));
			} else {
				jsonObject.put("metadataName", map.get("column_name"));
			}
			List<JSONObject> propertys = new ArrayList<JSONObject>();
			JSONObject property = new JSONObject();
			property.putAll(map);
			propertys.add(property);
			jsonObject.put("property", propertys);
			jsonObjects.add(jsonObject);
		}
		return jsonObjects;
	}

	// 根据表元素据ID查它所属数据库类型
	public String getDbTypeByTable(String metadataId) throws Exception {
		String dbType = "";
		Metadata tableMetadataVO = metadataServiceImpl.selectById(metadataId);
		Metadata dbMetadataVO = metadataServiceImpl.selectById(tableMetadataVO.getParentMetadata() + "");
		Map<String, String> appparList = appparServiceImpl.selectAppparList("modelclass");
		List<MetamodelClassproperty> propertyClassVOs = metamodelClasspropertyServiceImpl
				.selectMetamodelClasspropertyList(dbMetadataVO.getClassId() + "");
		List<MetadataProperty> propertyVOs = metadataPropertyServiceImpl
				.queryMetadataPropertyVO(dbMetadataVO.getMetadataId() + "");
		for (int i = 0; i < propertyVOs.size(); i++) {
			for (int j = 0; j < propertyClassVOs.size(); j++) {
				if (propertyClassVOs.get(j).getPropertyCode().equals(appparList.get("db_driver"))
						&& (propertyClassVOs.get(j).getPropertyId() + "")
								.equals(propertyVOs.get(i).getClassPropertyId() + "")) {
					String dbDirver = propertyVOs.get(i).getPropertyValue();
					if ("com.mysql.jdbc.Driver".equals(dbDirver)) {
						dbType = "mysql";
						return dbType;
					} else if ("oracle.jdbc.driver.OracleDriver".equals(dbDirver)) {//
						dbType = "oracle";
						return dbType;
					} else if ("org.postgresql.Driver".equals(dbDirver)) {//
						dbType = "postgresql";
						return dbType;
					}
				}
			}
		}
		return dbType;
	}

        /*
	public boolean checkConnect(String type, String url, String username, String pwd, String dabasename)
			throws Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		JdbcConBean jdbcConBean = new JdbcConBean();
		jdbcConBean.setJdbcUserName(username);
		jdbcConBean.setJdbcPassword(pwd);

		if ("mysql".equals(type)) {
			jdbcConBean
					.setJdbcURL("jdbc:mysql://" + url + "/" + dabasename + "?useUnicode=true&characterEncoding=UTF-8");
			jdbcConBean.setJdbcDriver("com.mysql.jdbc.Driver");
		} else if ("oracle".equals(type)) {//
			jdbcConBean.setJdbcURL("jdbc:oracle:thin:@" + url + ":" + dabasename);
			jdbcConBean.setJdbcDriver("oracle.jdbc.driver.OracleDriver");
		} else if ("postgres".equals(type)) {//
			jdbcConBean.setJdbcURL("jdbc:postgresql://" + url + "/" + dabasename);
			jdbcConBean.setJdbcDriver("org.postgresql.Driver");
		} else {

		}
		jdbcConBean.setMaxWait(10000);// 如果没设置则默认10秒
		Connection conn = basicJdbcUtil.getCon(jdbcConBean);
		if (conn == null) {
			return false;
		} else {
			return true;
		}

	}
*/
}
