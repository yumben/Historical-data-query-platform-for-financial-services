package cn.com.infohold.service.impl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bson.Document;
import org.springframework.stereotype.Service;

import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.param.InsertBean;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.metadata.ret.MetaData;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.basic.util.jdbc.BasicJdbcUtil;
import cn.com.infohold.basic.util.jdbc.JdbcConBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.basic.util.mongo.MongoDbBean;
import cn.com.infohold.basic.util.mongo.MongoDbUtil;
import cn.com.infohold.service.IDataInsertService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.AnalysisUtil;
import cn.com.infohold.util.MetadataUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
@Service
@Log4j2
public class DataInsertServiceImpl implements IDataInsertService {

	private static final MongoDbUtil mongo = MongoDbUtil.getInstance();
	private static final MongoDbBean staticMongoBean = new MongoDbBean();
	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();
	private static final JdbcConBean bean = new JdbcConBean();
	private static String LocalIP = "";
	private static String LocalHostname = "";

	@Override
	public RetBean insertByJson(InsertBean insertBean) throws Exception {
		// InsertBean insertBean = AnalysisUtil.toQueryBean(json,
		// InsertBean.class);
		RetBean result = new RetBean();
		result.setRet_code("0");
		try {
			String metadataTableCode = insertBean.getTableCode();
			MetaData metadata = MetadataUtil.getMetadataBymetadataCode(metadataTableCode, "a");// 元数据库表属性信息

			MetaData parent = metadata.getParent();
			Map<String, Object> propertyMap = new HashMap<String, Object>();
			if (null != metadata.getProperty()) {
				propertyMap.putAll(metadata.getProperty());
			}
			if (null != parent.getProperty()) {
				propertyMap.putAll(parent.getProperty());
			}

			if ("mongo".equals(propertyMap.get("db_type"))) {
				insertAnalysis(insertBean, metadata);
			} else {
				insertAnalysisSql(insertBean, propertyMap);
			}
		} catch (Exception ex) {
			result.setRet_code("-1");
			result.setRet_message(ex.getLocalizedMessage());
			throw ex;
		}
		return result;
	}

	private void insertAnalysisSql(InsertBean insertBean, Map<String, Object> dataBaseInfoMap)
			throws SQLException, Exception {
		String tableName = null != dataBaseInfoMap.get("table_name") ? dataBaseInfoMap.get("table_name").toString()
				: "";
		if (StringUtil.isNotEmpty(tableName)) {
			BasicJdbcUtil jdbcUtil = BasicJdbcUtil.getInstance();

			JdbcConBean jdbcConBean = AnalysisUtil.setJdbcBeanSql(dataBaseInfoMap);

			List<Map<String, Object>> data = insertBean.getData();
			List<String> fields = new ArrayList<String>();
			List<Object> valuesList = new ArrayList<Object>();
			int count = 1;//计数
			StringBuffer valuekeyBuffer  = new StringBuffer();
			StringBuffer valueBuffer  = new StringBuffer();
			for (Map<String, Object> map : data) {
				
				List<String> values = new ArrayList<String>();
				for (String key : map.keySet()) {
					if (count == 1) {
						fields.add(key);
					}
					values.add("?");
					valuesList.add(map.get(key));
				}
				valuekeyBuffer.append("("+ StringUtils.join(values, ",") + "),");
			
				count++;
			}
			if (valuekeyBuffer!=null && valueBuffer!=null) {
				String valueskey = valuekeyBuffer.substring(0, valuekeyBuffer.length()-1);
				String sql = "INSERT INTO " + tableName + " (" + StringUtils.join(fields, ",") + ") VALUES  "
						+ valueskey ;
				log.debug("sql====" + sql);
				jdbcUtil.executeUpdate(jdbcConBean, sql, valuesList.toArray());
			}
		}
	}

	private void insertAnalysis(InsertBean insertBean, MetaData metadata) throws Exception {
		List<Map<String, Object>> fields = insertBean.getData();// 新增的字段
		MetadataUtil.getMongoDbBean(metadata, staticMongoBean);
		String table = metadata.getProperty().get("table_name").toString();

		List<Document> documents = new ArrayList<Document>(fields.size());
		for (Map<String, Object> map : fields) {
			Document document = new Document();
			document.putAll(map);
			documents.add(document);
		}
		mongo.batchInsert(staticMongoBean, table, documents);
		documents.clear();
	}

	@Override
	public RetBean insertBatch(ExecuteBatchSqlBean executeBatchSqlBean) throws IOException, Exception {
		RetBean result = new RetBean();
		result.setRet_code("0");
		try {
			if (executeBatchSqlBean != null && executeBatchSqlBean.getInsertBeans() != null
					&& executeBatchSqlBean.getInsertBeans().size() > 0) {
				for (InsertBean insertBean : executeBatchSqlBean.getInsertBeans()) {
					result = insertByJson(insertBean);
				}
			}
		} catch (Exception ex) {
			result.setRet_code("-1");
			result.setRet_message(ex.getLocalizedMessage());
			throw ex;
		}
		return result;
	}

	@Override
	public RetBean insertAuth() throws IOException, Exception {
		
		Map<String, Object> dataBaseInfoMap = new HashMap<String, Object>();
		BasicJdbcUtil jdbcUtil = BasicJdbcUtil.getInstance();
		dataBaseInfoMap.put("db_type", PropUtil.getProperty("db_type"));
		dataBaseInfoMap.put("db_driver", PropUtil.getProperty("db_driver"));
		dataBaseInfoMap.put("db_encoding", PropUtil.getProperty("db_encoding"));
		dataBaseInfoMap.put("db_name", PropUtil.getProperty("db_name"));
		dataBaseInfoMap.put("db_user", PropUtil.getProperty("db_user"));
		dataBaseInfoMap.put("db_port", PropUtil.getProperty("db_port"));
		dataBaseInfoMap.put("db_host", PropUtil.getProperty("db_host"));
		dataBaseInfoMap.put("db_password", PropUtil.getProperty("db_password"));
		log.debug("导入开始" + dataBaseInfoMap);
		
		JdbcConBean jdbcConBean = AnalysisUtil.setJdbcBeanSql(dataBaseInfoMap);
		String user_resource_sql = "SELECT '1' as user_type, user_id as user_id, resource_business_id as resource_business_id FROM auth_resource, auth_user";
		List<Map<String, Object>> user_resource_list = jdbcUtil.select(jdbcConBean, user_resource_sql, new Object[] {});

		String drop_auth_privilege_operation_Sql = "truncate table auth_privilege_operation";
		String drop_auth_privilege_Sql = "truncate table auth_privilege";
		
//		log.info("privilege====" + privilegeSql);
		jdbcUtil.executeUpdate(jdbcConBean, drop_auth_privilege_operation_Sql, new Object[] {});
		jdbcUtil.executeUpdate(jdbcConBean, drop_auth_privilege_Sql, new Object[] {});
		
		Long start = 0l;
		Long end = 0l;
		int i = 0 ;
		for (Map<String, Object> map : user_resource_list) {
			if (0 == start.intValue()) {
				start = System.currentTimeMillis();
			}
			String user_type = map.get("user_type") + "";
			String user_id = map.get("user_id") + "";
			String resource_business_id = map.get("resource_business_id") + "";
			String privilegeUUID = UUID.randomUUID().toString();
			String operationUUID1 = UUID.randomUUID().toString();
			String operationUUID2 = UUID.randomUUID().toString();
			
			String operationExport = "ae6d1e12-3053-4ed6-b19e-3e79334eff57";
			String operationSearch = "b04de815-ed14-4206-ac92-8cf72d577f07";
			
			String privilegeSql = "INSERT INTO auth_privilege ( privilege_id, privilege_master_type, privilege_master_id, resource_id ) VALUES ( '"
					+ privilegeUUID + "', '" + user_type + "','" + user_id + "','" + resource_business_id + "')";
//			log.info("privilege====" + privilegeSql);
			jdbcUtil.executeUpdate(jdbcConBean, privilegeSql, new Object[] {});
			
			String operationSql1 = "INSERT INTO auth_privilege_operation ( privilege_operation_id, resource_id, operation_id, privilege_id ) VALUES ( '"
					+ operationUUID1 + "', '" + resource_business_id + "','" + operationExport + "','" + privilegeUUID + "'),( '"
					+ operationUUID2 + "', '" + resource_business_id + "','" + operationSearch + "','" + privilegeUUID + "')";
//			log.info("privilege====" + privilegeSql);
			jdbcUtil.executeUpdate(jdbcConBean, operationSql1, new Object[] {});
			
			if (i == 1000) {
				end = System.currentTimeMillis();
				log.debug("1000次循环耗时 {}" ,(end-start));
				start = 0l;
				end = 0l;
				i = 0;
			}
			
			i++;
//			String operationSql2 = "INSERT INTO auth_privilege_operation ( privilege_operation_id, resource_id, operation_id, privilege_id ) VALUES ( '"
//					+ operationUUID2 + "', '" + resource_business_id + "','" + operationSearch + "','" + privilegeUUID + "')";
//			log.info("privilege====" + privilegeSql);
//			jdbcUtil.executeUpdate(jdbcConBean, operationSql2, new Object[] {});
		}
		System.out.println(1);
		return null;
	}

}
