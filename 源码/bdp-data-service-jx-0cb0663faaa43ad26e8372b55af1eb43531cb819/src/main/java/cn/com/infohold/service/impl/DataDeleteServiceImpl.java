package cn.com.infohold.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BsonDocument;
import org.springframework.stereotype.Service;

import bdp.commons.dataservice.config.ConditionBean;
import bdp.commons.dataservice.config.ConfigureBean;
import bdp.commons.dataservice.param.DeteleBean;
import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.metadata.ret.MetaData;
import cn.com.infohold.basic.util.jdbc.BasicJdbcUtil;
import cn.com.infohold.basic.util.jdbc.JdbcConBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.basic.util.mongo.MongoDbBean;
import cn.com.infohold.basic.util.mongo.MongoDbUtil;
import cn.com.infohold.service.IDataDeleteService;
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
public class DataDeleteServiceImpl implements IDataDeleteService {

	private static final MongoDbUtil mongo = MongoDbUtil.getInstance();

	private static final MongoDbBean staticMongoBean = new MongoDbBean();
	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();
	private static final JdbcConBean bean = new JdbcConBean();
	private static String LocalIP = "";
	private static String LocalHostname = "";
	private static final BasicJdbcUtil jdbcUtil = BasicJdbcUtil.getInstance();
	
	@Override
	public RetBean deleteByJson(DeteleBean deleteBean) throws Exception {
		RetBean result = new RetBean();
		result.setRet_code("0");
		try {
			String metadataTableCode = deleteBean.getTableCode();
			MetaData metadata = MetadataUtil.getMetadataBymetadataCode(metadataTableCode,"a");// 元数据库表属性信息
			MetaData parent = metadata.getParent();
			Map<String, Object> propertyMap = new HashMap<String, Object>();
			propertyMap.putAll(metadata.getProperty());
			propertyMap.putAll(parent.getProperty());
			if ("mongo".equals(propertyMap.get("database_type"))) {
				deleteAnalysis(deleteBean, metadata);
			} else {
				deleteAnalysisSql(deleteBean, propertyMap);
			}

		} catch (Exception ex) {
			result.setRet_code("-1");
			result.setRet_message(ex.getLocalizedMessage());
			throw ex;
		}
		return result;
	}

	private void deleteAnalysisSql(DeteleBean deleteBean, Map<String, Object> dataBaseInfoMap) throws Exception {
		String tableName = dataBaseInfoMap.get("table_name").toString();

		BasicJdbcUtil jdbcUtil = BasicJdbcUtil.getInstance();

		JdbcConBean jdbcConBean = AnalysisUtil.setJdbcBeanSql(dataBaseInfoMap);
		String sql = "delete from " + tableName + " where ";

		String conds = AnalysisUtil.conditionMosaicToSql(deleteBean.getConditions());
		if (StringUtil.isNotEmpty(conds)) {
			conds = "  " + conds;
		}
		sql = sql + conds;
		List<Object> objects = new ArrayList<Object>();
		int result = jdbcUtil.executeUpdate(jdbcConBean, sql, objects.toArray());
	}

	private void deleteAnalysis(DeteleBean deleteBean, MetaData metadata) throws Exception {
		List<ConditionBean> conds = deleteBean.getConditions();// 条件
		MetadataUtil.getMongoDbBean(metadata, staticMongoBean);
		String table = metadata.getProperty().get("m_col").toString();

		ConfigureBean configureBean = AnalysisUtil.getConfigureBean();
		BsonDocument filter = AnalysisUtil.conditionMosaicToBson(conds, configureBean.getConds());
		mongo.deleteMany(staticMongoBean, table, filter);
	}

	@Override
	public RetBean deleteBatch(ExecuteBatchSqlBean executeBatchSqlBean) throws Exception {
		RetBean result = new RetBean();
		result.setRet_code("0");
		try {
			if (null!=executeBatchSqlBean.getDeteleBeans()) {
				for (DeteleBean deteleBean : executeBatchSqlBean.getDeteleBeans()) {
					String metadataTableCode = deteleBean.getTableCode();
					MetaData metadata = MetadataUtil.getMetadataBymetadataCode(metadataTableCode,"a");// 元数据库表属性信息
					MetaData parent = metadata.getParent();
					Map<String, Object> propertyMap = new HashMap<String, Object>();
					propertyMap.putAll(metadata.getProperty());
					propertyMap.putAll(parent.getProperty());
					if ("mongo".equals(propertyMap.get("database_type"))) {
						deleteAnalysis(deteleBean, metadata);
					} else {
						deleteAnalysisSql(deteleBean, propertyMap);
					}
				}
			}
			//语句删除
			if(null!=executeBatchSqlBean.getExecuteBySqlBeans()){
				
	            for (ExecuteBySqlBean executeBySqlBean : executeBatchSqlBean.getExecuteBySqlBeans()) {
	            	String db_code = executeBySqlBean.getDb_code();
		            MetaData metadata = MetadataUtil.getMetadataBymetadataCode(db_code, "i"); // 元数据库表属性信息
		            if (null == metadata) {
		                result.setRet_code("-1");
		                result.setRet_message("元数据不存在！");
		                return result;
		            }
		            Map<String, Object> propertyMap = new HashMap<String, Object>();
		            propertyMap.putAll(metadata.getProperty());
		            JdbcConBean jdbcConBean = AnalysisUtil.setJdbcBeanSql(propertyMap);
		            String sql = executeBySqlBean.getSql();
	                jdbcUtil.executeUpdate(jdbcConBean, sql, executeBySqlBean.getObjects());
	            }
			}
			

		} catch (Exception ex) {
			result.setRet_code("-1");
			result.setRet_message(ex.getLocalizedMessage());
			throw ex;
		}
		return result;
	}
}
