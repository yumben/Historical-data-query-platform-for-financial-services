package cn.com.infohold.service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.joda.MillisecondInstantPrinter;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.com.infohold.basic.util.jdbc.BasicJdbcUtil;
import cn.com.infohold.basic.util.jdbc.JdbcConBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.entity.Metadata;
import cn.com.infohold.entity.MetadataProperty;
import cn.com.infohold.entity.MetamodelClass;
import cn.com.infohold.entity.MetamodelClassproperty;
import cn.com.infohold.service.IMetadataDatabaseService;
import cn.com.infohold.service.IVMetadataPropertyService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.DatabaseUtil;
import cn.com.infohold.util.NumberUtil;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MetadataDatabaseServiceImpl implements IMetadataDatabaseService {
	//private static final Log logger = LogFactory.getLog(MetadataDatabaseServiceImpl.class);
	@Autowired
	DatabaseUtil databaseUtil;
	@Autowired
	MetadataServiceImpl metadataServiceImpl;// = (MetadataServiceImpl)
											// BeanTools.getBean(MetadataServiceImpl.class);
	@Autowired
	AppparServiceImpl appparServiceImpl;// = (AppparServiceImpl)
										// BeanTools.getBean(AppparServiceImpl.class);
	@Autowired
	MetamodelClasspropertyServiceImpl metamodelClasspropertyServiceImpl;// =
																		// (MetamodelClasspropertyServiceImpl)
																		// BeanTools.getBean(MetamodelClasspropertyServiceImpl.class);
	@Autowired
	MetadataPropertyServiceImpl metadataPropertyServiceImpl;// =
															// (MetadataPropertyServiceImpl)
															// BeanTools.getBean(MetadataPropertyServiceImpl.class);
	@Autowired
	MetamodelClassServiceImpl metamodelClassServiceImpl;// =
														// (MetamodelClassServiceImpl)BeanTools.getBean(MetamodelClassServiceImpl.class);
	@Autowired
	IVMetadataPropertyService iVMetadataPropertyServiceImpl;

	@Override
	public Map<String, String> addTableColumn(Metadata metadataVO, String[] propertyValue) throws Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		Map<String, String> resMap = new HashMap<String, String>();
		Metadata tableMetadataVO = metadataServiceImpl.selectById(metadataVO.getParentMetadata());
		// 获取表属性
		JSONObject tableJsonObject = new JSONObject();

		List<Map<String, Object>> tablesList = iVMetadataPropertyServiceImpl
				.queryPropertyByMatedataId(metadataVO.getParentMetadata());
		if (tablesList.size() > 0) {
			tableJsonObject.putAll(tablesList.get(0));
		}

		Metadata dbMetadataVO = metadataServiceImpl.selectById((String) tableJsonObject.get("parent_metadata"));
		String tableName = (String) tableJsonObject.get("table_name");
		// String checkTable = "show tables like \"" + tableName + "\"";
		String type = "";
		String length = "";
		String isnull = "";
		String iskey = "";
		String isfkey = "";
		String isUnique = "";
		String charCode = " DEFAULT CHARSET=utf8";
		String newField = "";
		Map<String, String> propertyMap = propertyValueToMap(propertyValue);
		if (propertyMap != null) {
			type = propertyMap.get("type");
			length = propertyMap.get("length");
			isnull = propertyMap.get("isnull");
			iskey = propertyMap.get("iskey");
			isfkey = propertyMap.get("isfkey");
			isUnique = propertyMap.get("isUnique");
			newField = propertyMap.get("column_name");
		}
		try {
			JdbcConBean jc = databaseUtil.initDb(dbMetadataVO);

			if (jc == null) {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "暂时不支持该类型数据库操作");
				return resMap;
			}

			String checkTableSQL = "";
			String checkFieldSQL = "";
			String dbType = "";
			// String newField = metadataVO.getMetadataCode();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			boolean tableExist = false;
			if ("com.mysql.jdbc.Driver".equals(jc.getJdbcDriver())) {
				checkTableSQL = "show tables like \"" + tableName + "\"";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				if (list.size() > 0) {
					tableExist = true;
					checkFieldSQL = "DESCRIBE " + tableName;
				}
				dbType = "mysql";
			} else if ("oracle.jdbc.driver.OracleDriver".equals(jc.getJdbcDriver())) {
				tableName = tableName.toUpperCase();
				newField = newField.toUpperCase();
				checkTableSQL = "select count(*) as EXIST from user_tables where table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				// log.debug("111="+list.get(0));
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("EXIST").toString())) {
					tableExist = true;
					checkFieldSQL = "SELECT COUNT(*) as EXIST FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '" + tableName
							+ "' AND COLUMN_NAME = '" + newField + "'";
				}
				dbType = "oracle";
			} else if ("org.postgresql.Driver".equals(jc.getJdbcDriver())) {
				// 查询表是否存在
				checkTableSQL = "select count(*) from information_schema.tables where  table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				// log.debug("111="+list.get(0));
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("count").toString())) {
					tableExist = true;
					checkFieldSQL = "SELECT COUNT(*) as EXIST FROM information_schema.columns WHERE table_name  = '"
							+ tableName + "' AND column_name = '" + newField + "'";
				}
				dbType = "postgres";
			}

			// 判断是否需要长度
			if (checkeIsNeedLength(type) && !"postgres".equals(dbType)) {
				if (length == null || length.length() < 1) {
					resMap.put("returnCode", "0001");
					resMap.put("msg", "请输入长度");
					return resMap;
				}
			}

			// 表是否存在
			if (tableExist) {
				log.debug("table exist!");// 则增加字段
				String alertTable = "";
				String alertNullTable = "";
				// 字段存在则修改
				if (fieldIsExist(jc, checkFieldSQL, newField, dbType)) {
					if ("mysql".equals(dbType)) {// 字段存在
						if (checkeIsNeedLength(type)) {// 需要加长度
							alertTable = "alter table " + tableName + " modify column " + newField + " " + type + "("
									+ length + ") ";
						} else {
							alertTable = "alter table " + tableName + " modify column " + newField + " " + type;
						}
						if (isnull != null && isnull != "" && "N".equals(isnull.toUpperCase())) {
							alertTable += " NOT NULL ";
						} else if (isnull != null && isnull != "" && "Y".equals(isnull.toUpperCase())) {
							alertTable += " NULL ";
						}
					} else if ("oracle".equals(dbType)) {
						if (checkeIsNeedLength(type)) {
							alertTable = "alter table " + tableName + " modify " + newField + " " + type + "(" + length
									+ ") ";
						} else {
							alertTable = "alter table " + tableName + " modify " + newField + " " + type;
						}
						if (isnull != null && isnull != "" && "N".equals(isnull.toUpperCase())) {
							alertNullTable = "alter table " + tableName + " modify " + newField + " NOT NULL ";
						}

					} else if ("postgres".equals(dbType)) {

						alertTable = "alter table " + tableName + " alter column \"" + newField + "\" type " + type;

					} else {// 其他类型的数据库暂时不支持

					}

				} else {// 字段不存在则新增
					if ("postgres".equals(dbType)) {
						alertTable = "ALTER TABLE " + tableName + " ADD COLUMN \"" + newField + "\" " + type;
					} else {
						if (checkeIsNeedLength(type)) {
							alertTable = "alter table " + tableName + " add " + newField + " " + type + "(" + length
									+ ") ";
						} else {
							alertTable = "alter table " + tableName + " add " + newField + " " + type;
						}
						if (isnull != null && isnull != "" && "N".equals(isnull.toUpperCase())) {
							alertTable += " NOT NULL ";
						}
					}

				}
				if (alertTable != "") {
					basicJdbcUtil.executeUpdate(jc, alertTable);
				}
				if (alertNullTable != "") {
					basicJdbcUtil.executeUpdate(jc, alertTable);
				}

				if (iskey != null && iskey != "" && "Y".equals(iskey.toUpperCase())) {// 更新主键
					if ("mysql".equals(dbType)) {// mysql

						updateMysqlPrimaryKey(jc, tableName, newField, iskey, true);

					} else if ("oracle".equals(dbType)) {// oracle
						updateOraclePrimaryKey(jc, tableName, newField, iskey, true);
					}

				}

			} else {// 否则增加表
				if ("postgres".equals(dbType)) {
					String[] field = { newField, type };
					addPostGreCol(tableMetadataVO, field, tableName);// tableJsonObject
					// addPostGreCol(tableMetadataVO,field);
				} else {
					String createtable = "create table " + tableName + " (" + newField + " " + type;
					if (checkeIsNeedLength(type)) {
						createtable += "(" + length + ") ";
					}
					if (isnull != null && ("N" == isnull || "n" == isnull)) {
						createtable += "NOT NULL ";
					}
					if (iskey != null && (iskey == "Y" || iskey == "y")) {
						if ("mysql".equals(dbType)) {
							createtable += " ,primary key(" + newField + "))";
						} else if ("oracle".equals(dbType)) {
							createtable += " ,constraint pkey primary key(" + newField + "))";
						}

					} else {
						createtable += " ) ";
					}
					if (createtable != "") {
						basicJdbcUtil.executeUpdate(jc, createtable);
						log.debug("create table success!");
					}
				}

			}
			resMap.put("returnCode", "0000");

		} catch (Exception e) {
			resMap.put("returnCode", "0001");
			resMap.put("msg", "操作数据库失败");
			throw e;
		}
		return resMap;
	}

	@Override
	public boolean delTableColumn(Metadata metadataVO) throws Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		boolean flag = true;
		/*
		 * Metadata tableMetadataVO =
		 * metadataServiceImpl.selectById(metadataVO.getParentMetadata());
		 * Metadata dbMetadataVO =
		 * metadataServiceImpl.selectById(tableMetadataVO.getParentMetadata());
		 * String tableName = tableMetadataVO.getMetadataCode(); String filed =
		 * metadataVO.getMetadataCode();
		 */

		// 获取表属性

		List<Map<String, Object>> tablesList = iVMetadataPropertyServiceImpl
				.queryPropertyByMatedataId(metadataVO.getParentMetadata());
		JSONObject tableJsonObject = new JSONObject();
		if (tablesList.size() > 0) {
			tableJsonObject.putAll(tablesList.get(0));
		}
		// 获取字段属性
		List<Map<String, Object>> fieldsList = iVMetadataPropertyServiceImpl
				.queryPropertyByMatedataId(metadataVO.getMetadataId());
		JSONObject fieldJsonObject = new JSONObject();
		if (fieldsList.size() > 0) {
			tableJsonObject.putAll(fieldsList.get(0));
		}
		Metadata dbMetadataVO = metadataServiceImpl.selectById((String) tableJsonObject.get("parent_metadata"));
		String tableName = tableJsonObject.getString("table_name");
		String fieldName = fieldJsonObject.getString("column_name");
		String checkTable = "show tables like \"" + tableName + "\"";
		try {
			JdbcConBean jc = databaseUtil.initDb(dbMetadataVO);
			if (jc == null) {
				return false;
			}
			String alertTable = "";
			String checkTableSQL = "";
			String checkFieldSQL = "";
			String dbType = "";
			boolean tableExist = false;
			// String fieldName = metadataVO.getMetadataCode();
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			if ("com.mysql.jdbc.Driver".equals(jc.getJdbcDriver())) {
				checkTableSQL = "show tables like \"" + tableName + "\"";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				if (list.size() > 0) {
					tableExist = true;
					checkFieldSQL = "DESCRIBE " + tableName;
				}
				dbType = "mysql";
			} else if ("oracle.jdbc.driver.OracleDriver".equals(jc.getJdbcDriver())) {

				tableName = tableName.toUpperCase();
				fieldName = fieldName.toUpperCase();
				checkTableSQL = "select count(*) as EXIST from user_tables where table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				// log.debug("111="+list.get(0));
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("EXIST").toString())) {
					tableExist = true;
					checkFieldSQL = "SELECT COUNT(*) as EXIST FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '" + tableName
							+ "' AND COLUMN_NAME = '" + fieldName + "'";
				}
				dbType = "oracle";
			} else if ("org.postgresql.Driver".equals(jc.getJdbcDriver())) {
				// 查询表是否存在
				checkTableSQL = "select count(*) from information_schema.tables where  table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				// log.debug("111="+list.get(0));
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("count").toString())) {
					tableExist = true;
					checkFieldSQL = "SELECT COUNT(*) as EXIST FROM information_schema.columns WHERE table_name  = '"
							+ tableName + "' AND column_name = '" + fieldName + "'";
				}
				dbType = "postgres";
			}
			// List<Map<String, Object>> list = basicJdbcUtil.select(jc,
			// checkTable);

			if (tableExist) {
				log.debug("table exist!");// 则删除字段

				if (!fieldIsExist(jc, checkFieldSQL, fieldName, dbType)) {// 字段不存在不操作
					return true;
				}
				// 判断是否为表中最后一个字段
				List<Metadata> queryMetaDataByparentId = metadataServiceImpl
						.queryMetaDataByparentId(metadataVO.getParentMetadata());
				if (queryMetaDataByparentId.size() > 1) {
					alertTable = "alter table " + tableName + " drop COLUMN  " + fieldName;
				} else {// 连表一起删
					if ("postgres".equals(dbType)) {
						alertTable = "DROP FOREIGN TABLE " + tableName;
					} else {
						alertTable = "drop table " + tableName;
					}

				}
				basicJdbcUtil.executeUpdate(jc, alertTable);
			}
			flag = true;

		} catch (Exception e) {
			// flag = false ;
			throw e;
		}
		return flag;
	}

	@Override
	public boolean dropDatabase(Metadata metadataVO) throws Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		boolean flag = true;
		try {

			JdbcConBean jc = databaseUtil.initDb(metadataVO);
			if (jc == null) {
				return false;
			}
			basicJdbcUtil.executeUpdate(jc, "drop database " + metadataVO.getMetadataCode());
			flag = true;

		} catch (Exception e) {
			// flag = false ;
			throw e;
		}
		return flag;
	}

	@Override
	public boolean delTable(Metadata metadataVO) throws Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		boolean flag = true;
		Metadata dbMetadataVO = metadataServiceImpl.selectById(metadataVO.getParentMetadata());
		// 获取表属性
		// JSONObject tableJsonObject =
		// iVMetadataPropertyServiceImpl.queryPropertyByMatedataId(metadataVO.getParentMetadata());
		JSONObject tableJsonObject = new JSONObject();
		List<Map<String, Object>> tablesList = iVMetadataPropertyServiceImpl
				.queryPropertyByMatedataId(metadataVO.getParentMetadata());
		if (tablesList.size() > 0) {
			tableJsonObject.putAll(tablesList.get(0));
		}
		String tableName = tableJsonObject.getString("table_name");
		try {
			JdbcConBean jc = databaseUtil.initDb(dbMetadataVO);
			if (jc == null) {
				return false;
			}
			String checkTableSQL = "";
			String alertTable = "";
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			boolean tableExist = false;
			// 判断表是否存在，存在则执行删除语句
			if ("com.mysql.jdbc.Driver".equals(jc.getJdbcDriver())) {
				checkTableSQL = "show tables like \"" + tableName + "\"";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				if (list.size() > 0) {
					tableExist = true;
				}
				alertTable = "drop table " + tableName;
			} else if ("oracle.jdbc.driver.OracleDriver".equals(jc.getJdbcDriver())) {
				tableName = tableName.toUpperCase();
				checkTableSQL = "select count(*) as EXIST from user_tables where table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("EXIST").toString())) {
					tableExist = true;
				}
				alertTable = "drop table " + tableName;
			} else if ("org.postgresql.Driver".equals(jc.getJdbcDriver())) {
				checkTableSQL = "select count(*) from information_schema.tables where  table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("count").toString())) {
					tableExist = true;
				}
				alertTable = "DROP FOREIGN TABLE " + tableName;
			}
			if (tableExist) {
				log.debug("table exist!");// 则删除表
				basicJdbcUtil.executeUpdate(jc, alertTable);
			}
			flag = true;

		} catch (Exception e) {
			// flag = false ;
			throw e;
		}
		return flag;
	}

	@Override
	public Map<String, String> updateTable(Metadata metadataVO, String newName) throws Exception {

		Map<String, String> resMap = new HashMap<String, String>();

		try {

			BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
			
			// 获取表属性	
			JSONObject tableJsonObject = new JSONObject();
			List<Map<String, Object>> tablesList = iVMetadataPropertyServiceImpl
					.queryPropertyByMatedataId(metadataVO.getMetadataId());
			if (tablesList!=null && tablesList.size() > 0) {
				tableJsonObject.putAll(tablesList.get(0));
			}
			Metadata dbMetadataVO = metadataServiceImpl.selectById((String) tableJsonObject.get("parent_metadata"));
			String tableName = tableJsonObject.getString("table_name");
			// 表是否为空，是的话返回报错
			if (StringUtil.isEmpty(tableName)) {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "原表名不能为空");
				return resMap;
			}
			// 查询新表名与原表名是否一样，是则直接返回
			if (tableName.equals(newName)) {
				resMap.put("returnCode", "0000");
				return resMap;
			}
			// JdbcConBean jc = databaseUtil.initDb(dbMetadataVO);
			JdbcConBean jc = databaseUtil.initDb(dbMetadataVO);
			if (jc == null) {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "暂时不支持该类型数据库连接");
				return resMap;
			}
			String checkTableSQL = "";
			String alertTable = "";
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			boolean tableExist = false;
			if ("com.mysql.jdbc.Driver".equals(jc.getJdbcDriver())) {
				// 查询新表是不是已经存在
				checkTableSQL = "show tables like \"" + newName + "\"";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				if (list.size() > 0) {
					resMap.put("returnCode", "0001");
					resMap.put("msg", "新表名已经存在，请重新输入");
					return resMap;
				}
				// 查询原表是否存在
				checkTableSQL = "show tables like \"" + tableName + "\"";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				if (list.size() > 0) {
					tableExist = true;
				}
				alertTable = "alter table " + tableName + " rename " + newName;
			} else if ("oracle.jdbc.driver.OracleDriver".equals(jc.getJdbcDriver())) {

				// 查询新表是不是已经存在
				checkTableSQL = "select count(*) as EXIST from user_tables where table_name ='" + newName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("EXIST").toString())) {
					resMap.put("returnCode", "0001");
					resMap.put("msg", "新表明已经存在，请重新输入");
					return resMap;
				}
				// 查询原表是否存在
				tableName = tableName.toUpperCase();
				newName = newName.toUpperCase();
				checkTableSQL = "select count(*) as EXIST from user_tables where table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				map = list.get(0);
				if (1 == Integer.parseInt(map.get("EXIST").toString())) {
					tableExist = true;
				}
				alertTable = "alter table " + tableName + " rename to " + newName;
			} else if ("org.postgresql.Driver".equals(jc.getJdbcDriver())) {
				// 查询新表是不是已经存在
				checkTableSQL = "select count(*) from information_schema.tables where  table_name ='" + newName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("count").toString())) {
					resMap.put("returnCode", "0001");
					resMap.put("msg", "新表名已经存在，请重新输入");
					return resMap;
				}
				// 查询原表是否存在
				checkTableSQL = "select count(*) from information_schema.tables where  table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				map = list.get(0);
				if (1 == Integer.parseInt(map.get("count").toString())) {
					tableExist = true;
				}
				alertTable = "alter table " + tableName + " rename to " + newName;
			}
			if (tableExist) {
				log.debug("table exist!");// 则改表名
				basicJdbcUtil.executeUpdate(jc, alertTable);
				resMap.put("returnCode", "0000");
			} else {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "表不存在");
				return resMap;
			}

		} catch (Exception e) {
			// flag = false ;
			resMap.put("returnCode", "0001");
			resMap.put("msg", "操作数据库异常");
			throw e;
		}
		return resMap;
	}

	@Override
	public Map<String, String> updateTableColumn(Metadata metadataVO, String[] propertyValue) throws Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		Map<String, String> resMap = new HashMap<String, String>();
		// 获取表属性

		List<Map<String, Object>> tablesList = iVMetadataPropertyServiceImpl
				.queryPropertyByMatedataId(metadataVO.getParentMetadata());
		JSONObject tableJsonObject = new JSONObject();
		if (tablesList.size() > 0) {
			tableJsonObject.putAll(tablesList.get(0));
		}
		// 获取字段属性
		List<Map<String, Object>> fieldsList = iVMetadataPropertyServiceImpl
				.queryPropertyByMatedataId(metadataVO.getMetadataId());
		JSONObject fieldJsonObject = new JSONObject();
		if (fieldsList.size() > 0) {
			fieldJsonObject.putAll(fieldsList.get(0));
		}
		Metadata dbMetadataVO = metadataServiceImpl.selectById((String) tableJsonObject.get("parent_metadata"));
		String tableName = tableJsonObject.getString("table_name");

		String type = "";
		String length = "";
		String iskey = "";
		String isfkey = "";
		String isUnique = "";
		String isnull = "";
		String oldField = fieldJsonObject.getString("column_name");
		String newField = "";
		if (propertyValue != null && propertyValue.length > 0) {

			Map<String, String> propertyMap = propertyValueToMap(propertyValue);
			if (propertyMap != null) {
				type = propertyMap.get("type");
				length = propertyMap.get("length");
				isnull = propertyMap.get("isnull");
				iskey = propertyMap.get("iskey");
				isfkey = propertyMap.get("isfkey");
				isUnique = propertyMap.get("isUnique");
				newField = propertyMap.get("column_name");
			}

		} else {

			String data_type = fieldJsonObject.getString("data_type");// 当时为了配合指标，字段类型存的是：类型#标识(如：varchar#1)等
			String[] data_type_arr = data_type.split(data_type);
			type = data_type_arr[0];// character_maximum_length
			length = fieldJsonObject.getString("character_maximum_length");
			isnull = fieldJsonObject.getString("isnull");
			iskey = fieldJsonObject.getString("iskey");
			isfkey = fieldJsonObject.getString("isfkey");
			isUnique = fieldJsonObject.getString("isUnique");
			newField = fieldJsonObject.getString("column_name");

		}

		try {

			JdbcConBean jc = databaseUtil.initDb(dbMetadataVO);
			if (jc == null) {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "连接数据库失败");
				return resMap;
			}

			String checkTableSQL = "";
			String checkFieldSQL = "";
			String checkOldFieldSQL = "";
			String dbType = "";

			// String oldField = metadataVO.getMetadataCode();
			boolean isexistNewField = false;
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			boolean tableExist = false;
			if ("com.mysql.jdbc.Driver".equals(jc.getJdbcDriver())) {
				checkTableSQL = "show tables like \"" + tableName + "\"";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				if (list.size() > 0) {
					tableExist = true;
					checkFieldSQL = "DESCRIBE " + tableName;
					checkOldFieldSQL = "DESCRIBE " + tableName;
				}
				dbType = "mysql";
			} else if ("oracle.jdbc.driver.OracleDriver".equals(jc.getJdbcDriver())) {
				tableName = tableName.toUpperCase();
				newField = newField.toUpperCase();
				oldField = oldField.toUpperCase();
				checkTableSQL = "select count(*) as EXIST from user_tables where table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				// log.debug("111="+list.get(0));
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("EXIST").toString())) {
					tableExist = true;
					checkFieldSQL = "SELECT COUNT(*) as EXIST FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '" + tableName
							+ "' AND COLUMN_NAME = '" + newField + "'";
					checkOldFieldSQL = "SELECT COUNT(*) as EXIST FROM USER_TAB_COLUMNS WHERE TABLE_NAME = '" + tableName
							+ "' AND COLUMN_NAME = '" + oldField + "'";
				}
				dbType = "oracle";
			} else if ("org.postgresql.Driver".equals(jc.getJdbcDriver())) {
				// 查询表是否存在
				checkTableSQL = "select count(*) from information_schema.tables where  table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				// log.debug("111="+list.get(0));
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("count").toString())) {
					tableExist = true;
					checkFieldSQL = "SELECT COUNT(*) as EXIST FROM information_schema.columns WHERE table_name  = '"
							+ tableName + "' AND column_name = '" + newField + "'";
					checkOldFieldSQL = "SELECT COUNT(*) as EXIST FROM information_schema.columns WHERE table_name  = '"
							+ tableName + "' AND column_name = '" + oldField + "'";
				}
				dbType = "postgres";
			}
			// 判断是否需要长度
			if (checkeIsNeedLength(type) && !"postgres".equals(dbType)) {
				if (length == null || length.length() < 1) {
					resMap.put("returnCode", "0001");
					resMap.put("msg", "请输入长度");
					return resMap;
				}
			}
			// 表是否存在
			if (tableExist) {
				log.debug("table exist!");// 则修改字段
				String alertTable = "";
				if (fieldIsExist(jc, checkFieldSQL, newField, dbType)) {// 新字段存在
					// 新字段已经存在，说明数据库已经存在字段只要修改字段类型同步元数据就行
					if ("mysql".equals(dbType)) {
						alertTable = "alter table " + tableName + " modify column " + newField + " ";
						if (type != "") {
							alertTable += " " + type;
						}
						if (checkeIsNeedLength(type)) {
							alertTable += "(" + length + ")";
						}
						if (isnull != null && isnull != "" && "N".equals(isnull.toUpperCase())) {
							alertTable += " NOT NULL ";
						} else if (isnull != null && isnull != "" && "Y".equals(isnull.toUpperCase())) {
							alertTable += " NULL ";
						}
						basicJdbcUtil.executeUpdate(jc, alertTable);
					} else if ("oracle".equals(dbType)) {
						alertTable = "alter table " + tableName + " modify (" + newField;
						if (type != "") {
							alertTable += " " + type;
						}
						if (checkeIsNeedLength(type)) {
							alertTable += "(" + length + ")";
						}
						alertTable += ")";
						isexistNewField = true;
						basicJdbcUtil.executeUpdate(jc, alertTable);
						if (isnull != null && isnull != "" && "N".equals(isnull.toUpperCase())) {// 设置是否为空
							alertTable = "alter table " + tableName + " modify " + newField + " NOT NULL ";
							basicJdbcUtil.executeUpdate(jc, alertTable);
						} else if (isnull != null && isnull != "" && "Y".equals(isnull.toUpperCase())) {
							/*
							 * alertTable = "alter table " + tableName +
							 * " modify "+code+" NULL ";
							 * basicJdbcUtil.executeUpdate(jc, alertTable);
							 */
						}
					} else if ("postgres".equals(dbType)) {

						alertTable = "alter table " + tableName + " alter column \"" + newField + "\" type " + type;
						basicJdbcUtil.executeUpdate(jc, alertTable);
					}

				} else if (!fieldIsExist(jc, checkOldFieldSQL, oldField, dbType)) {// 新，旧字段不存在
					resMap.put("returnCode", "0001");
					resMap.put("msg", "原字段不存在");
					return resMap;
				} else if (oldField.equals(newField)) {// 新旧字段都一样，则只更新字段类型
					// 字段已经存在，说明数据库已经存在字段只要修改字段类型同步元数据就行
					if ("mysql".equals(dbType)) {
						alertTable = "alter table " + tableName + " modify column " + newField + " ";
						if (type != "") {
							alertTable += " " + type;
						}
						if (checkeIsNeedLength(type)) {
							alertTable += "(" + length + ")";
						}
						if (isnull != null && isnull != "" && "N".equals(isnull.toUpperCase())) {
							alertTable += " NOT NULL ";
						} else if (isnull != null && isnull != "" && "Y".equals(isnull.toUpperCase())) {
							alertTable += " NULL ";
						}
						basicJdbcUtil.executeUpdate(jc, alertTable);
					} else if ("oracle".equals(dbType)) {
						alertTable = "alter table " + tableName + " modify (" + newField;
						if (type != "") {
							alertTable += " " + type;
						}
						if (checkeIsNeedLength(type)) {
							alertTable += "(" + length + ")";
						}
						alertTable += ")";
						isexistNewField = true;
						basicJdbcUtil.executeUpdate(jc, alertTable);
						if (isnull != null && isnull != "" && "N".equals(isnull.toUpperCase())) {// 设置是否为空
							alertTable = "alter table " + tableName + " modify " + newField + " NOT NULL ";
							basicJdbcUtil.executeUpdate(jc, alertTable);
						} else if (isnull != null && isnull != "" && "Y".equals(isnull.toUpperCase())) {
							/*
							 * alertTable = "alter table " + tableName +
							 * " modify "+code+" NULL ";
							 * basicJdbcUtil.executeUpdate(jc, alertTable);
							 */
						}
					} else if ("postgres".equals(dbType)) {

						alertTable = "alter table " + tableName + " alter column \"" + newField + "\" type " + type;
						basicJdbcUtil.executeUpdate(jc, alertTable);
					}
				} else {// 旧字段存在，且新旧字段名字不一样，更新字段

					if ("mysql".equals(dbType)) {
						alertTable = "alter table " + tableName + " change " + oldField + " " + newField;
						if (type != "") {
							alertTable += " " + type;
						}
						if (checkeIsNeedLength(type)) {
							alertTable += "(" + length + ")";
						}
						if (isnull != null && isnull != "" && "N".equals(isnull.toUpperCase())) {
							alertTable += " NOT NULL ";
						} else if (isnull != null && isnull != "" && "Y".equals(isnull.toUpperCase())) {
							alertTable += " NULL ";
						}
						basicJdbcUtil.executeUpdate(jc, alertTable);
					} else if ("oracle".equals(dbType)) {
						alertTable = "alter table " + tableName + " rename column " + oldField + " to " + newField;
						basicJdbcUtil.executeUpdate(jc, alertTable);

						String alertField = "alter table " + tableName + " modify (" + newField;
						if (type != "") {
							alertField += " " + type;
						}
						if (checkeIsNeedLength(type)) {
							alertField += "(" + length + ")";
						}
						alertField += ")";
						if (isnull != null && isnull != "" && "N".equals(isnull.toUpperCase())) {
							alertTable += " NOT NULL ";
						} 
						basicJdbcUtil.executeUpdate(jc, alertField);
					} else if ("postgres".equals(dbType)) {
						alertTable = "ALTER TABLE " + tableName + " rename  \"" + oldField + "\" to \"" + newField
								+ "\"";
						basicJdbcUtil.executeUpdate(jc, alertTable);
					}
				}

				if (iskey != null && iskey != "") {// 更新主键
					if ("mysql".equals(dbType)) {
						updateMysqlPrimaryKey(jc, tableName, newField, iskey, isexistNewField);
					} else if ("oracle".equals(dbType)) {// oracle更新主键
						updateOraclePrimaryKey(jc, tableName, newField, iskey, isexistNewField);
					}

				}
				// log.debug("updateSQL = "+sqlString);

				resMap.put("returnCode", "0000");

			} else {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "表不存在");
				return resMap;
			}

		} catch (Exception e) {
			// flag = false ;

			resMap.put("returnCode", "0001");
			resMap.put("msg", "系统异常");
			throw e;
		}
		return resMap;
	}

	@Override
	public boolean updateDatabase(Metadata metadataVO, String[] propertyValue, String code) throws Exception {
		boolean flag = true;
		return flag;
	}

	/**
	 * 创建数据库
	 * 
	 * @param propertyValue
	 * @param baseName
	 * @return
	 * @throws Exception
	 */
	public boolean creatDb(String[] propertyValue, String baseName) throws Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		boolean flag = true;

		JdbcConBean jc = new JdbcConBean();
		if (propertyValue[4].equals("com.mysql.jdbc.Driver")) {
			jc.setJdbcURL(
					"jdbc:mysql://" + propertyValue[1] + "/" + baseName + "?useUnicode=true&characterEncoding=UTF-8");
		}
		// jc.setJdbcURL(propertyValue[1]);
		jc.setJdbcDriver(propertyValue[4]);
		jc.setJdbcUserName(propertyValue[2]);
		jc.setJdbcPassword(propertyValue[3]);

		// 一开始必须填一个已经存在的数据库
		try {
			basicJdbcUtil.executeUpdate(jc, "create database " + baseName);
			flag = true;

		} catch (Exception e) {
			// TODO: handle exception
			flag = false;
			throw e;
		}

		return flag;
	}

	public String changeUrlDatabaseName(String dbUrl, String databaseName) {
		String newurl = "";
		String arr[] = dbUrl.split("/");
		String arr2[] = arr[3].split("[?]");
		newurl = dbUrl.replace(arr2[0], databaseName);
		log.debug("newurl=" + newurl);
		return newurl;
	}

	

	public Map<String, String> checkConnectDatabase(Metadata metadataVO, String[] propertyValue) throws Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		Map<String, String> resMap = new HashMap<String, String>();
		JdbcConBean jc = new JdbcConBean();
		if (propertyValue == null && propertyValue.length == 0) {
			jc = databaseUtil.initDb(metadataVO);
		} else {
			Map<String, String> propertyMap = propertyValueToMap(propertyValue);
			if ("com.mysql.jdbc.Driver".equals(propertyMap.get("dbDriver"))) {
				jc.setJdbcURL("jdbc:mysql://" + propertyMap.get("dbUrl") + "/" + propertyMap.get("db_name")
						+ "?useUnicode=true&characterEncoding=UTF-8");
			} else if ("oracle.jdbc.driver.OracleDriver".equals(propertyMap.get("dbDriver"))) {
				jc.setJdbcURL("jdbc:oracle:thin:@" + propertyMap.get("dbUrl") + ":" + propertyMap.get("db_name"));
			} else if ("oracle.jdbc.driver.OracleDriver".equals(propertyMap.get("dbDriver"))) {
				jc.setJdbcURL("jdbc:oracle:thin:@" + propertyMap.get("dbUrl") + ":" + propertyMap.get("db_name"));
			} else if ("org.postgresql.Driver".equals(propertyMap.get("dbDriver"))) {
				jc.setJdbcURL("jdbc:postgresql://" + propertyMap.get("dbUrl") + "/" + propertyMap.get("db_name"));
			}
			// jc.setJdbcURL(propertyValue[1]);
			jc.setJdbcDriver(propertyMap.get("dbDriver"));
			jc.setJdbcUserName(propertyMap.get("dbUser"));
			jc.setJdbcPassword(propertyMap.get("dbPassword"));
			/*
			 * String connectTimeout =
			 * AppContextUtil.getProperties("connectTimeout") != null ?
			 * AppContextUtil.getProperties("connectTimeout") : "10000";
			 */
			String connectTimeout = "10000";
			jc.setMaxWait(Integer.parseInt(connectTimeout));
		}
Connection conn =null;
		try {
			conn= basicJdbcUtil.getCon(jc);
			if (conn == null) {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "连接数据库失败");
				return resMap;
			}
			resMap.put("returnCode", "0000");
		} catch (Exception e) {
			// TODO: handle exception
			resMap.put("returnCode", "0001");
			resMap.put("msg", "连接数据库异常");
			throw e;

		}
                finally{
                    if (conn != null) {
                        conn.close();
                    }
                }
		return resMap;

	}

	public boolean fieldIsExist(JdbcConBean jdbcConBean, String sql, String newFieldName, String dbType)
			throws SQLException, Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		List<Map<String, Object>> listField = basicJdbcUtil.select(jdbcConBean, sql);
		log.debug("newFieldName = " + newFieldName);
		if ("mysql".equals(dbType)) {
			for (int i = 0; i < listField.size(); i++) {
				Map<String, Object> maplist = listField.get(i);
				log.debug("field = " + maplist.get("Field"));
				if (newFieldName.equals(maplist.get("Field"))) {
					return true;
				}
			}
		} else if ("oracle".equals(dbType)) {
			Map map = listField.get(0);
			if (1 == Integer.parseInt(map.get("EXIST").toString())) {
				log.debug("字段存在");
				return true;
			}
		} else if ("postgres".equals(dbType)) {
			Map map = listField.get(0);
			if (1 == Integer.parseInt(map.get("exist").toString())) {
				log.debug("字段存在");
				return true;
			}
		} else {// 其他暂时不支持
		}

		return false;

	}

	public List<Map<String, Object>> getTableFieldList(JdbcConBean jdbcConBean, String sql)
			throws SQLException, Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		List<Map<String, Object>> listField = basicJdbcUtil.select(jdbcConBean, sql);
		return listField;

	}

	public Map<String, String> checkTableIsExist(Metadata metadataVO, String tableName) throws SQLException, Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		Map<String, String> resMap = new HashMap<String, String>();
		Metadata dbMetadataVO = metadataServiceImpl.selectById(metadataVO.getParentMetadata() + "");
		// String tableName = metadataVO.getMetadataCode();
		// String checkTable = "show tables like \"" + tableName + "\"";
		try {
			if (tableName == null && "".equals(tableName)) {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "表名不能为空");
				return resMap;
			}
			JdbcConBean jc = databaseUtil.initDb(dbMetadataVO);
			if (jc == null) {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "暂时不支持该类型数据库连接");
				return resMap;
			}
			String checkTable = "";
			if ("com.mysql.jdbc.Driver".equals(jc.getJdbcDriver())) {
				checkTable = "show tables like \"" + tableName + "\"";
			} else if ("oracle.jdbc.driver.OracleDriver".equals(jc.getJdbcDriver())) {
				checkTable = "select " + tableName + " from user_tables";
			}
			List<Map<String, Object>> list = basicJdbcUtil.select(jc, checkTable);

			if (list.size() > 0) {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "表已存在");
			} else {
				resMap.put("returnCode", "0000");
				return resMap;
			}

		} catch (Exception e) {
			// flag = false ;
			resMap.put("returnCode", "0001");
			resMap.put("msg", "操作数据库异常");
			throw e;
		}
		return resMap;

	}

	public Map<String, String> propertyValueToMap(String[] propertyValue) {
		Map<String, String> returnMap = new HashMap<String, String>();
		if (propertyValue != null && propertyValue.length > 0) {
			for (int i = 0; i < propertyValue.length; i++) {
				String property = propertyValue[i];
				String[] propertyArr = property.split("=");
				String key = propertyArr[0];
				String value = "";
				if (propertyArr.length > 1) {
					value = propertyArr[1];
				}
				Map<String, String> appparList = appparServiceImpl.selectAppparList("modelclass");

				if (key.equals(appparList.get("data_type"))) {
					String[] typeValue = value.split("#");
					returnMap.put("type", typeValue[0]);
				} else if (key.equals("character_maximum_length")) {
					returnMap.put("length", value);
				} else if (key.equals(appparList.get("f_isNullable"))) {
					returnMap.put("isnull", value);
				} else if (key.equals(appparList.get("fkey"))) {
					returnMap.put("isfkey", value);
				} else if (key.equals(appparList.get("f_isUnique"))) {
					returnMap.put("isUnique", value);
				} else if (key.equals(appparList.get("f_pk"))) {
					returnMap.put("iskey", value);
				} else if (key.equals(appparList.get("db_url"))) {
					returnMap.put("dbUrl", value);
				} else if (key.equals(appparList.get("db_driver"))) {
					returnMap.put("dbDriver", value);
				} else if (key.equals(appparList.get("db_user"))) {
					returnMap.put("dbUser", value);
				} else if (key.equals(appparList.get("db_password"))) {
					returnMap.put("dbPassword", value);
				} else if (key.equals(appparList.get("m_field_name"))) {
					returnMap.put("m_field_name", value);
				} else if (key.equals(appparList.get("m_field_type"))) {
					String[] typeValue = value.split("#");
					returnMap.put("type", typeValue[0]);
				} else if (key.equals(appparList.get("db_name"))) {
					returnMap.put("db_name", value);
				} else if (key.equals(appparList.get("table_name"))) {
					returnMap.put("table_name", value);
				}else if (key.equals(appparList.get("table_comment"))) {
					returnMap.put("table_comment", value);
				} else if (key.equals(appparList.get("column_name"))) {
					returnMap.put("column_name", value);
				} else if (key.equals(appparList.get("character_maximum_length"))) {
					returnMap.put("character_maximum_length", value);
				} else if (key.equals(appparList.get("column_comment"))) {
					returnMap.put("column_comment", value);
				}

			}

			return returnMap;
		}
		return null;
	}

	public Map<String, String> propertyVoToMap(Metadata metadataVO) throws Exception {
		Map<String, String> returnMap = new HashMap<String, String>();

		List<MetamodelClassproperty> propertyClassVOs = metamodelClasspropertyServiceImpl
				.selectMetamodelClasspropertyList(metadataVO.getClassId() + "");
		List<MetadataProperty> propertyVOs = metadataPropertyServiceImpl
				.selectMetaDataPropertyByMetadataId(metadataVO.getMetadataId() + "");

		Map<String, String> appparList = appparServiceImpl.selectAppparList("modelclass");

		for (int i = 0; i < propertyVOs.size(); i++) {
			for (int j = 0; j < propertyClassVOs.size(); j++) {
				if (propertyClassVOs.get(j).getPropertyCode().equals(appparList.get("fieldType"))
						&& (propertyClassVOs.get(j).getPropertyId() + "")
								.equals(propertyVOs.get(i).getClassPropertyId() + "")) {
					String value = propertyVOs.get(i).getPropertyValue();
					String[] typeValue = value.split("#");
					returnMap.put("type", typeValue[0]);
				} else if (propertyClassVOs.get(j).getPropertyCode().equals(appparList.get("fieldLongth"))
						&& (propertyClassVOs.get(j).getPropertyId() + "")
								.equals(propertyVOs.get(i).getClassPropertyId() + "")) {

					returnMap.put("length", propertyVOs.get(i).getPropertyValue());
				} else if (propertyClassVOs.get(j).getPropertyCode().equals(appparList.get("f_isNullable"))
						&& (propertyClassVOs.get(j).getPropertyId() + "")
								.equals(propertyVOs.get(i).getClassPropertyId() + "")) {
					returnMap.put("isnull", propertyVOs.get(i).getPropertyValue());
				} else if (propertyClassVOs.get(j).getPropertyCode().equals(appparList.get("f_pk"))
						&& (propertyClassVOs.get(j).getPropertyId() + "")
								.equals(propertyVOs.get(i).getClassPropertyId() + "")) {
					returnMap.put("iskey", propertyVOs.get(i).getPropertyValue());
				} else if (propertyClassVOs.get(j).getPropertyCode().equals(appparList.get("fkey"))
						&& (propertyClassVOs.get(j).getPropertyId() + "")
								.equals(propertyVOs.get(i).getClassPropertyId() + "")) {
					returnMap.put("isfkey", propertyVOs.get(i).getPropertyValue());
				} else if (propertyClassVOs.get(j).getPropertyCode().equals(appparList.get("f_isUnique"))
						&& (propertyClassVOs.get(j).getPropertyId() + "")
								.equals(propertyVOs.get(i).getClassPropertyId() + "")) {
					returnMap.put("isUnique", propertyVOs.get(i).getPropertyValue());
				} else if (propertyClassVOs.get(j).getPropertyCode().equals(appparList.get("m_field_type"))) {
					String value = propertyVOs.get(i).getPropertyValue();
					String[] typeValue = value.split("#");
					returnMap.put("type", typeValue[0]);
				}
			}

		}

		return returnMap;
	}

	/**
	 * 判断字段类型是否需要加长度
	 * 
	 * @param FieldType
	 * @return
	 */
	public boolean checkeIsNeedLength(String FieldType) {
		if (FieldType != null && ("datetime".equals(FieldType) || "date".equals(FieldType)
				|| "INT".equals(FieldType.toUpperCase()) || "INTGER".equals(FieldType.toUpperCase()))) {
			return false;
		} else if (FieldType == null) {
			return false;
		}
		return true;

	}

	public void updateOraclePrimaryKey(JdbcConBean jc, String tableName, String fieldName, String iskey,
			boolean isexistNewField) throws SQLException, Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		String alterSql = "";
		String notDelKeyStr = "";// 不删除主键字符串
		// 判断是增加主键还是删除主键
		if (iskey != null && iskey != "") {
			// 判断表是否存在主键
			List<Map<String, Object>> listPkey = basicJdbcUtil.select(jc,
					"  SELECT   *   from   user_cons_columns c where c.table_name = '" + tableName + "'");
			if (listPkey.size() > 0) {// 存在
				Object pKeyName = "";
				String keyString = "";
				for (int i = 0; i < listPkey.size(); i++) {// 遍历已有主键
					Map<String, Object> map = listPkey.get(i);
					if (map.get("POSITION") != null) {
						pKeyName = map.get("CONSTRAINT_NAME");
						keyString += map.get("COLUMN_NAME") + ",";
						if (!map.get("COLUMN_NAME").equals(fieldName)) {
							notDelKeyStr += map.get("COLUMN_NAME") + ",";
						}
					}
				}
				if (pKeyName != "") {// 删除全部主键
					basicJdbcUtil.executeUpdate(jc, "alter table " + tableName + " drop constraint " + pKeyName);
				} else {
					pKeyName = "primaryKey";
				}
				if ("Y".equals(iskey.toUpperCase())) {// 增减主键
					if (keyString == "") {
						alterSql = "  alter table " + tableName + " add constraint " + pKeyName + " primary key("
								+ fieldName + ")";
					} else {
						alterSql = "  alter table " + tableName + " add constraint " + pKeyName + " primary key("
								+ keyString + fieldName + ")";
					}
				} else {// 删除主键
					if (notDelKeyStr.length() > 0) {// 保留不是此次删除的主键
						notDelKeyStr = notDelKeyStr.substring(0, notDelKeyStr.length() - 1);
						alterSql = "  alter table " + tableName + " add constraint " + pKeyName + " primary key("
								+ notDelKeyStr + ")";
					}

				}

			} else {
				if ("Y".equals(iskey.toUpperCase())) {// 增加第一个主键
					alterSql = "  alter table " + tableName + " add constraint primaryKey primary key(" + fieldName
							+ ")";
				}
			}

			if (alterSql != "") {
				log.debug("updateOraclePrimaryKey --altersql=" + alterSql);
				basicJdbcUtil.executeUpdate(jc, alterSql);
			}

		}
	}

	public void updateMysqlPrimaryKey(JdbcConBean jc, String tableName, String fieldName, String iskey,
			boolean isexistNewField) throws SQLException, Exception {
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		String sqlString = "alter table " + tableName;
		List<Map<String, Object>> fieldList = getTableFieldList(jc, "DESCRIBE " + tableName);
		String keyString = "";
		String delKey = "";
		if (fieldList != null) {
			for (int k = 0; k < fieldList.size(); k++) {
				Map<String, Object> maplist = fieldList.get(k);
				log.debug("field = " + maplist.get("Key"));
				if ("PRI".equals(maplist.get("Key"))) {
					keyString += maplist.get("Field") + ",";
					if (!fieldName.equals(maplist.get("Field"))) {
						delKey += maplist.get("Field") + ",";
					}
				}
			}

		}
		if ("Y".equals(iskey.toUpperCase())) {

			if (keyString.length() > 0) {
				if (isexistNewField) {
					keyString = keyString.substring(0, keyString.length() - 1);
					sqlString += " drop primary key, add primary key(" + keyString + ")";
				} else {
					sqlString += " drop primary key, add primary key(" + keyString + fieldName + ")";
				}

			} else {
				sqlString += " add primary key(" + fieldName + ")";
			}
		} else {
			if (delKey.length() > 0) {
				sqlString += " drop primary key, add primary key(" + delKey.substring(0, delKey.length() - 1) + ")";
			}

		}

		basicJdbcUtil.executeUpdate(jc, sqlString);

	}

	@Override
	public Map<String, String> addPostGreCol(Metadata metadataVO, String[] fieldValue, String tableName)
			throws Exception {
		Map<String, String> retMap = new HashMap<String, String>();
		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Metadata dbMetadataVO = metadataServiceImpl.selectById(metadataVO.getParentMetadata() + "");
		// 获取表属性
		JSONObject tableJsonObject = new JSONObject();
		List<Map<String, Object>> tablesList = iVMetadataPropertyServiceImpl
				.queryPropertyByMatedataId(metadataVO.getParentMetadata());
		if (tablesList.size() > 0) {
			tableJsonObject.putAll(tablesList.get(0));
		}
		String addTablesql = "";
		JdbcConBean jc = databaseUtil.initDb(dbMetadataVO);
		// 查询表是否存在
		String checkTableSQL = "select count(*) from information_schema.tables where  table_name ='"
				+ metadataVO.getMetadataCode() + "'";
		list = basicJdbcUtil.select(jc, checkTableSQL);
		// log.debug("111="+list.get(0));
		Map map = list.get(0);
		if (1 == Integer.parseInt(map.get("count").toString())) {
			retMap.put("returnCode", "0000");
			return retMap;
		}

		String mongodbName = getMongodbName(dbMetadataVO);
		String mongoServer = jc.getMongoServer();// mongo_server
		if (mongoServer == null || "".equals(mongoServer)) {
			retMap.put("returnCode", "0001");
			retMap.put("msg", "外部服务器未配置，请到库级元数据上配置");
			return retMap;
		}
		if (fieldValue != null) {
			addTablesql = "CREATE FOREIGN TABLE \"public\".\"" + metadataVO.getMetadataCode() + "\"(\"_id\" name,"
					+ "\"" + fieldValue[0] + "\" " + fieldValue[1] + ")";
		} else {
			addTablesql = "CREATE FOREIGN TABLE \"public\".\"" + metadataVO.getMetadataCode() + "\"(\"_id\" name)";
		}

		addTablesql += " SERVER \"" + mongoServer + "\"";
		addTablesql += " OPTIONS (\"database\" '" + mongodbName + "', \"collection\" '" + metadataVO.getMetadataCode()
				+ "')";
		basicJdbcUtil.executeUpdate(jc, addTablesql);

		String alertTablesql1 = "ALTER FOREIGN TABLE \"public\".\"" + metadataVO.getMetadataCode() + "\" OWNER TO \""
				+ dbMetadataVO.getMetadataCode() + "\"";
		basicJdbcUtil.executeUpdate(jc, alertTablesql1);
		String alertTablesql2 = "ALTER FOREIGN TABLE \"public\".\"" + metadataVO.getMetadataCode()
				+ "\" SET WITHOUT OIDS";
		basicJdbcUtil.executeUpdate(jc, alertTablesql2);
		retMap.put("returnCode", "0000");
		return retMap;
	}

	public String getMongodbName(Metadata metadataVO) throws Exception {
		String mongodbName = "";
		String code = appparServiceImpl.selectAppparList("modelclass", "mongodbName");
		List<MetadataProperty> list = metadataPropertyServiceImpl.queryMetaDataPropertyByMetadataIdAndPropertyCode1(
				metadataVO.getMetadataId(), code, metadataVO.getClassId());
		if (list != null) {
			mongodbName = list.get(0).getPropertyValue();
		}
		return mongodbName;

	}

	@Override
	public Map<String, String> createIndexTables(Metadata metadataVO, String[] dimFieids, String[] dimParentFieids,
			String[] ruleFieids, String[] ruleParentFieids) throws Exception {

		BasicJdbcUtil basicJdbcUtil = BasicJdbcUtil.getInstance();
		Map<String, String> resMap = new HashMap<String, String>();
		Metadata dbMetadataVO = metadataServiceImpl.selectById(metadataVO.getParentMetadata() + "");
		String tableName = metadataVO.getMetadataCode();

		try {
			JdbcConBean jc = databaseUtil.initDb(dbMetadataVO);

			if (jc == null) {
				resMap.put("returnCode", "0001");
				resMap.put("msg", "暂时不支持该类型数据库操作");
				return resMap;
			}

			String checkTableSQL = "";
			String checkFieldSQL = "";
			String dbType = "";
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			boolean tableExist = false;
			if ("com.mysql.jdbc.Driver".equals(jc.getJdbcDriver())) {
				checkTableSQL = "show tables like \"" + tableName + "\"";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				if (list.size() > 0) {
					tableExist = true;
				}
				dbType = "mysql";
			} else if ("oracle.jdbc.driver.OracleDriver".equals(jc.getJdbcDriver())) {
				tableName = tableName.toUpperCase();
				checkTableSQL = "select count(*) as EXIST from user_tables where table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				// log.debug("111="+list.get(0));
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("EXIST").toString())) {
					tableExist = true;
				}
				dbType = "oracle";
			} else if ("org.postgresql.Driver".equals(jc.getJdbcDriver())) {
				// 查询表是否存在
				checkTableSQL = "select count(*) from information_schema.tables where  table_name ='" + tableName + "'";
				list = basicJdbcUtil.select(jc, checkTableSQL);
				// log.debug("111="+list.get(0));
				Map map = list.get(0);
				if (1 == Integer.parseInt(map.get("count").toString())) {
					tableExist = true;
				}
				dbType = "postgres";
			}

			// 判断表是否存在
			if (!tableExist) {

				String createSql = "create table " + tableName + "(run_time timestamp(0) default CURRENT_TIMESTAMP,";
				if (dimFieids != null && dimFieids.length > 0) {
					for (int i = 0; i < dimFieids.length; i++) {
						createSql += dimParentFieids[i] + "_" + dimFieids[i] + " varchar(255) default null,";
					}
				}

				if (ruleFieids != null && ruleFieids.length > 0) {
					for (int i = 0; i < ruleFieids.length; i++) {
						createSql += "result_" + ruleParentFieids[i] + "_" + ruleFieids[i]
								+ " varchar(255) default null,";
					}
				}

				// 去除"，"
				createSql = createSql.substring(0, createSql.length() - 1);

				createSql += ")";

				log.debug(createSql);

				if (createSql != "") {
					basicJdbcUtil.executeUpdate(jc, createSql);
					log.debug("----------------create table success!---------------");
				}
			}

		} catch (Exception e) {
			resMap.put("returnCode", "0001");
			resMap.put("msg", "操作数据库失败");
			throw e;
		}
		return resMap;
	}

	@Override
	public Metadata getMetadataVO(String metadataId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> addCheckDatabaseOperator(Metadata metadataVO, String[] propertyArr) {
		/* 判断是否增加元数据为数据库 */
		Map<String, String> appparList = appparServiceImpl.selectAppparList("modelclass");
		Map<String, String> propertyMap = propertyValueToMap(propertyArr);// 属性map
		Map<String, String> resMap = new HashMap<String, String>();
		try {

			MetamodelClass mvo = metamodelClassServiceImpl.queryModelClassByClassId(metadataVO.getClassId());
			if ((appparList.get("db")).equals(mvo.getClassCode())) {// 增加数据库
				// 查看配置的数据库连接是否存在
				resMap = checkConnectDatabase(metadataVO, propertyArr);
			} else if ((appparList.get("table")).equals(mvo.getClassCode())) {// 增加表
				if (NumberUtil.checkStrIsNumber(propertyMap.get("table_name"))) {
					log.debug("表名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("returnCode", "0001");
					resMap.put("msg", "表名不能为纯数字或者数字开头，请重新输入!");
					return resMap;
				}
				resMap = checkTableIsExist(metadataVO, propertyMap.get("tabel_name"));
			} else if ((appparList.get("field")).equals(mvo.getClassCode())) {// 增加字段
				if (NumberUtil.checkStrIsNumber(propertyMap.get("column_name"))) {
					log.debug("字段名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("msg", "字段名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("returnCode", "0001");
					return resMap;
				}

				resMap = addTableColumn(metadataVO, propertyArr);
			} else if ((appparList.get("key")).equals(mvo.getClassCode())) {// 增加主键

			} else if ((appparList.get("fkey")).equals(mvo.getClassCode())) {// 增加外键

			} else if ((appparList.get("pgdb")).equals(mvo.getClassCode())) {// 增加postgre
				if (NumberUtil.checkStrIsNumber(propertyMap.get("db_name"))) {
					log.debug("表名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("returnCode", "0001");
					resMap.put("msg", "表名不能为纯数字或者数字开头，请重新输入!");
					return resMap;
				}
				resMap = checkConnectDatabase(metadataVO, propertyArr);
			} else if ((appparList.get("pgCol")).equals(mvo.getClassCode())) {// 增加postgre集合
				if (NumberUtil.checkStrIsNumber(propertyMap.get("table_name"))) {
					log.debug("表名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("returnCode", "0001");
					resMap.put("msg", "表名不能为纯数字或者数字开头，请重新输入!");
					return resMap;
				}
			} else if ((appparList.get("pgField")).equals(mvo.getClassCode())) {// 增加postgre字段
				if (NumberUtil.checkStrIsNumber(propertyMap.get("column_name"))) {
					log.debug("表名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("returnCode", "0001");
					resMap.put("msg", "表名不能为纯数字或者数字开头，请重新输入!");
					return resMap;
				}
				resMap = addTableColumn(metadataVO, propertyArr);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resMap.put("returnCode", "0001");
			resMap.put("msg", "操作异常!");
			return resMap;
		}

		return resMap;

	}

	@Override
	public Map<String, String> updateCheckDatabaseOperator(Metadata metadataVO, String[] propertyArr) {
		/* 判断是否修改库操作 */
		
		Map<String, String> resMap = new HashMap<String, String>();
		try {
			Map<String, String> appparList = appparServiceImpl.selectAppparList("modelclass");
			Map<String, String> propertyMap = propertyValueToMap(propertyArr);// 属性map

			if (propertyMap == null ) {
				resMap.put("msg", "请输入属性信息!");
				resMap.put("returnCode", "0001");
				return resMap;
			}
			// 判断是否修改数据库或者表或者表字段
			MetamodelClass mvo = metamodelClassServiceImpl.queryModelClassByClassId(metadataVO.getClassId());
			if (appparList.get("db").equals(mvo.getClassCode()) && propertyArr != null) {// 修改数据库
				String bd_name = propertyMap.get("db_name");
				if (StringUtil.isEmpty(bd_name)) {
					log.debug("库名为空，请输入!");
					resMap.put("msg", "库名为空，请输入!");
					resMap.put("returnCode", "-1");
					return resMap;
				}
				if (NumberUtil.checkStrIsNumber(bd_name)) {
					log.debug("库名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("msg", "库名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("returnCode", "-1");
					return resMap;
				}
				resMap = checkConnectDatabase(metadataVO, propertyArr);

			} else if ((appparList.get("table")).equals(mvo.getClassCode())) {// 修改表
				String table_name = propertyMap.get("table_name");
				if (StringUtil.isEmpty(table_name)) {
					log.debug("表名为空，请输入!");
					resMap.put("msg", "表名为空，请输入!");
					resMap.put("returnCode", "-1");
					return resMap;
				}
				if (NumberUtil.checkStrIsNumber(table_name)) {
					log.debug("表名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("msg", "表名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("returnCode", "-1");
					return resMap;
				}
				
				resMap = updateTable(metadataVO, table_name);
			} else if ((appparList.get("field")).equals(mvo.getClassCode())) {// 修改字段
				String column_name = propertyMap.get("column_name");
				if (StringUtil.isEmpty(column_name)) {
					log.debug("字段名为空，请输入!");
					resMap.put("msg", "字段名为空，请输入!");
					resMap.put("returnCode", "-1");
					return resMap;
				}
				if (NumberUtil.checkStrIsNumber(column_name)) {
					log.debug("字段名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("msg", "字段名不能为纯数字或者数字开头，请重新输入!");
					resMap.put("returnCode", "-1");
					return resMap;
				}
				
				resMap = updateTableColumn(metadataVO, propertyArr);

			} else if (appparList.get("key").equals(mvo.getClassCode())) {// 修改主键

			} else if (appparList.get("fkey").equals(mvo.getClassCode())) {// 修改外键

			}

		} catch (Exception e) {
			// TODO: handle exception
			resMap.put("returnCode", "-2");
			resMap.put("msg", "操作失败!");
			e.printStackTrace();
		}
		return resMap;
	}
}
