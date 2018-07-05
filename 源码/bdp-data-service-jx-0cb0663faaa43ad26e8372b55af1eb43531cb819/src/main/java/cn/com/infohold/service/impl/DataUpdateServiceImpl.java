package cn.com.infohold.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bson.BsonDocument;
import org.bson.Document;
import org.springframework.stereotype.Service;

import bdp.commons.dataservice.config.ConditionBean;
import bdp.commons.dataservice.config.ConfigureBean;
import bdp.commons.dataservice.param.UpdateBean;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.metadata.ret.MetaData;
import cn.com.infohold.basic.util.jdbc.BasicJdbcUtil;
import cn.com.infohold.basic.util.jdbc.JdbcConBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.basic.util.mongo.MongoDbBean;
import cn.com.infohold.basic.util.mongo.MongoDbUtil;
import cn.com.infohold.service.IDataUpdateService;
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
public class DataUpdateServiceImpl implements IDataUpdateService {

	private static final MongoDbUtil mongo = MongoDbUtil.getInstance();
	private static final MongoDbBean staticMongoBean = new MongoDbBean();
	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();
	private static final JdbcConBean bean = new JdbcConBean();
	private static String LocalIP = "";
	private static String LocalHostname = "";

	@Override
	public RetBean updateByJson(UpdateBean updateBean) throws Exception {
		RetBean result = new RetBean();
		result.setRet_code("0");
		try {
			String metadataTableCode = updateBean.getTableCode();
			MetaData metadata = MetadataUtil.getMetadataBymetadataCode(metadataTableCode,"a");// 元数据库表属性信息
			MetaData parent = metadata.getParent();
			Map<String, Object> propertyMap = new HashMap<String, Object>();
			propertyMap.putAll(metadata.getProperty());
			propertyMap.putAll(parent.getProperty());
			if ("mongo".equals(propertyMap.get("database_type"))) {
				updateAnalysis(updateBean, metadata);
			} else {
				updateAnalysisSql(updateBean, propertyMap);
			}
		} catch (Exception ex) {
			result.setRet_code("-1");
			result.setRet_message(ex.getLocalizedMessage());
			throw ex;
		}
		return result;
	}

	private void updateAnalysisSql(UpdateBean updateBean, Map<String, Object> dataBaseInfoMap)
			throws SQLException, Exception {
		String tableName = dataBaseInfoMap.get("table_name").toString();

		BasicJdbcUtil jdbcUtil = BasicJdbcUtil.getInstance();

		JdbcConBean jdbcConBean = AnalysisUtil.setJdbcBeanSql(dataBaseInfoMap);
		Map<String, Object> data = updateBean.getData();
		List<String> sets = new ArrayList<String>();
		for (String key : data.keySet()) {
			sets.add(key + "='" + data.get(key) + "'");
		}

		String conds = AnalysisUtil.conditionMosaicToSql(updateBean.getConditions());
		if (StringUtil.isNotEmpty(conds)) {
			conds = "  " + conds;
		}
		if (sets.size() > 0) {
			String sql = "UPDATE " + tableName + " SET   " + StringUtils.join(sets, ",") + " where  " + conds;
			List<Object> objects = new ArrayList<Object>();
			jdbcUtil.executeUpdate(jdbcConBean, sql, objects.toArray());
		}
	}

	private void updateAnalysis(UpdateBean updateBean, MetaData metadata) throws Exception {
		Map<String, Object> field = updateBean.getData();// 修改的字段
		List<ConditionBean> conds = updateBean.getConditions();// 条件
		MetadataUtil.getMongoDbBean(metadata, staticMongoBean);
		String table = metadata.getProperty().get("m_col").toString();

		Document document = new Document();
		document.putAll(field);

		ConfigureBean configureBean = AnalysisUtil.getConfigureBean();
		BsonDocument filter = AnalysisUtil.conditionMosaicToBson(conds, configureBean.getConds());
		mongo.updateMany(staticMongoBean, table, filter, new Document("$set", document));
	}
}
