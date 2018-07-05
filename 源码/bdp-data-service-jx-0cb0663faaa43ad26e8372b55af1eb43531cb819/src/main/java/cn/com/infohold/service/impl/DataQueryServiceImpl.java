package cn.com.infohold.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.mongodb.MongoClient;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;

import bdp.commons.dataservice.bean.SqlBean;
import bdp.commons.dataservice.config.ConfigureBean;
import bdp.commons.dataservice.config.ConfigureBeanConds;
import bdp.commons.dataservice.config.ConfigureBeanFunctions;
import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.param.QueryBeanField;
import bdp.commons.dataservice.param.QueryBeanGroup;
import bdp.commons.dataservice.param.QueryBeanOrder;
import bdp.commons.dataservice.param.QuerySummary;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.metadata.ret.MetaData;
import cn.com.infohold.basic.util.common.DateUtil;
import cn.com.infohold.basic.util.file.PropUtil;
import cn.com.infohold.basic.util.jdbc.BasicJdbcUtil;
import cn.com.infohold.basic.util.jdbc.JdbcConBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.basic.util.mongo.MongoDbBean;
import cn.com.infohold.basic.util.mongo.MongoDbUtil;
import cn.com.infohold.service.IDataQueryService;
import cn.com.infohold.tools.util.StringUtil;
import cn.com.infohold.util.AnalysisUtil;
import cn.com.infohold.util.MetadataUtil;
import cn.com.infohold.util.SqlBeanHelper;
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
public class DataQueryServiceImpl implements IDataQueryService {

	private static final BasicJsonUtil BJU = BasicJsonUtil.getInstance();
	private static final MongoDbUtil mongo = MongoDbUtil.getInstance();
	private static final BasicJdbcUtil jdbcUtil = BasicJdbcUtil.getInstance();
	private static final MongoDbBean staticMongoBean = new MongoDbBean();
	private static final ForkJoinPool FJP = AnalysisUtil.getForkJoinPool();
	// private static final JdbcConBean bean = new JdbcConBean();
	private static String LocalIP = "";
	private static String LocalHostname = "";
	private static int lock = 0;

	static {
		String connectionsPerHosts = PropUtil.getProperty("connectionsPerHost");
		String threadsAllowedToBlockForConnectionMultipliers = PropUtil
				.getProperty("threadsAllowedToBlockForConnectionMultiplier");
		Integer connectionsPerHost = 200;
		Integer threadsAllowedToBlockForConnectionMultiplier = 200;
		try {
			connectionsPerHost = Integer.parseInt(connectionsPerHosts);
			threadsAllowedToBlockForConnectionMultiplier = Integer
					.parseInt(threadsAllowedToBlockForConnectionMultipliers);
		} catch (Exception ex) {
			log.warn(" connectionsPerHosts=" + connectionsPerHost);
		}
		log.debug("connectionsPerHost=" + connectionsPerHost);

		staticMongoBean.setThreadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier);
		staticMongoBean.setConnectionsPerHost(connectionsPerHost);
		try {
			InetAddress host = InetAddress.getLocalHost();
			LocalIP = host.getHostAddress();
			LocalHostname = host.getHostName();
		} catch (Exception ex) {
			System.out.println("LocalIP=" + LocalIP + "," + ex.getLocalizedMessage());
		}
	}

	public MongoDbBean getMongoBean() {
		return staticMongoBean;
	}

	public MongoClient getMongoClient(MongoDbBean staticMongoBean) throws Exception {
		// MongoClient mongoClient = null;
		// if (1 == lock) {
		// lock = 0;
		// log.debug("从节点==============");
		// mongoClient = mongo.connectPoolSecondaryPreferred(staticMongoBean);
		// } else {
		// log.debug("主节点===============");
		// lock = 1;
		// mongoClient = mongo.connectPool(staticMongoBean);
		// }
		// return mongoClient;
		MongoClient mongoClient = null;
		Random random = new Random();
		int r = random.nextInt(2) % (2 - 1 + 1) + 1;
		if (1 == r) {
			log.debug("从节点==============");
			mongoClient = mongo.connectPoolSecondaryPreferred(staticMongoBean);
		} else {
			log.debug("主节点===============");
			mongoClient = mongo.connectPool(staticMongoBean);
		}
		return mongoClient;
	}

	@SuppressWarnings("static-access")
	public Long getCountNoGroup(BsonDocument filter, MongoDbBean mongoBean, String table, QueryBean queryBean)
			throws Exception {
		Long count = new Long(0);
		long start = System.currentTimeMillis();
		if (filter == null) {
			filter = new BsonDocument();
		} else {
			filter = (BsonDocument) filter.get("$match");
		}
		MongoClient mongoClient = getMongoClient(staticMongoBean);
		count = mongo.countFind(mongoClient, mongoBean.getDatabaseName(), table, filter);
		long end = System.currentTimeMillis();
		log.debug("query getCountNoGroup use time  " + (end - start) + " ms");
		log.debug("count=" + count);
		return count;
	}

	/**
	 * 分组count
	 * 
	 * @param filter
	 * @param group
	 * @param having
	 * @param mongoBean
	 * @param table
	 * @param queryBean
	 * @return
	 * @throws Exception
	 */
	public Long getCountHaveGroup(BsonDocument filter, List<Bson> group, BsonDocument having, MongoDbBean mongoBean,
			String table, QueryBean queryBean) throws Exception {
		Long count = new Long(0);
		long start = System.currentTimeMillis();
		List<Bson> countParam = new ArrayList<Bson>();

		if (filter != null && !filter.isEmpty()) {
			countParam.add(filter);
		}
		if (group != null && !group.isEmpty()) {
			countParam.addAll(group);
		}
		if (having != null && !having.isEmpty()) {
			countParam.add(having);
		}
		MongoClient mongoClient = getMongoClient(staticMongoBean);
		count = mongo.countAggregates(mongoClient, mongoBean.getDatabaseName(), table, countParam);

		long end = System.currentTimeMillis();
		log.debug("query getCountNoGroup use time  {} {} ", (end - start), " ms");
		log.debug("count=" + count);
		return count;
	}

	/**
	 * 无分组count
	 * 
	 * @param filter
	 * @param group
	 * @param having
	 * @param mongoBean
	 * @param table
	 * @param queryBean
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public Long getCountHaveGroup(BsonDocument filter, BsonDocument group, BsonDocument having, MongoDbBean mongoBean,
			String table, QueryBean queryBean) throws Exception {
		Long count = new Long(0);
		long start = System.currentTimeMillis();
		List<Bson> countParam = new ArrayList<Bson>();

		if (filter != null && !filter.isEmpty()) {
			countParam.add(filter);
		}
		if (group != null && !group.isEmpty()) {
			countParam.add(group);
		}
		if (having != null && !having.isEmpty()) {
			countParam.add(having);
		}
		MongoClient mongoClient = getMongoClient(staticMongoBean);
		count = mongo.countAggregates(mongoClient, mongoBean.getDatabaseName(), table, countParam);

		long end = System.currentTimeMillis();
		log.debug("query getCountNoGroup use time  " + (end - start) + " ms");
		log.debug("count=" + count);
		return count;
	}

	/**
	 * 无分组count
	 * 
	 * @param filter
	 * @param group
	 * @param having
	 * @param mongoBean
	 * @param table
	 * @param queryBean
	 * @return
	 * @throws Exception
	 */
	public Long getCount(BsonDocument filter, BsonDocument group, BsonDocument having, MongoDbBean mongoBean,
			String table, QueryBean queryBean) throws Exception {
		return getCountHaveGroup(filter, group, having, mongoBean, table, queryBean);
		// }
	}

	/**
	 * 分组count
	 * 
	 * @param filter
	 * @param group
	 * @param having
	 * @param mongoBean
	 * @param table
	 * @param queryBean
	 * @return
	 * @throws Exception
	 */
	public Long getCount(BsonDocument filter, List<Bson> group, BsonDocument having, MongoDbBean mongoBean,
			String table, QueryBean queryBean) throws Exception {
		return getCountHaveGroup(filter, group, having, mongoBean, table, queryBean);
	}

	public List<Bson> merge(BsonDocument fieldsDocument, BsonDocument group, BsonDocument having, BsonDocument filter,
			List<Bson> sorts, List<Bson> countDistinctList, int limit, int skip) {
		List<Bson> param = new LinkedList<Bson>();
		// 条件
		if (filter != null && filter.size() > 0) {
			param.add(filter);
		}
		// 分组
		if (group != null && group.size() > 0) {
			param.add(group);
		}
		if (countDistinctList != null && countDistinctList.size() > 0) {
			param.addAll(countDistinctList);
		}
		// 过滤
		if (having != null && having.size() > 0) {
			param.add(having);
		}
		// 不显示_id
		param.add(Aggregates.project(Projections.fields(Projections.excludeId())));
		// 返回最后结果
		if (fieldsDocument != null && fieldsDocument.size() > 0) {
			param.add(fieldsDocument);
		}
		if (sorts != null && sorts.size() > 0) {
			param.add(Aggregates.sort(Sorts.orderBy(sorts)));
		}
		if (limit > 0) {
			param.add(Aggregates.limit(limit));
		}
		param.add(Aggregates.skip(skip));
		return param;
	}

	public List<Bson> geneMongQuery(QueryBean queryBean) throws Exception {
		// String table = queryBean.getTable();
		int limit = (queryBean.getLimit() != null && 0 != queryBean.getLimit()) ? queryBean.getLimit() : 10;
		int skip = (queryBean.getSkip() != null && 0 != queryBean.getSkip()) ? queryBean.getSkip() : 0;
		BsonDocument fieldsDocument = null;
		BsonDocument group = null;
		BsonDocument having = null;
		BsonDocument filter = null;
		List<Bson> sorts = null;
		List<Bson> countDistinctList = null;
		// String result = "";
		try {
			ConfigureBean configureBean = getConfigureBean();
			countDistinctList = new ArrayList<Bson>();
			boolean isGroup = isGroup(queryBean);
			// 条件
			filter = conditionMosaicToBson(queryBean.getConditions(), configureBean.getConds());
			// 分组
			if (isGroup) {
				group = groupMosaicToBson(queryBean, configureBean.getFunctions(), countDistinctList);
			}
			// having
			if (isGroup && queryBean.getHaving() != null) {
				having = conditionMosaicToBson(queryBean.getHaving(), configureBean.getConds());
			}
			// order by
			sorts = orderMosaicToBson(queryBean);
			// 无分组的字段
			if (!isGroup) {
				fieldsDocument = fieldMosaicToBson(queryBean, configureBean.getFunctions());
			}

		} catch (Exception ex) {
			throw ex;
		}
		return merge(fieldsDocument, group, having, filter, sorts, countDistinctList, limit, skip);
	}

	// /**
	// * 转bean解析
	// *
	// * @param queryBean
	// * @return
	// */
	// @SuppressWarnings("unchecked")
	// public RetBean beanAnalysis(QueryBean queryBean, Map<String, Object>
	// dataBaseInfoMap) {
	// long startTime = System.currentTimeMillis();
	// long endTime = System.currentTimeMillis();
	//
	// int limit = (queryBean.getLimit() != null && 0 != queryBean.getLimit()) ?
	// queryBean.getLimit() : 10;
	// int skip = (queryBean.getSkip() != null && 0 != queryBean.getSkip()) ?
	// queryBean.getSkip() : 0;
	// Boolean if_count = (queryBean.getIf_count() != null) ?
	// queryBean.getIf_count() : false;
	// Boolean if_show_count = (queryBean.getIf_show_count() != null) ?
	// queryBean.getIf_show_count() : false;
	//
	// if (null != queryBean.getFirst_limit()) {
	// int first_limit = Integer.parseInt(queryBean.getFirst_limit());
	// if (first_limit < limit) {
	// limit = first_limit;
	// } else if (first_limit < (skip + limit)) {
	// limit = first_limit - skip;
	// }
	// }
	//
	// List<Bson> param = new LinkedList<Bson>();
	// List<Document> doncuments = new LinkedList<Document>();
	// BsonDocument fieldsDocument = null;
	// BsonDocument group = null;
	// BsonDocument having = null;
	// BsonDocument filter = null;
	// List<Bson> pageTotal = new LinkedList<Bson>();
	//
	// RetBean result = new RetBean();
	// result.setRet_code("0");
	// try {
	// ConfigureBean configureBean = getConfigureBean();
	// List<Bson> countDistinctList = new ArrayList<Bson>();
	// boolean isGroup = isGroup(queryBean);
	//
	// if (isGroup) {// 是否有分组
	// /**
	// * ********************** 分组 ****************************
	// */
	// group = groupMosaicToBson(queryBean, configureBean.getFunctions(),
	// countDistinctList);
	// if (queryBean.getHaving() != null) {
	// having = conditionMosaicToBson(queryBean.getHaving(),
	// configureBean.getConds());
	// }
	// } else {
	// /**
	// * ******************** 返回字段 ****************************
	// */
	// fieldsDocument = fieldMosaicToBson(queryBean,
	// configureBean.getFunctions());
	// }
	//
	// /**
	// * ********************** 条件 ****************************
	// */
	// filter = conditionMosaicToBson(queryBean.getConditions(),
	// configureBean.getConds());
	// /**
	// * ********************** 排序 ****************************
	// */
	// List<Bson> sorts = orderMosaicToBson(queryBean);
	//
	// /**
	// * ********************** 汇总 ****************************
	// */
	// List<Bson> summaryParam = summaryMosaicToBson(queryBean);
	//
	// if (filter != null && filter.size() > 0) {
	// param.add(filter);
	// if (!isGroup) {// 明细和汇总合计有区分
	// pageTotal.add(filter);
	// }
	// }
	// if (fieldsDocument != null && fieldsDocument.size() > 0) {
	// param.add(fieldsDocument);
	// }
	// if (null != summaryParam && summaryParam.size() > 0) {
	// param.addAll(summaryParam);
	// }
	// if (group != null && group.size() > 0) {
	// param.add(group);
	// }
	// if (countDistinctList != null && countDistinctList.size() > 0) {//
	// 有去重计数的处理
	// param.addAll(countDistinctList);
	// }
	// if (having != null && having.size() > 0) {
	// param.add(having);
	// }
	// if (isGroup) {// 明细和汇总合计有区分
	// pageTotal.addAll(param);
	// }
	//
	// staticMongoBean.setDatabaseName(dataBaseInfoMap.get("db_name").toString());
	// staticMongoBean.setHost(dataBaseInfoMap.get("db_host").toString());
	// staticMongoBean.setPassword(dataBaseInfoMap.get("db_password").toString());
	// staticMongoBean.setPort(Integer.parseInt(dataBaseInfoMap.get("db_port").toString()));
	// staticMongoBean.setUsername(dataBaseInfoMap.get("db_user").toString());
	// String table = dataBaseInfoMap.get("table_name").toString();
	// // MongoDbBean mongoBean = getMongoBean();
	// MongoClient mongoClient = getMongoClient(staticMongoBean);
	//
	// // log.debug("查询数据param={}", param);
	// doncuments = mongo.groupBy(mongoClient,
	// staticMongoBean.getDatabaseName(), table, filter, param, limit,
	// skip, sorts);
	// endTime = System.currentTimeMillis();
	// log.debug("query data use time {} ms", (endTime - startTime));
	// startTime = System.currentTimeMillis();
	// log.debug("doncuments=" + doncuments.toString());
	// // log.debug("doncuments.size()=" + doncuments.size());
	//
	// endTime = System.currentTimeMillis();
	// log.debug("query count use time {} ms", (endTime - startTime));
	// startTime = System.currentTimeMillis();
	//
	// List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	// for (Document document : doncuments) {// mongo结果转换
	// Map<String, Object> map = JSON.parseObject(document.toJson());
	// list.add(map);
	// }
	//
	// Map<String, Object> retmap = new HashMap<String, Object>();
	// if (if_show_count != null && if_show_count && list != null && list.size()
	// > 0) {// 是否返回合计
	// getTotalMap(queryBean, list, retmap, mongoClient, table, filter, sorts,
	// pageTotal, isGroup);
	// if (null != retmap.get("total")) {
	// Map<String, Object> countMap = (Map<String, Object>) retmap.get("total");
	// Integer count = (Integer) countMap.get("count");
	// result.setCount(count);
	// }
	//
	// } else if (if_count != null && if_count) {// 符合的记录
	// // 使用countDistinctList，避免出现问题
	// Integer count = doncuments.size() + skip;
	// if (count >= limit) {
	// Long lcount = null;
	// if (group != null && group.isEmpty()) {
	// log.debug("进入无分组count");
	// lcount = getCount(filter, group, having, staticMongoBean, table,
	// queryBean);
	//
	// } else {
	// log.debug("进入有分组count");
	// lcount = getCount(filter, countDistinctList, having, staticMongoBean,
	// table, queryBean);
	// }
	// // jsonObject.put("count", count);
	// count = lcount.intValue();
	// }
	// result.setCount(count);
	// }
	//
	// result.setTotalResult(retmap);// 合计字段
	// result.setResults(list);
	// result.setLine_count(doncuments.size());
	// } catch (Exception e) {
	// result.setRet_code("-1");
	// result.setRet_message(e.getLocalizedMessage());
	// e.printStackTrace();
	// } finally {
	// doncuments.clear();
	// doncuments = null;
	// }
	//
	// return result;
	// }

	/**
	 * 转bean解析
	 *
	 * @param queryBean
	 * @return
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	public RetBean beanAnalysis(QueryBean queryBean, Map<String, Object> dataBaseInfoMap)
			throws InterruptedException, ExecutionException {

		Boolean if_count = (queryBean.getIf_count() != null) ? queryBean.getIf_count() : false;

		RetBean result = new RetBean();

		if (if_count != null && if_count) {
			result = queryDataAndCountParral(queryBean, dataBaseInfoMap);
		} else {
			result = queryData(queryBean, dataBaseInfoMap);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public RetBean queryDataAndCountParral(QueryBean queryBean, Map<String, Object> dataBaseInfoMap)
			throws InterruptedException, ExecutionException {
		RetBean resultBean = new RetBean();
		Boolean if_show_count = (queryBean.getIf_show_count() != null) ? queryBean.getIf_show_count() : false;

		ForkJoinTask<Integer> querycounttask = null;
		ForkJoinTask<RetBean> querydatatask = null;
		ForkJoinTask<Map<String, Object>> querytotaltask = null;

		if (!if_show_count) {// 如果有合计就不用count了

			Callable<RetBean> querydata = new Callable<RetBean>() {
				@Override
				public RetBean call() throws Exception {
					return queryData(queryBean, dataBaseInfoMap);
				}
			};
			querydatatask = FJP.submit(querydata);

			Callable<Integer> querycount = new Callable<Integer>() {
				@Override
				public Integer call() throws Exception {
					return queryDataCount(queryBean, dataBaseInfoMap);
				}
			};
			querycounttask = FJP.submit(querycount);
			resultBean = querydatatask.get();
			resultBean.setCount(querycounttask.get());

		} else {
			Callable<RetBean> querydata = new Callable<RetBean>() {

				@Override
				public RetBean call() throws Exception {
					return queryData(queryBean, dataBaseInfoMap);
				}
			};
			querydatatask = FJP.submit(querydata);

			Callable<Map<String, Object>> querytotal = new Callable<Map<String, Object>>() {
				@Override
				public Map<String, Object> call() throws Exception {
					return queryTotal(queryBean, dataBaseInfoMap);
				}
			};
			querytotaltask = FJP.submit(querytotal);
			resultBean = querydatatask.get();
			Map<String, Object> totalMap = querytotaltask.get();
			if (null != totalMap.get("total")) {
				Map<String, Object> countMap = (Map<String, Object>) totalMap.get("total");
				Integer count = Integer.parseInt(countMap.get("count").toString());
				resultBean.setCount(count);
				totalMap.put("pageTotal", getPageTotal(queryBean, resultBean.getResults()));
				resultBean.setTotalResult(totalMap);
			} else {
				resultBean.setCount(0);
			}

		}

		return resultBean;

	}

	public Integer queryDataCount(QueryBean queryBean, Map<String, Object> dataBaseInfoMap) {
		Integer count = 0;
		log.debug(" start query data count");
		RetBean result = new RetBean();
		result.setRet_code("0");

		BsonDocument group = null;
		BsonDocument having = null;
		BsonDocument filter = null;

		try {
			ConfigureBean configureBean = getConfigureBean();
			List<Bson> countDistinctList = new ArrayList<Bson>();
			boolean isGroup = isGroup(queryBean);

			if (isGroup) {// 是否有分组
				/**
				 * ********************** 分组 ****************************
				 */
				group = groupMosaicToBson(queryBean, configureBean.getFunctions(), countDistinctList);
			}

			/**
			 * ********************** 条件 ****************************
			 */
			filter = conditionMosaicToBson(queryBean.getConditions(), configureBean.getConds());

			staticMongoBean.setDatabaseName(dataBaseInfoMap.get("db_name").toString());
			staticMongoBean.setHost(dataBaseInfoMap.get("db_host").toString());
			staticMongoBean.setPassword(dataBaseInfoMap.get("db_password").toString());
			staticMongoBean.setPort(Integer.parseInt(dataBaseInfoMap.get("db_port").toString()));
			staticMongoBean.setUsername(dataBaseInfoMap.get("db_user").toString());
			String table = dataBaseInfoMap.get("table_name").toString();

			Long lcount = null;
			if (group != null && group.isEmpty()) {
				log.debug("进入无分组count");
				lcount = getCount(filter, group, having, staticMongoBean, table, queryBean);

			} else {
				log.debug("进入有分组count");
				lcount = getCount(filter, countDistinctList, having, staticMongoBean, table, queryBean);
			}
			count = lcount.intValue();
			// result.setCount(count);

		} catch (Exception e) {
			result.setRet_code("-1");
			result.setRet_message(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {

		}

		return count;
	}

	public RetBean queryData(QueryBean queryBean, Map<String, Object> dataBaseInfoMap) {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		log.debug(" start query data");
		int limit = (queryBean.getLimit() != null && 0 != queryBean.getLimit()) ? queryBean.getLimit() : 10;
		int skip = (queryBean.getSkip() != null && 0 != queryBean.getSkip()) ? queryBean.getSkip() : 0;
		Boolean if_show_count = (queryBean.getIf_show_count() != null) ? queryBean.getIf_show_count() : false;
		RetBean result = new RetBean();
		result.setRet_code("0");

		if (null != queryBean.getFirst_limit()) {
			int first_limit = Integer.parseInt(queryBean.getFirst_limit());
			if (first_limit < limit) {
				limit = first_limit;
			} else if (first_limit < (skip + limit)) {
				limit = first_limit - skip;
			}
		}
		List<Bson> param = new LinkedList<Bson>();
		List<Document> doncuments = new LinkedList<Document>();
		BsonDocument fieldsDocument = null;
		BsonDocument group = null;
		BsonDocument having = null;
		BsonDocument filter = null;
		List<Bson> pageTotal = new LinkedList<Bson>();
		// HashMap<String, Object> jsonObject = new HashMap<String, Object>();

		try {
			ConfigureBean configureBean = getConfigureBean();
			List<Bson> countDistinctList = new ArrayList<Bson>();
			boolean isGroup = isGroup(queryBean);

			if (isGroup) {// 是否有分组
				/**
				 * ********************** 分组 ****************************
				 */
				group = groupMosaicToBson(queryBean, configureBean.getFunctions(), countDistinctList);
				if (queryBean.getHaving() != null) {
					having = conditionMosaicToBson(queryBean.getHaving(), configureBean.getConds());
				}
			} else {
				/**
				 * ******************** 返回字段 ****************************
				 */
				fieldsDocument = fieldMosaicToBson(queryBean, configureBean.getFunctions());
			}

			/**
			 * ********************** 条件 ****************************
			 */
			filter = conditionMosaicToBson(queryBean.getConditions(), configureBean.getConds());
			/**
			 * ********************** 排序 ****************************
			 */
			List<Bson> sorts = orderMosaicToBson(queryBean);

			/**
			 * ********************** 汇总 ****************************
			 */
			List<Bson> summaryParam = summaryMosaicToBson(queryBean);

			if (filter != null && filter.size() > 0) {
				param.add(filter);
			}
			if (!isGroup) {// 明细和汇总合计有区分
				if (sorts != null && sorts.size() > 0) {
					sorts = orderDetailedMosaicToBson(queryBean);
					param.add(Aggregates.sort(Sorts.orderBy(sorts)));
				}
				if (skip > 0) {
					param.add(Aggregates.skip(skip));
				}
				if (limit > 0) {
					param.add(Aggregates.limit(limit));
				}
			}
			if (fieldsDocument != null && fieldsDocument.size() > 0) {
				param.add(fieldsDocument);
			}
			if (null != summaryParam && summaryParam.size() > 0) {
				param.addAll(summaryParam);
			}
			if (group != null && group.size() > 0) {
				param.add(group);
			}
			if (countDistinctList != null && countDistinctList.size() > 0) {//
				// 有去重计数的处理
				param.addAll(countDistinctList);
			}
			if (having != null && having.size() > 0) {
				param.add(having);
			}

			if (isGroup) {// 明细和汇总合计有区分
				if (sorts != null && sorts.size() > 0) {
					param.add(Aggregates.sort(Sorts.orderBy(sorts)));
				}

				if (skip > 0) {
					param.add(Aggregates.skip(skip));
				}

				if (limit > 0) {
					param.add(Aggregates.limit(limit));
				}
			}

			staticMongoBean.setDatabaseName(dataBaseInfoMap.get("db_name").toString());
			staticMongoBean.setHost(dataBaseInfoMap.get("db_host").toString());
			staticMongoBean.setPassword(dataBaseInfoMap.get("db_password").toString());
			staticMongoBean.setPort(Integer.parseInt(dataBaseInfoMap.get("db_port").toString()));
			staticMongoBean.setUsername(dataBaseInfoMap.get("db_user").toString());
			String table = dataBaseInfoMap.get("table_name").toString();
			// MongoDbBean mongoBean = getMongoBean();
			MongoClient mongoClient = getMongoClient(staticMongoBean);

			// log.debug("param={}", param);
			doncuments = mongo.groupBy(mongoClient, staticMongoBean.getDatabaseName(), table, filter, param, limit,
					skip, sorts);
			endTime = System.currentTimeMillis();
			log.debug("query data use time {} ms", (endTime - startTime));
			startTime = System.currentTimeMillis();
			// log.debug("doncuments=" + doncuments.toString());
			// log.debug("doncuments.size()=" + doncuments.size());

			// endTime = System.currentTimeMillis();
			// log.debug("query count use time {} ms", (endTime - startTime));
			// startTime = System.currentTimeMillis();

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			for (Document document : doncuments) {// mongo结果转换
				Map<String, Object> map = JSON.parseObject(document.toJson());
				list.add(map);
			}
			//
			// Map<String, Object> retmap = new HashMap<String, Object>();
			// if (if_show_count != null && if_show_count && list != null &&
			// list.size() > 0) {// 是否返回合计
			// getTotalMap(queryBean, list, retmap, mongoClient, table, filter,
			// sorts, pageTotal, isGroup);
			// if (null != retmap.get("total")) {
			// Map<String, Object> countMap = (Map<String, Object>)
			// retmap.get("total");
			// Integer count =
			// Integer.parseInt(countMap.get("count").toString());
			// result.setCount(count);
			// }
			// }
			// result.setTotalResult(retmap);// 合计字段
			result.setResults(list);
			result.setLine_count(doncuments.size());

		} catch (Exception e) {
			result.setRet_code("-1");
			result.setRet_message(e.getLocalizedMessage());
			e.printStackTrace();
		} finally {
			doncuments.clear();
			doncuments = null;
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryTotal(QueryBean queryBean, Map<String, Object> dataBaseInfoMap) throws Exception {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();
		log.debug(" start query total");
		int limit = (queryBean.getLimit() != null && 0 != queryBean.getLimit()) ? queryBean.getLimit() : 10;
		int skip = (queryBean.getSkip() != null && 0 != queryBean.getSkip()) ? queryBean.getSkip() : 0;
		Boolean if_show_count = (queryBean.getIf_show_count() != null) ? queryBean.getIf_show_count() : false;
		RetBean result = new RetBean();
		result.setRet_code("0");

		if (null != queryBean.getFirst_limit()) {
			int first_limit = Integer.parseInt(queryBean.getFirst_limit());
			limit = first_limit;
			// if (first_limit < limit) {
			// limit = first_limit;
			// } else if (first_limit < (skip + limit)) {
			// limit = first_limit - skip;
			// }

		}

		List<Bson> param = new LinkedList<Bson>();
		BsonDocument fieldsDocument = null;
		BsonDocument group = null;
		BsonDocument having = null;
		BsonDocument filter = null;
		List<Bson> pageTotal = new LinkedList<Bson>();
		// HashMap<String, Object> jsonObject = new HashMap<String, Object>();

		ConfigureBean configureBean = getConfigureBean();
		List<Bson> countDistinctList = new ArrayList<Bson>();
		boolean isGroup = isGroup(queryBean);

		if (isGroup) {// 是否有分组
			/**
			 * ********************** 分组 ****************************
			 */
			group = groupMosaicToBson(queryBean, configureBean.getFunctions(), countDistinctList);
			if (queryBean.getHaving() != null) {
				having = conditionMosaicToBson(queryBean.getHaving(), configureBean.getConds());
			}
		} else {
			/**
			 * ******************** 返回字段 ****************************
			 */
			fieldsDocument = fieldMosaicToBson(queryBean, configureBean.getFunctions());
		}

		/**
		 * ********************** 条件 ****************************
		 */
		filter = conditionMosaicToBson(queryBean.getConditions(), configureBean.getConds());
		/**
		 * ********************** 排序 ****************************
		 */
		List<Bson> sorts = orderMosaicToBson(queryBean);

		/**
		 * ********************** 汇总 ****************************
		 */
		List<Bson> summaryParam = summaryMosaicToBson(queryBean);

		if (!isGroup) {// 明细和汇总合计有区分
			if (filter != null && filter.size() > 0) {
				pageTotal.add(filter);
			}

			if (sorts != null && sorts.size() > 0) {
				param.add(Aggregates.sort(Sorts.orderBy(sorts)));
			}

			// if (skip > 0) {
			// param.add(Aggregates.skip(skip));
			// }

			if (limit > 0) {
				param.add(Aggregates.limit(limit));
			}
		} else {
			if (filter != null && filter.size() > 0) {
				param.add(filter);
			}
		}
		if (fieldsDocument != null && fieldsDocument.size() > 0) {
			param.add(fieldsDocument);
		}
		if (null != summaryParam && summaryParam.size() > 0) {
			param.addAll(summaryParam);
		}
		if (group != null && group.size() > 0) {
			param.add(group);
		}
		if (countDistinctList != null && countDistinctList.size() > 0) {//
			// 有去重计数的处理
			param.addAll(countDistinctList);
		}
		if (having != null && having.size() > 0) {
			param.add(having);
		}
		if (isGroup) {// 明细和汇总合计有区分
			pageTotal.addAll(param);
		}
		staticMongoBean.setDatabaseName(dataBaseInfoMap.get("db_name").toString());
		staticMongoBean.setHost(dataBaseInfoMap.get("db_host").toString());
		staticMongoBean.setPassword(dataBaseInfoMap.get("db_password").toString());
		staticMongoBean.setPort(Integer.parseInt(dataBaseInfoMap.get("db_port").toString()));
		staticMongoBean.setUsername(dataBaseInfoMap.get("db_user").toString());
		String table = dataBaseInfoMap.get("table_name").toString();
		// MongoDbBean mongoBean = getMongoBean();
		MongoClient mongoClient = getMongoClient(staticMongoBean);

		Map<String, Object> retmap = getTotalMap(queryBean, mongoClient, table, filter, sorts, isGroup, pageTotal);
		// if (null != retmap.get("total")) {
		// Map<String, Object> countMap = (Map<String, Object>)
		// retmap.get("total");
		// Integer count = Integer.parseInt(countMap.get("count").toString());
		// result.setCount(count);
		// }

		return retmap;
	}

	public Map<String, BigDecimal> getPageTotal(QueryBean queryBean, List<Map<String, Object>> list) {
		Map<String, BigDecimal> pageMap = new HashMap<String, BigDecimal>();
		Map<String, String> funMap = new HashMap<String, String>();// 存字段函数的map
		for (QueryBeanField entity : queryBean.getFields()) {
			if ("1".equals(entity.getIs_total_field())) {// 取出所有数值型
				String name = StringUtil.isNotEmpty(entity.getAlias()) ? entity.getAlias() : entity.getName();
				String fun = entity.getFunction();
				funMap.put(name, fun);

				for (Map<String, Object> documentMap : list) {// 存当前合计字段的值
					Object ob = documentMap.get(name);
					BigDecimal number = null;
					if (StringUtil.isNotEmpty(ob + "")) {
						number = new BigDecimal(ob + "");
					} else {
						number = new BigDecimal("0");
					}

					if (null != pageMap.get(name)) {// 有就取出来加上
						BigDecimal tmp = pageMap.get(name);
						tmp = tmp.add(number);
						pageMap.put(name, tmp);
					} else {
						pageMap.put(name, number);
					}
				}
				if ("avg".equals(fun)) {// 小计要是平均
					BigDecimal avgNumber = pageMap.get(name);
					BigDecimal divisor = new BigDecimal(Double.toString(list.size()));
					pageMap.put(name, avgNumber.divide(divisor, 6));
				}
			}
		}
		return pageMap;

	}

	/**
	 * 获得合计的方法
	 *
	 * @throws Exception
	 */
	public Map<String, Object> getTotalMap(QueryBean queryBean, MongoClient mongoClient, String table,
			BsonDocument filter, List<Bson> sorts, boolean isGroup, List<Bson> pageTotal) throws Exception {
		// 是否返回合计
		Map<String, Object> retmap = new HashMap<String, Object>();
		Map<String, String> funMap = new HashMap<String, String>();// 存字段函数的map

		List<Bson> totalParam = null;
		BsonDocument projectBsonDocument = new BsonDocument();

		totalParam = getTotalBson(queryBean, filter, isGroup, projectBsonDocument, pageTotal, sorts);

		if (totalParam != null && totalParam.size() > 0) {

			// totalParam.add(projectBsonDocument);

			List<Document> totalDoncuments = mongo.groupBy(mongoClient, staticMongoBean.getDatabaseName(), table, null,
					totalParam, 0, 0, null);
			if (totalDoncuments.size() > 1) {// 有分组
				Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
				for (Document document : totalDoncuments) {
					for (String key : document.keySet()) {// 先把分组的都加上
						Object ob = document.get(key);
						BigDecimal number = null;
						if (StringUtil.isNotEmpty(ob + "")) {
							number = new BigDecimal(ob + "");
						} else {
							number = new BigDecimal("0");
						}

						if (null != map.get(key)) {// 有就取出来加上
							BigDecimal tmp = map.get(key);
							tmp = tmp.add(number);
							map.put(key, tmp);
						} else {
							map.put(key, number);
						}
					}
				}

				for (String funString : funMap.keySet()) {
					String funValue = funMap.get(funString);
					if ("avg".equals(funValue)) {
						BigDecimal number = map.get(funString);
						BigDecimal divisor = new BigDecimal(Double.toString(totalDoncuments.size()));
						map.put(funString, number.divide(divisor, 6));
					}
				}

				retmap.put("total", map);
			} else {
				for (Document document : totalDoncuments) {
					Map<String, Object> map = JSON.parseObject(document.toJson());
					retmap.put("total", map);
				}
			}
		}
		return retmap;

	}

	/**
	 * 获得返回字段
	 *
	 * @param fields
	 * @return
	 * @throws IOException
	 */
	public BsonDocument fieldMosaicToBson(QueryBean queryBean, List<ConfigureBeanFunctions> configureBeanFunctions)
			throws IOException {
		List<QueryBeanField> fields = queryBean.getFields();
		if (fields == null || fields.isEmpty()) {
			return null;
		}
		HashMap<String, Object> fieldObj = new HashMap<String, Object>();
		for (QueryBeanField entity : fields) {
			String function = entity.getFunction();
			if (StringUtil.isNotEmpty(function)) {// 判断当前字段是不是函数

				fieldObj.putAll(getFunctionJson(entity, configureBeanFunctions));
			} else {
				String name = entity.getName();
				String alias = entity.getAlias();
				String $name = "$" + name;

				if (StringUtil.isNotEmpty(entity.getAlias())) {
					fieldObj.put(alias, $name);
				} else {
					fieldObj.put(name, $name);
				}
			}
		}
		BsonDocument fieldsDocument = new BsonDocument();
		String fos = BJU.toJsonString(fieldObj);
		fieldsDocument.put("$project", BsonDocument.parse(fos));
		return fieldsDocument;
	}

	/**
	 * 获得sql返回字段
	 *
	 * @param fields
	 * @return
	 */
	public String fieldMosaicToSql(QueryBean queryBean) {
		StringBuffer sb = new StringBuffer();

		List<QueryBeanField> fields = queryBean.getFields();
		if (null != fields && !fields.isEmpty()) {
			boolean caseFlag = false;// 是否分段统计
			List<QueryBeanGroup> groups = queryBean.getGroups();

			if (null != groups && groups.size() > 0) {
				for (QueryBeanGroup queryBeanGroup : groups) {
					if (queryBeanGroup != null && StringUtil.isNotEmpty(queryBeanGroup.getField())) {
						if ("case".equals(queryBeanGroup.getFunction())) {
							caseFlag = true;
							sb.append(getGroupCase(queryBeanGroup));
							sb.append("AS " + queryBeanGroup.getField() + ",");
						} else {
							sb.append(queryBeanGroup.getField() + " ,");
						}
					}
				}
			}

			for (QueryBeanField entity : fields) {
				String function = entity.getFunction();
				String field = "";
				if (StringUtil.isNotEmpty(function)) {// 判断当前字段是不是函数

					field = getFunctionSql(entity);
				} else {
					String name = entity.getName();
					String alias = entity.getAlias();

					if (!caseFlag && !entity.isDimension()) {
						if (StringUtil.isNotEmpty(alias)) {
							field = name + " as " + alias + ",";
						} else {
							field = name + " as " + name + ",";
						}
					}
				}
				sb.append(field);
			}

			return sb.substring(0, sb.length() - 1);
		}
		return " * ";
	}

	/**
	 * 获得分组
	 *
	 * @param fields
	 * @return
	 */
	public BsonDocument groupMosaicToBson(QueryBean queryBean, List<ConfigureBeanFunctions> configureBeanFunctions,
			List<Bson> param) throws Exception {
		List<QueryBeanGroup> groups = queryBean.getGroups();
		HashMap<String, Object> groupsJson = new HashMap<String, Object>();
		List<Map<String, String>> countDistinctMap = new ArrayList<Map<String, String>>();// 去重计数
		List<Map<String, String>> notemptyMap = new ArrayList<Map<String, String>>();// 非空去重
		BsonDocument countDistinctProject = new BsonDocument();// 用来装去重后的project
		Map<String, String> dimensionMap = new HashMap<String, String>();// 为了页面显示维度
		if (groups != null && !groups.isEmpty()) {// 不止根据一个字段分组就循环
			HashMap<String, Object> groupIdJson = new HashMap<String, Object>();
			for (QueryBeanGroup queryBeanGroup : groups) {
				if (StringUtil.isNotEmpty(queryBeanGroup.getField())) {
					groupIdJson.put(queryBeanGroup.getField(), "$" + queryBeanGroup.getField());
				}
			}

			if (groupIdJson.size() > 0) {// 如果大于0表示分组字段正确
				groupsJson.put("_id", groupIdJson);
			} else {
				groupsJson.put("_id", "null");
			}

			// } else if (groups != null) {
			// String field = "$" + groups.get(0).getField();
			// groupsJson.put("_id", field);
		} else if (groups == null || groups.isEmpty()) {
			groupsJson.put("_id", "null");
		}

		for (QueryBeanField entity : queryBean.getFields()) {
			String function = entity.getFunction();
			String $function = "$" + entity.getFunction();
			String name = entity.getName();
			String $name = "$" + entity.getName();
			HashMap<String, Object> functionJson = new HashMap<String, Object>();
			if (StringUtil.isNotEmpty(entity.getAlias())) {// 有别名就用别名没有就字段名
				name = entity.getAlias();
			}

			for (ConfigureBeanFunctions beanFunction : configureBeanFunctions) {
				if (entity.isDimension()) {
					dimensionMap.put(name, $name);
					break;
				}
				if (beanFunction.getFname().equals(function)) {
					if (beanFunction.isGroup()) {// 说明是要分组的
						if ("count".equals(function)) {
							functionJson.put("$sum", 1);
							groupsJson.put(name, functionJson);
							break;
						} else if ("count_distinct".equals(function)) {
							Map<String, String> map = new HashMap<String, String>();
							functionJson.put("$addToSet", $name);
							groupsJson.put(name, functionJson);
							map.put("name", name);
							map.put("$name", $name);
							countDistinctMap.add(map);
							break;
						} else if ("notempty".equals(function)) {// 去重非空计数
							Map<String, String> map = new HashMap<String, String>();
							functionJson.put("$addToSet", $name);
							groupsJson.put(name, functionJson);
							map.put("name", name);
							map.put("$name", $name);
							notemptyMap.add(map);
							break;
						} else if ("nonemptycount".equals(function)) {// 非空计数
							Map<String, Object> cond1 = new HashMap<String, Object>();
							Map<String, Object> cond2 = new HashMap<String, Object>();
							Map<String, Object> cond3 = new HashMap<String, Object>();
							Map<String, Object> if1 = new HashMap<String, Object>();
							Map<String, Object> if2 = new HashMap<String, Object>();
							Map<String, Object> if3 = new HashMap<String, Object>();
							Map<String, Object> temp1 = new HashMap<String, Object>();
							Map<String, Object> temp2 = new HashMap<String, Object>();
							Map<String, Object> temp3 = new HashMap<String, Object>();
							List<Object> eq1 = new ArrayList<Object>();
							List<Object> eq2 = new ArrayList<Object>();
							List<Object> eq3 = new ArrayList<Object>();
							eq1.add($name);
							eq1.add("");
							eq2.add($name);
							eq2.add(null);
							eq3.add($name);
							eq3.add("null");
							if1.put("$eq", eq1);
							if2.put("$eq", eq2);
							if3.put("$eq", eq3);
							cond3.put("if", if3);
							cond3.put("else", 1);
							cond3.put("then", 0);
							temp3.put("$cond", cond3);
							cond2.put("if", if2);
							cond2.put("then", 0);
							cond2.put("else", temp3);
							temp2.put("$cond", cond2);
							cond1.put("if", if1);
							cond1.put("then", 0);
							cond1.put("else", temp2);
							temp1.put("$cond", cond1);
							functionJson.put("$sum", temp1);
							groupsJson.put(name, functionJson);
							break;
						} else {
							functionJson.put($function, $name);
							groupsJson.put(name, functionJson);
							break;
						}
					} else if (StringUtil.isNotEmpty(function)) {
						functionJson = getFunctionJson(entity, configureBeanFunctions);
						groupsJson.putAll(functionJson);
						break;
					} else {
						// 校验是否正确
						if (groups == null || groups.isEmpty()) {
							throw new Exception("语法有问题");
						}
						int inGroup = 0;
						for (QueryBeanGroup queryBeanGroup : groups) {
							if (name.equals(queryBeanGroup.getField())) {
								inGroup++;
								// groupsJson.put(name, $name);
							}
							if (inGroup == 0) {
								throw new Exception("语法有问题");
							}
						}
					}

				}
			}
		}

		BsonDocument groupDocument = new BsonDocument();
		String gs = BJU.toJsonString(groupsJson);
		groupDocument.put("$group", BsonDocument.parse(gs));

		if (countDistinctMap != null && !countDistinctMap.isEmpty()) {// 如果有去重count
			HashMap<String, Object> countDistinctJson = new HashMap<String, Object>();
			countDistinctJson.putAll(groupsJson);
			for (String key : countDistinctJson.keySet()) {
				countDistinctJson.put(key, "$" + key);
			}

			countDistinctJson.put("_id", "$_id");
			for (Map<String, String> map : countDistinctMap) {
				HashMap<String, Object> tmpObject = new HashMap<String, Object>();
				tmpObject.put("$size", "$" + map.get("name").toString());
				countDistinctJson.put(map.get("name").toString(), tmpObject);
			}
			BsonDocument countDistinctDocument = new BsonDocument();
			String cs = BJU.toJsonString(countDistinctJson);
			countDistinctDocument.put("$project", BsonDocument.parse(cs));
			countDistinctProject.putAll(BsonDocument.parse(cs));
			param.add(groupDocument);
			param.add(countDistinctDocument);
			if ((dimensionMap == null || dimensionMap.isEmpty()) && (notemptyMap == null || notemptyMap.isEmpty())) {
				return null;
			}
		}

		if (notemptyMap != null && !notemptyMap.isEmpty()) {// 如果有非空去重
			if (countDistinctProject.size() == 0) {
				param.add(groupDocument);
				countDistinctProject.putAll(BsonDocument.parse(gs));
			}
			for (Map<String, String> map : notemptyMap) {
				HashMap<String, Object> unwindDocument = new HashMap<String, Object>();
				unwindDocument.put("$unwind", "$" + map.get("name").toString());

				param.add(BsonDocument.parse(BJU.toJsonString(unwindDocument)));

				HashMap<String, Object> notemptyProjectKey = new HashMap<String, Object>();
				for (String projectKey : countDistinctProject.keySet()) {
					if (!projectKey.equals(map.get("name").toString())) {
						notemptyProjectKey.put(projectKey, "$" + projectKey);
					}
				}

				BsonDocument group = new BsonDocument();
				HashMap<String, Object> tmpGroup = new HashMap<String, Object>();
				HashMap<String, Object> sumTemp = new HashMap<String, Object>();
				tmpGroup.put("_id", notemptyProjectKey);

				Map<String, Object> cond1 = new HashMap<String, Object>();
				Map<String, Object> cond2 = new HashMap<String, Object>();
				Map<String, Object> cond3 = new HashMap<String, Object>();
				Map<String, Object> if1 = new HashMap<String, Object>();
				Map<String, Object> if2 = new HashMap<String, Object>();
				Map<String, Object> if3 = new HashMap<String, Object>();
				Map<String, Object> temp1 = new HashMap<String, Object>();
				Map<String, Object> temp2 = new HashMap<String, Object>();
				Map<String, Object> temp3 = new HashMap<String, Object>();
				List<Object> eq1 = new ArrayList<Object>();
				List<Object> eq2 = new ArrayList<Object>();
				List<Object> eq3 = new ArrayList<Object>();
				eq1.add("$" + map.get("name").toString());
				eq1.add("");
				eq2.add("$" + map.get("name").toString());
				eq2.add(null);
				eq3.add("$" + map.get("name").toString());
				eq3.add("null");
				if1.put("$eq", eq1);
				if2.put("$eq", eq2);
				if3.put("$eq", eq3);
				cond3.put("if", if3);
				cond3.put("else", 1);
				cond3.put("then", 0);
				temp3.put("$cond", cond3);
				cond2.put("if", if2);
				cond2.put("then", 0);
				cond2.put("else", temp3);
				temp2.put("$cond", cond2);
				cond1.put("if", if1);
				cond1.put("then", 0);
				cond1.put("else", temp2);
				temp1.put("$cond", cond1);

				sumTemp.put("$sum", temp1);
				tmpGroup.put(map.get("name").toString(), sumTemp);
				group.put("$group", BsonDocument.parse(BJU.toJsonString(tmpGroup)));
				param.add(group);

				BsonDocument project = new BsonDocument();
				HashMap<String, Object> temproject = new HashMap<String, Object>();
				temproject.put("_id", 0);

				for (String projectKey : countDistinctProject.keySet()) {
					if (projectKey.equals(map.get("name").toString())) {
						temproject.put(projectKey, "$" + projectKey);
					} else {
						temproject.put(projectKey, "$_id." + projectKey);
					}
				}
				project.put("$project", BsonDocument.parse(BJU.toJsonString(temproject)));
				param.add(project);
			}

			if (dimensionMap == null || dimensionMap.isEmpty()) {
				return null;
			}
		}

		if (dimensionMap != null && !dimensionMap.isEmpty()) {// 显示维度字段

			HashMap<String, Object> dimensionDistinctJson = new HashMap<String, Object>();

			for (String key : groupsJson.keySet()) {
				if ("_id".equals(key)) {
					for (String dimensionKey : dimensionMap.keySet()) {
						dimensionDistinctJson.put(dimensionKey, "$_id." + dimensionKey);
					}
				} else {
					dimensionDistinctJson.put(key, "$" + key);
				}

			}

			BsonDocument dimensionDistinctDocument = new BsonDocument();
			String ds = BJU.toJsonString(dimensionDistinctJson);
			dimensionDistinctDocument.put("$project", BsonDocument.parse(ds));

			if ((countDistinctMap == null || countDistinctMap.isEmpty())
					&& (notemptyMap == null || notemptyMap.isEmpty())) {
				param.add(groupDocument);
			}
			param.add(dimensionDistinctDocument);
			return null;
		}

		return groupDocument;
	}

	public String getGroupCase(QueryBeanGroup group) {

		StringBuffer sb = new StringBuffer(" CASE ");
		JSONArray jsonArray = JSON.parseArray(group.getParam());

		for (int i = 0; i < jsonArray.size(); i++) {

			int index = i + 1;
			if (index % 2 != 0 && jsonArray.size() - 1 == i) {// 最后一个 而且是单数
				String str = jsonArray.getString(i);
				sb.append(" ELSE ");
				sb.append("'" + str + "'");
				sb.append(" END ");
			} else if (index % 2 != 0) {// 单数
				QueryBeanCondition condition = jsonArray.getObject(i, QueryBeanCondition.class);
				sb.append(" WHEN ");
				sb.append(conditionMosaicToSql(condition));
			} else {// 双数
				String str = jsonArray.getString(i);
				sb.append(" THEN ");
				sb.append("'" + str + "'");
			}

		}

		return sb.toString();
	}

	/**
	 * 获得sql分组
	 *
	 * @param fields
	 * @return
	 */
	public String groupMosaicToSql(QueryBean queryBean) throws Exception {
		List<QueryBeanGroup> groups = queryBean.getGroups();

		if (null == groups || groups.size() < 1) {
			return "";
		}
		boolean flag = false;// 判断是否有group by 字段，如果字段都为空，则不做group byo
		StringBuffer sb = new StringBuffer(" group by ");
		for (QueryBeanGroup queryBeanGroup : groups) {
			if (queryBeanGroup != null && StringUtil.isNotEmpty(queryBeanGroup.getField())) {
				if ("case".equals(queryBeanGroup.getFunction())) {
					sb.append(getGroupCase(queryBeanGroup) + " ,");
				} else {
					sb.append(queryBeanGroup.getField() + " ,");
				}

				flag = true;
			}
		}
		if (flag) {
			return sb.substring(0, sb.length() - 1);
		}
		return "";

	}

	/**
	 * 递归出条件组
	 *
	 * @param queryBeanCondition
	 * @param jsonObj
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getConditions(QueryBeanCondition queryBeanCondition, HashMap<String, Object> jsonObj, String type,
			List<ConfigureBeanConds> configureBeanConds) throws Exception {
		List<QueryBeanCondition> andList = queryBeanCondition.getAnd();
		List<QueryBeanCondition> orList = queryBeanCondition.getOr();

		if (andList != null && andList.size() > 0) {// 判断条件组是不是只有一个
			for (QueryBeanCondition and : andList) {
				getConditions(and, jsonObj, "and", configureBeanConds);
			}
		} else if (orList != null && orList.size() > 0) {
			for (QueryBeanCondition or : orList) {
				getConditions(or, jsonObj, "or", configureBeanConds);
			}
		} else if ("and".equals(type)) {// 如果是and节点
			HashMap<String, Object> condJson = parseCondition(queryBeanCondition, configureBeanConds);
			String field1 = queryBeanCondition.getField1();
			HashMap<String, Object> oldJson = (HashMap<String, Object>) jsonObj.get(field1);
			if (oldJson != null) {
				oldJson.putAll((HashMap<String, Object>) condJson.get(field1));
				jsonObj.put(field1, oldJson);
			} else {
				jsonObj.putAll(condJson);
			}
		} else if ("or".equals(type)) {// 如果是or节点
			List<HashMap<String, Object>> jsonList = (List<HashMap<String, Object>>) jsonObj.get("$or");
			if (jsonList == null) {
				jsonList = new ArrayList<HashMap<String, Object>>();
			}
			HashMap<String, Object> condJson = parseCondition(queryBeanCondition, configureBeanConds);
			jsonList.add(condJson);
			jsonObj.put("$or", jsonList);
		} else {// 没有节点
			jsonObj.putAll(parseCondition(queryBeanCondition, configureBeanConds));
		}
	}

	/**
	 * 获得过滤条件
	 *
	 * @param queryBean
	 * @return
	 */
	public BsonDocument conditionMosaicToBson(List<QueryBeanCondition> conditions,
			List<ConfigureBeanConds> configureBeanConds) throws Exception {
		if (conditions == null || conditions.isEmpty()) {
			return null;
		}
		BsonDocument filter = new BsonDocument();
		List<Object> tmps = new ArrayList<>();
		for (QueryBeanCondition queryBeanCondition : conditions) {
			HashMap<String, Object> jsonObj = new HashMap<>();
			getConditions(queryBeanCondition, jsonObj, null, configureBeanConds);
			if (jsonObj != null && !jsonObj.isEmpty()) {
				tmps.add(jsonObj);
			}
		}
		if (tmps == null || tmps.isEmpty()) {
			return filter;
		}
		Map<String, Object> and = new HashMap<>();
		and.put("$and", tmps);
		String andString = BJU.toJsonString(and);
		filter.put("$match", BsonDocument.parse(andString));
		// log.debug("filter={}", filter);
		return filter;
	}

	/**
	 * 递归出sql条件组
	 *
	 * @param queryBeanCondition
	 * @param jsonObj
	 */
	public void getSqlConditions(QueryBeanCondition queryBeanCondition, List<String> list, String type) {
		List<QueryBeanCondition> andList = queryBeanCondition.getAnd();
		List<QueryBeanCondition> orList = queryBeanCondition.getOr();

		if (andList != null && andList.size() > 0) {// 判断条件组是不是只有一个
			for (QueryBeanCondition and : andList) {
				getSqlConditions(and, list, "and");
			}
		} else if (orList != null && orList.size() > 0) {
			for (QueryBeanCondition or : orList) {
				getSqlConditions(or, list, "or");
			}
		} else if ("and".equals(type)) {// 如果是and节点
			if (StringUtil.isNotEmpty(parseSqlCondition(queryBeanCondition))) {
				String condStr = " and " + parseSqlCondition(queryBeanCondition);
				list.add(condStr);
			}
		} else if ("or".equals(type)) {// 如果是or节点
			if (StringUtil.isNotEmpty(parseSqlCondition(queryBeanCondition))) {
				String condStr = " or " + parseSqlCondition(queryBeanCondition);
				list.add(condStr);
			}
		} else {// 没有节点
			if (StringUtil.isNotEmpty(parseSqlCondition(queryBeanCondition))) {
				String condStr = " and " + parseSqlCondition(queryBeanCondition);
				list.add(condStr);
			}

		}
	}

	/**
	 * 获得sql过滤条件
	 *
	 * @param queryBean
	 * @return
	 */
	public String conditionMosaicToSql(List<QueryBeanCondition> conditions) {

		if (conditions == null) {
			return "";
		}
		StringBuffer sb = new StringBuffer(" 1 = 1");
		List<String> list = new ArrayList<String>();

		for (QueryBeanCondition queryBeanCondition : conditions) {
			getSqlConditions(queryBeanCondition, list, null);
		}

		for (String string : list) {
			sb.append(string);
		}
		// log.info(sb);
		return sb.toString();
	}

	/**
	 * 获得sql过滤条件
	 *
	 * @param queryBean
	 * @return
	 */
	public String conditionMosaicToSql(QueryBeanCondition condition) {

		StringBuffer sb = new StringBuffer(" 1 = 1");
		List<String> list = new ArrayList<String>();

		getSqlConditions(condition, list, null);

		for (String string : list) {
			sb.append(string);
		}
		// log.info(sb);
		return sb.toString();
	}

	/**
	 * 获得排序
	 *
	 * @param orders
	 * @param fields
	 * @return
	 */
	public List<Bson> orderMosaicToBson(QueryBean queryBean) {
		List<QueryBeanField> fields = queryBean.getFields();
		List<QueryBeanOrder> orders = queryBean.getOrders();
		if (orders == null && fields == null) {
			return null;
		}
		/* 把字段名和别名封装成map */
		// Map<String, String> fieldMap = new HashMap<String, String>();
		// for (QueryBeanField queryBeanField : fields) {
		// if (StringUtil.isNotEmpty(queryBeanField.getAlias())) {
		// fieldMap.put(queryBeanField.getName(), queryBeanField.getAlias());
		// } else {
		// fieldMap.put(queryBeanField.getName(), queryBeanField.getName());
		// }
		// }

		List<Bson> sorts = new ArrayList<Bson>();

		for (QueryBeanOrder entity : orders) {
			String type = entity.getType();
			// String field = fieldMap.get(entity.getField());
			String field = StringUtil.isNotEmpty(entity.getAlias())?entity.getAlias():entity.getField();
			if (StringUtil.isEmpty(field)) {// 判断是何种规则获取字段名
				int seq = entity.getSeq();
				if (fields.size() < seq) {
					// 报错，xml错误
				} else {
					QueryBeanField fieldEntity = fields.get(seq - 1);
					if (StringUtil.isNotEmpty(fieldEntity.getAlias())) {// 有别名要用别名
						field = fieldEntity.getAlias();
					} else {
						field = fieldEntity.getName();
					}

				}
			}
			if ("asc".equals(type) || "ASC".equals(type)) {// 判断当前字段的排序规则
				sorts.add(Sorts.ascending(field));
			} else if ("DESC".equals(type) || "desc".equals(type)) {
				sorts.add(Sorts.descending(field));
			} else {
				sorts.add(Sorts.descending(field));
			}
		}
		return sorts;
	}
	
	/**
	 * 获得排序
	 *
	 * @param orders
	 * @param fields
	 * @return
	 */
	public List<Bson> orderDetailedMosaicToBson(QueryBean queryBean) {
		List<QueryBeanField> fields = queryBean.getFields();
		List<QueryBeanOrder> orders = queryBean.getOrders();
		if (orders == null && fields == null) {
			return null;
		}
		/* 把字段名和别名封装成map */
		// Map<String, String> fieldMap = new HashMap<String, String>();
		// for (QueryBeanField queryBeanField : fields) {
		// if (StringUtil.isNotEmpty(queryBeanField.getAlias())) {
		// fieldMap.put(queryBeanField.getName(), queryBeanField.getAlias());
		// } else {
		// fieldMap.put(queryBeanField.getName(), queryBeanField.getName());
		// }
		// }

		List<Bson> sorts = new ArrayList<Bson>();

		for (QueryBeanOrder entity : orders) {
			String type = entity.getType();
			// String field = fieldMap.get(entity.getField());
			String field = entity.getField();
			if (StringUtil.isEmpty(field)) {// 判断是何种规则获取字段名
				int seq = entity.getSeq();
				if (fields.size() < seq) {
					// 报错，xml错误
				} else {
					QueryBeanField fieldEntity = fields.get(seq - 1);
					if (StringUtil.isNotEmpty(fieldEntity.getAlias())) {// 有别名要用别名
						field = fieldEntity.getAlias();
					} else {
						field = fieldEntity.getName();
					}

				}
			}
			if ("asc".equals(type) || "ASC".equals(type)) {// 判断当前字段的排序规则
				sorts.add(Sorts.ascending(field));
			} else if ("DESC".equals(type) || "desc".equals(type)) {
				sorts.add(Sorts.descending(field));
			} else {
				sorts.add(Sorts.descending(field));
			}
		}
		return sorts;
	}

	/**
	 * 汇总
	 * 
	 * @param queryBean
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Bson> summaryMosaicToBson(QueryBean queryBean) {
		List<Bson> param = new LinkedList<Bson>();
		QuerySummary querySummary = queryBean.getQuerySummary();
		if (null == querySummary) {
			return null;
		}
		BsonDocument summarys = new BsonDocument();
		List<QueryBeanField> fields = queryBean.getFields();
		Map<String, Object> projectDoc = new HashMap<String, Object>();
		String unitIdField = querySummary.getUnitId();
		String $unitIdField = "$" + querySummary.getUnitId();
		Map<String, List<String>> caseMap = querySummary.getCaseMap();
		List<Map<String, Object>> condMaps = new ArrayList<Map<String, Object>>();

		for (QueryBeanField queryBeanField : fields) {
			String name = queryBeanField.getName();
			projectDoc.put(name, "$" + name);
		}

		for (String caseMapKey : caseMap.keySet()) {
			Map<String, Object> condMap = new HashMap<>();
			Map<String, Object> tempMap = new HashMap<>();
			Map<String, Object> ifMap = new HashMap<>();
			List<Object> ifList = new ArrayList<Object>();
			ifList.add($unitIdField);
			ifList.add(caseMap.get(caseMapKey));
			ifMap.put("$in", ifList);
			tempMap.put("then", caseMapKey);
			tempMap.put("else", "");
			tempMap.put("if", ifMap);
			condMap.put("$cond", tempMap);
			condMaps.add(condMap);
		}

		String elseMapStr = "";
		for (int i = 0; i < condMaps.size(); i++) {
			Map<String, Object> tempMap = condMaps.get(i);
			if (StringUtil.isNotEmpty(elseMapStr)) {
				Map<String, Object> tempMap2 = (Map<String, Object>) tempMap.get("$cond");
				Map<String, Object> elseMap = JSON.parseObject(elseMapStr, Map.class);
				tempMap2.put("else", elseMap);
				elseMapStr = JSON.toJSONString(tempMap);
			} else {
				elseMapStr = JSON.toJSONString(tempMap);
			}
		}

		projectDoc.put(unitIdField, JSON.parseObject(elseMapStr, Map.class));
		summarys.put("$project", BsonDocument.parse(JSON.toJSONString(projectDoc)));
		param.add(summarys);
		BsonDocument match = new BsonDocument();
		Map<String, Object> matchDoc = new HashMap<String, Object>();
		Map<String, Object> $neDoc = new HashMap<String, Object>();
		$neDoc.put("$ne", "");
		matchDoc.put(unitIdField, $neDoc);
		match.put("$match", BsonDocument.parse(JSON.toJSONString(matchDoc)));
		param.add(match);
		return param;
	}

	/**
	 * 获得sql排序
	 *
	 * @param orders
	 * @param fields
	 * @return
	 */
	public String orderMosaicToSql(QueryBean queryBean) {
		List<QueryBeanField> fields = queryBean.getFields();
		List<QueryBeanOrder> orders = queryBean.getOrders();
		if (orders == null || orders.size() < 1) {
			return "";
		}
		StringBuffer sb = new StringBuffer(" order by ");
		boolean flag = false;
		for (QueryBeanOrder entity : orders) {
			String type = entity.getType();
			String field = entity.getField();
			if (StringUtil.isEmpty(field)) {// 判断是何种规则获取字段名
				int seq = entity.getSeq();
				if (fields.size() < seq) {
					// 报错，xml错误
				} else {
					QueryBeanField fieldEntity = fields.get(seq - 1);
					field = fieldEntity.getName();
				}
			}
			if (StringUtil.isEmpty(type)) {
				type = "DESC";
			}
			if (StringUtil.isNotEmpty(field)) {
				sb.append(" " + field + " " + type + " ,");
				flag = true;
			}

		}
		if (flag) {
			return sb.substring(0, sb.length() - 1);
		}
		return "";
	}

	public HashMap<String, Object> getFunctionJson(QueryBeanField entity,
			List<ConfigureBeanFunctions> configureBeanFunctions) {
		HashMap<String, Object> jsonObject = new HashMap<String, Object>();
		String name = entity.getName();
		String alias = entity.getAlias();
		String $name = "$" + name;
		String function = entity.getFunction();
		String $function = "$" + function;
		if (StringUtil.isNotEmpty(alias)) {// 如果有别名就用别名
			name = alias;
		}
		for (ConfigureBeanFunctions beanFunction : configureBeanFunctions) {
			if (beanFunction.getFname().equals(function)) {
				if (beanFunction.isArray()) {// 说明是那种数组的
					jsonObject.putAll(getArrayParam(entity, $name, name, $function));
				} else {
					HashMap<String, Object> tmpObject = new HashMap<String, Object>();
					tmpObject.put($function, $name);
					jsonObject.put(name, tmpObject);
				}

			}
		}
		return jsonObject;
	}

	public String getFunctionSql(QueryBeanField entity) {
		String name = entity.getName();
		String alias = entity.getAlias();
		String function = entity.getFunction();

		String field = "";
		if ("count_distinct".equals(function)) {
			field = "count(distinct  " + name + ")";
		} else if ("nonemptycount".equals(function)) {
			field = "count(" + name + ")";
		} else if ("notempty".equals(function)) {
			field = "count(distinct  " + name + ")";
		} else {
			field = function + "(" + name + ")";
		}
		if (StringUtil.isNotEmpty(alias)) {// 如果有别名就用别名
			field += " as " + alias + " ,";
		} else {
			field += " as " + name + " ,";
		}
		return field;
	}

	// 判断有没有聚集函数
	public boolean isGroup(QueryBean queryBean) {
		boolean isGroup = false;
		if (queryBean.getFields() == null) {
			return isGroup;
		}
		for (QueryBeanField entity : queryBean.getFields()) {
			if ("count".equals(entity.getFunction()) || "sum".equals(entity.getFunction())
					|| "avg".equals(entity.getFunction()) || "first".equals(entity.getFunction())
					|| "last".equals(entity.getFunction()) || "max".equals(entity.getFunction())
					|| "min".equals(entity.getFunction()) || "stdDevPop".equals(entity.getFunction())
					|| "stdDevSamp".equals(entity.getFunction()) || "count_distinct".equals(entity.getFunction())
					|| "notempty".equals(entity.getFunction()) || "nonemptycount".equals(entity.getFunction())) {
				isGroup = true;
				return isGroup;
			}
		}
		if (queryBean.getGroups() != null && queryBean.getGroups().size() > 0) {// 有group节点
			isGroup = true;
			for (QueryBeanGroup group : queryBean.getGroups()) {
				if (StringUtil.isEmpty(group.getField())) {
					isGroup = false;
				}
			}
		}
		return isGroup;

	}

	/**
	 * 解析条件节点数据
	 *
	 * @param ele
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, Object> parseCondition(QueryBeanCondition queryBeanCondition,
			List<ConfigureBeanConds> configureBeanConds) throws Exception {
		String cond = transformation(queryBeanCondition.getCond(), configureBeanConds);// 转换逻辑符号
		String field1 = queryBeanCondition.getField1();
		Object value1 = queryBeanCondition.getValue1();
		String field2 = queryBeanCondition.getField2();
		HashMap<String, Object> condJson = new HashMap<String, Object>();
		if ("between".equals(cond)) {
			Object value2 = queryBeanCondition.getValue2();
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put("$gte", value1);
			json.put("$lte", value2);
			condJson.put(field1, json);
		} else if ("like".equals(cond)) {
			String value = Objects.toString(value1);
			// 正则表达式开始
			// value = "^" + value + "$";
			value = value.replace("%", ".{0,}");
			HashMap<String, Object> like = new HashMap<String, Object>();
			like.put("$regex", value);
			condJson.put(field1, like);
		} else if ("start".equals(cond)) {
			String value = Objects.toString(value1);
			// 正则表达式开始
			value = "^" + value;
			value = value.replace("%", ".{0,}");
			HashMap<String, Object> like = new HashMap<String, Object>();
			like.put("$regex", value);
			condJson.put(field1, like);
		} else if ("end".equals(cond)) {
			String value = Objects.toString(value1);
			// 正则表达式开始
			value = value + "$";
			value = value.replace("%", ".{0,}");
			HashMap<String, Object> like = new HashMap<String, Object>();
			like.put("$regex", value);
			condJson.put(field1, like);
		} else if ("notLike".equals(cond)) {
			// String value = value1.toString().replace("%", "/");
			// if (value.indexOf("/") != 0) {
			// value = "/" + value;
			// }
			// if (value.lastIndexOf("/") != value.length() - 1) {
			// value = value + "/";
			// }
			// $regex
			String value = value1.toString().replace("%", "");
			HashMap<String, Object> like = new HashMap<String, Object>();
			like.put("$regex", value);
			HashMap<String, Object> tmpJson = new HashMap<String, Object>();
			tmpJson.put("$not", like);
			condJson.put(field1, tmpJson);
		} else if ("$in".equals(cond) || "$nin".equals(cond)) {
			// String value[] = value1.toString().substring(1,
			// value1.toString().length() - 1).split(",");
			List<Object> value = null;
			if (value1 instanceof List) {
				value = (List<Object>) value1;
			} else {
				throw new Exception("in  value1 type is bad ");
			}

			boolean flag = false;
			for (Object object : value) {
				if (StringUtil.isNotEmpty(object + "")) {
					flag = true;
				}
			}

			if (flag) {
				HashMap<String, Object> json = new HashMap<String, Object>();
				json.put(cond, value);
				condJson.put(field1, json);
			}
		} else if (value1 != null && StringUtil.isNotEmpty(value1.toString())) {// 判断是常量还是字段
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put(cond, value1);
			HashMap<String, Object> oldJson = (HashMap<String, Object>) condJson.get(field1);
			if (oldJson != null) {
				oldJson.putAll(json);
				condJson.put(field1, oldJson);
			} else {
				condJson.put(field1, json);
			}
		} else if ("$ne".equals(cond) && (value1 == null || "".equals(value1 + ""))) {// 不等于null或者不等于""
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put(cond, value1);
			HashMap<String, Object> oldJson = (HashMap<String, Object>) condJson.get(field1);
			if (oldJson != null) {
				oldJson.putAll(json);
				condJson.put(field1, oldJson);
			} else {
				condJson.put(field1, json);
			}
		} else if (StringUtil.isNotEmpty(field2)) {
			HashMap<String, Object> json = new HashMap<String, Object>();
			json.put(cond, "$" + field2);
			condJson.put(field1, json);
		}
		if ("isnull".equals(cond.replace(" ", ""))) {// null值查询
			condJson.put(field1, "null");
		}
		return condJson;
	}

	/**
	 * 解析sql条件节点数据
	 *
	 * @param ele
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String parseSqlCondition(QueryBeanCondition queryBeanCondition) {
		String cond = queryBeanCondition.getCond();// 转换逻辑符号
		String field1 = queryBeanCondition.getField1();
		Object value1 = queryBeanCondition.getValue1();
		Object value2 = queryBeanCondition.getValue2();
		String field2 = queryBeanCondition.getField2();
		String result = "";
		if (StringUtil.isNotEmpty(field2)) {
			result = " " + field1 + cond + field2 + " ";

		} else if (StringUtil.isNotEmpty(value1 + "")) {// 细分各种清空
			if ("like".equals(cond)) {// 包含
				result = " " + field1 + " " + cond + " '%" + value1 + "%' ";
			} else if ("notLike".equals(cond)) {// 不包含
				result = " " + field1 + " not like '%" + value1 + "%' ";
			} else if ("start".equals(cond)) {// 开头包含
				result = " " + field1 + " like '" + value1 + "%' ";
			} else if ("end".equals(cond)) {// 结尾包含
				result = " " + field1 + " like '%" + value1 + "' ";
			} else if ("in".equals(cond)) {// in
				List<Object> list = null;
				if (value1 instanceof List) {
					list = (List<Object>) value1;
					if (list.size() > 0) {
						StringBuffer sb = new StringBuffer(" ");
						for (Object object : list) {
							if (object != null && object != "") {
								sb.append("'");
								sb.append(object);
								sb.append("' ,");
							}
						}
						if (sb.length() > 1) {
							result = " " + field1 + " " + cond + " (" + sb.substring(0, sb.length() - 1) + ") ";
						}
					}
				}

			} else if ("notin".equals(cond)) {// not in
				List<Object> list = null;
				if (value1 instanceof List) {
					list = (List<Object>) value1;
					if (list.size() > 0) {
						StringBuffer sb = new StringBuffer(" ");
						for (Object object : list) {
							if (object != null && object != "") {
								sb.append("'");
								sb.append(object);
								sb.append("' ,");
							}
						}
						if (sb.length() > 1) {
							result = " " + field1 + " not in (" + sb.substring(0, sb.length() - 1) + ") ";
						}
					}
				}

			} else if ("between".equals(cond)) {// 结尾包含
				result = " " + field1 + " between " + value1 + " and " + value2;
			} else {// 其他
				result = " " + field1 + " " + cond + " '" + value1 + "' ";
			}

		}
		return result;
	}

	/**
	 * 转换逻辑判断条件表达式为mongo的
	 *
	 * @param text
	 * @return
	 */
	public static String transformation(String text, List<ConfigureBeanConds> configureBeanConds) {
		for (ConfigureBeanConds entity : configureBeanConds) {
			if (text.equals(entity.getSql_cond())) {
				text = entity.getMongo_cond();
			}
		}
		return text;
	}

	/**
	 * 对数组的函数进行处理
	 *
	 * @return
	 */
	public HashMap<String, Object> getArrayParam(QueryBeanField entity, String $name, String name, String $function) {
		List<Object> params = entity.getParam();
		if (params == null || params.isEmpty()) {
			return new HashMap<String, Object>();
		}
		HashMap<String, Object> jsonObject = new HashMap<String, Object>();
		HashMap<String, Object> tmpObject = new HashMap<String, Object>();
		List<Object> arrList = new ArrayList<Object>(params.size() + 1);
		arrList.add($name);
		arrList.addAll(params);
		// for (Object param : params) {
		// arrList.add(param);
		// }
		// params = new String[arrList.size()];
		// params = arrList.toArray(params);
		tmpObject.put($function, arrList);
		jsonObject.put(name, tmpObject);
		// log.debug(jsonObject);
		return jsonObject;
	}

	// @Override
	// public String queryByJson(String json, String metadataId) throws
	// Exception {
	//
	// String ret = null;
	//
	// try {
	// ret = beanAnalysis(AnalysisUtil.toQueryBean(json, QueryBean.class));
	// // saveQueryLog(uuid , ret, "result");
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// // long end = System.currentTimeMillis();
	// // log.debug("queryByJson use " + ( end-start) + " ms");
	// return ret;
	// }
	public void saveQueryLog(String uuid, String json, String flag) {
		long start = System.currentTimeMillis();
		try {
			Document document = new Document();
			document.put("uuid", uuid);
			document.put("process_server_ip", LocalIP);
			document.put("process_server_hostname", LocalHostname);
			document.put("create_time", DateUtil.getServerTime(DateUtil.DEFAULT_TIME_FORMAT_EN));
			if ("param".equals(flag)) {// 插入前
				document.put("param", json);
				// document.put("result", "");
			} else {// 插入后
				// document.put("param", "");
				document.put("result", json);
			}
			// mongo.insert(getMongoBean(),
			// PropUtil.getProperty("logTableName"), document);
			MongoClient mongoClient = getMongoClient(staticMongoBean);
			mongo.insert(mongoClient, staticMongoBean.getDatabaseName(), PropUtil.getProperty("logTableName"),
					document);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		log.debug("saveQueryLog use " + (end - start) + " ms");
	}

	public ConfigureBean getConfigureBean() {
		String json = PropUtil.getProperty("configureJson");
		ConfigureBean configureBean = AnalysisUtil.toQueryBean(json, ConfigureBean.class);
		return configureBean;
	}

	@Override
	public RetBean queryListByJson(QueryBean queryBean) throws Exception {
		long s = System.currentTimeMillis();
		String metadataTableCode = queryBean.getTable();
		MetaData metadata = MetadataUtil.getMetadataBymetadataCode(metadataTableCode, "p"); // 元数据库表属性信息
		RetBean result = new RetBean();
		if (null == metadata) {
			result.setRet_code("-1");
			result.setRet_message("元数据不存在！");
			return result;
		}
		long s2 = System.currentTimeMillis();
		log.debug("查询元数据" + (s2 - s) + "ms");
		MetaData parent = metadata.getParent();
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		propertyMap.putAll(metadata.getProperty());
		propertyMap.putAll(parent.getProperty());
		long s1 = System.currentTimeMillis();
		if ("mongo".equals(propertyMap.get("db_type"))) {
			result = beanAnalysis(queryBean, propertyMap);
		} else {
			result = beanAnalysisSql(queryBean, propertyMap);
		}

		log.debug("查询数据" + (s1 - s) + "ms");
		return result;
	}

	/**
	 * 转bean解析成sql查询
	 *
	 * @param queryBean
	 * @return
	 * @throws Exception
	 */
	public RetBean beanAnalysisSql(QueryBean queryBean, Map<String, Object> dataBaseInfoMap) throws Exception {
		RetBean retBean = new RetBean();
		Integer limit = queryBean.getLimit();
		Integer skip = queryBean.getSkip();
		Boolean if_count = (queryBean.getIf_count() != null) ? queryBean.getIf_count() : false;
		Boolean if_show_count = (queryBean.getIf_show_count() != null) ? queryBean.getIf_show_count() : false;
		String tableName = dataBaseInfoMap.get("table_name").toString();
		StringBuffer sb = new StringBuffer("select ");

		/**
		 * ******************** 返回字段 ****************************
		 */
		String field = fieldMosaicToSql(queryBean);

		/**
		 * ********************** 条件 ****************************
		 */
		String conds = conditionMosaicToSql(queryBean.getConditions());

		/**
		 * ********************** 分组 ****************************
		 */
		String group = groupMosaicToSql(queryBean);

		// String having = conditionMosaicToSql(queryBean.getHaving());

		/**
		 * ********************** 排序 ****************************
		 */
		String order = orderMosaicToSql(queryBean);

		sb.append(field);

		sb.append(" from " + tableName + " t ");

		if (StringUtil.isNotEmpty(conds)) {
			conds = " where " + conds;
		}

		sb.append(conds);

		sb.append(group);

		String totalSql = sb.toString();

		sb.append(order);

		String db_type = "";
		log.debug("beanAnalysisSql = " + sb);
		if (dataBaseInfoMap.containsKey("database_type")) {
			db_type = (String) dataBaseInfoMap.get("database_type");
		} else {
			db_type = (String) dataBaseInfoMap.get("db_type");
		}
		if ("mysql".equals(db_type)) {
			if (null != limit && null != skip) {
				sb.append(" limit ");
				sb.append(skip + ", ");
				sb.append(limit);
			}
		} else if ("oracle".equals(db_type)) {
			if (null != limit && null != skip) {
				String tmpSb = sb.toString();
				sb.append(" SELECT *  FROM (SELECT tt.*, ROWNUM AS rowno  FROM ( ");
				sb.append(tmpSb + " ) tt");
				sb.append("  WHERE ROWNUM <= " + limit + ") table_alias WHERE table_alias.rowno >= " + skip);
			}
		} else if ("postgresql".equals(db_type)) {
			if (null != limit && null != skip) {
				sb.append(" limit ");
				sb.append(limit + " ");
				sb.append(" offset " + skip);
			}
		}
		JdbcConBean jdbcConBean = AnalysisUtil.setJdbcBeanSql(dataBaseInfoMap);

		List<Map<String, Object>> result = jdbcUtil.select(jdbcConBean, sb.toString(), new Object[] {});
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (if_count != null && if_count) {// 符合的记录
			String countSql = "select count(1) as retcount from (" + totalSql + ") t";
			List<Map<String, Object>> count = jdbcUtil.select(jdbcConBean, countSql, new Object[] {});
			Long c = (long) count.get(0).get("retcount");
			retBean.setCount(c.intValue());
		}

		Map<String, Object> retmap = new HashMap<String, Object>();
		if (if_show_count != null && if_show_count) {// 是否返回合计
			String totalsql = getTotalsql(queryBean, tableName);// 拼接查询合计sql
			if (StringUtil.isNotEmpty(totalsql)) {
				List<Map<String, Object>> total = jdbcUtil.select(jdbcConBean, totalsql, new Object[] {});
				retmap.put("total", total.get(0));
				Map<String, BigDecimal> pageMap = new HashMap<String, BigDecimal>();
				for (QueryBeanField entity : queryBean.getFields()) {
					if ("1".equals(entity.getIs_total_field())) {// 取出所有数值型
						String name = StringUtil.isNotEmpty(entity.getAlias()) ? entity.getAlias() : entity.getName();

						for (Map<String, Object> resultListMap : result) {// 存当前合计字段的值
							Object ob = resultListMap.get(name);
							BigDecimal number = new BigDecimal(ob + "");
							if (null != pageMap.get(name)) {// 有就取出来加上
								BigDecimal tmp = pageMap.get(name);
								tmp = tmp.add(number);
								pageMap.put(name, tmp);
							} else {
								pageMap.put(name, number);
							}
						}

					}
				}
				retmap.put("pageTotal", pageMap);
			}

		}

		retBean.setTotalResult(retmap);// 合计
		resultMap.put("skip", skip);
		resultMap.put("limit", limit);
		resultMap.put("resultList", result);
		resultMap.put("line_count", result.size());
		retBean.setResults(result);
		retBean.setRet_code("0");
		retBean.setLine_count(result.size());
		// log.debug("result=" + result);
		return retBean;
	}

	private String getTotalsql(QueryBean queryBean, String tableName) {
		StringBuffer sb = new StringBuffer("select ");
		boolean flag = false;
		String result = "";
		if (null != queryBean.getFields() && !queryBean.getFields().isEmpty()) {
			for (QueryBeanField beanField : queryBean.getFields()) {
				if ("number".equals(beanField.getFiled_type()) || "decimal".equals(beanField.getFiled_type())) {// 取出所有数值型
					String name = beanField.getName();
					String alias = beanField.getAlias();
					sb.append(" sum(");
					sb.append(name);
					sb.append(") ");
					if (StringUtil.isNotEmpty(alias)) {
						sb.append("as " + alias);
					} else {
						sb.append("as " + name);
					}
					sb.append(",");
					flag = true;
				}
			}
			result = sb.substring(0, sb.length() - 1) + " from " + tableName;
		}
		if (flag) {
			return result;
		}
		return "";
	}

	/**
	 * 获得合计bson
	 *
	 * @param fields
	 * @return
	 * @throws IOException
	 */
	public List<Bson> getTotalBson(QueryBean queryBean, BsonDocument filter, boolean isGroup,
			BsonDocument projectBsonDocument, List<Bson> pageTotal, List<Bson> sorts) throws IOException {

		List<Bson> totalParam = new ArrayList<Bson>();
		// List<Bson> tempTotalParam = new ArrayList<Bson>();

		List<QueryBeanField> fields = queryBean.getFields();
		if (fields == null || fields.isEmpty()) {
			return null;
		}
		// List<Map<String, String>> countDistinctMap = new
		// ArrayList<Map<String, String>>();// 存在去重计数的标识
		HashMap<String, Object> fieldObj = new HashMap<String, Object>();
		BsonDocument groupBsonDocument = new BsonDocument();
		HashMap<String, Object> projectObj = new HashMap<String, Object>();
		if (isGroup) {
			totalParam.addAll(pageTotal);
		} else {
			if (filter != null && filter.size() > 0) {
				totalParam.add(filter);
			}
		}
		int f_limit = StringUtil.isNotEmpty(queryBean.getFirst_limit()) ? Integer.parseInt(queryBean.getFirst_limit())
				: 0;

		int limit = (queryBean.getLimit() != null && 0 != queryBean.getLimit()) ? queryBean.getLimit() : 10;
		if (sorts != null && sorts.size() > 0) {
			totalParam.add(Aggregates.sort(Sorts.orderBy(sorts)));
		}
		// if (0 < queryBean.getSkip()) {
		// totalParam.add(Aggregates.skip(queryBean.getSkip()));
		// }
		if (f_limit > 0) {

			totalParam.add(Aggregates.limit(f_limit));
		}
		// else {
		// if (filter != null && filter.size() > 0) {
		// totalParam.add(0, filter);
		// }
		// }
		// int f_limit = StringUtil.isNotEmpty(queryBean.getFirst_limit()) ?
		// Integer.parseInt(queryBean.getFirst_limit())
		// : 0;
		//
		// if (sorts != null && sorts.size() > 0) {
		// totalParam.add(Aggregates.sort(Sorts.orderBy(sorts)));
		// }
		// if (f_limit > 0) {
		// totalParam.add(Aggregates.skip(0));
		// totalParam.add(Aggregates.limit(f_limit));
		// }
		// totalParam.addAll(pageTotal);
		// List<QueryBeanGroup> groups = queryBean.getGroups();
		// HashMap<String, Object> groupObj = new HashMap<String, Object>();
		// for (QueryBeanGroup queryBeanGroup : groups) {
		// String field = queryBeanGroup.getField();
		// if (StringUtil.isNotEmpty(field)) {
		// groupObj.put(field, "$" + field);
		// }
		// }
		// fieldObj.put("_id", groupObj);
		// } else {
		fieldObj.put("_id", null);
		// }
		for (QueryBeanField entity : fields) {
			if ("1".equals(entity.getIs_total_field())) {
				HashMap<String, Object> tmpObj = new HashMap<String, Object>();

				String name = entity.getName();
				String alias = entity.getAlias();
				String fun = entity.getFunction();
				if (StringUtil.isNotEmpty(entity.getAlias())) {
					name = alias;
				}
				if (isGroup) {// 明细和汇总有区别

					String $name = "$" + name;

					if ("avg".equals(fun)) {
						tmpObj.put("$avg", $name);
						// } else if ("count".equals(fun) ||
						// "count_distinct".equals(fun) ||
						// "notempty".equals(fun)
						// || "nonemptycount".equals(fun)) {
						// tmpObj.put("$sum", 1);
					} else {
						tmpObj.put("$sum", $name);
					}

				} else {
					String $name = "$" + entity.getName();

					if ("avg".equals(fun)) {
						tmpObj.put("$avg", $name);
					} else if ("count".equals(fun) || "count_distinct".equals(fun) || "notempty".equals(fun)
							|| "nonemptycount".equals(fun)) {
						tmpObj.put("$sum", 1);
					} else {
						tmpObj.put("$sum", $name);
					}

				}
				fieldObj.put(name, tmpObj);
				projectObj.put(name, "$" + name);

			}
		}
		HashMap<String, Object> tmpObj1 = new HashMap<String, Object>();
		tmpObj1.put("$sum", 1);
		fieldObj.put("count", tmpObj1);
		projectObj.put("count", "$count");
		groupBsonDocument.put("$group", BsonDocument.parse(BJU.toJsonString(fieldObj)));

		projectBsonDocument.put("$project", BsonDocument.parse(BJU.toJsonString(projectObj)));
		totalParam.add(groupBsonDocument);
		return totalParam;
	}

	@Override
	public RetBean queryBySql(ExecuteBySqlBean queryBean) throws Exception {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();

		RetBean result = new RetBean();
		String sql = queryBean.getSql();
		String db_code = queryBean.getDb_code();
		MetaData metadata = MetadataUtil.getMetadataBymetadataCode(db_code, "i"); // 元数据库表属性信息

		endTime = System.currentTimeMillis();
		log.debug("getMetadataBymetadataCode use time {}  {} ", endTime - startTime, db_code);
		startTime = System.currentTimeMillis();

		if (null == metadata) {
			result.setRet_code("-1");
			result.setRet_message("元数据不存在！");
			return result;
		}
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		propertyMap.putAll(metadata.getProperty());
		JdbcConBean jdbcConBean = AnalysisUtil.setJdbcBeanSql(propertyMap);

		List<Map<String, Object>> list = jdbcUtil.select(jdbcConBean, sql, queryBean.getObjects());

		endTime = System.currentTimeMillis();
		log.debug("select use time {}  {} ", endTime - startTime, sql);
		startTime = System.currentTimeMillis();

		result.setResults(list);
		result.setRet_code("0");
		return result;
	}

	@Override
	public RetBean queryBatchSql(ExecuteBatchSqlBean executeBatchSqlBean) throws Exception {
		log.debug("批量查询开始");
		RetBean result = new RetBean();
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		for (ExecuteBySqlBean executeBySqlBean : executeBatchSqlBean.getExecuteBySqlBeans()) {
			String sql = executeBySqlBean.getSql();
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

			List<Map<String, Object>> list = jdbcUtil.select(jdbcConBean, sql, executeBySqlBean.getObjects());
			map.put(executeBySqlBean.getFlagKey(), list);

		}
		log.debug("批量查询结束");
		result.setRet_code("0");
		result.setBatchResults(map);
		return result;
	}

	@Override
	public RetBean queryMultiTable(SqlBean sqlBean) throws Exception {
		RetBean result = new RetBean();
		Map<String, Object> propertyMap = new HashMap<String, Object>();
		JdbcConBean jdbcConBean = null;
		if (sqlBean.getDb_connMap() == null) {
			MetaData metadata = MetadataUtil.getMetadataBymetadataCode(sqlBean.getDb_metadata(), "i"); // 元数据库表属性信息
			if (null == metadata) {
				result.setRet_code("-1");
				result.setRet_message("元数据不存在！");
				return result;
			}
			propertyMap.putAll(metadata.getProperty());
			sqlBean.setDb_connMap(propertyMap);
			jdbcConBean = AnalysisUtil.setJdbcBeanSql(propertyMap);
		} else {
			propertyMap.putAll(sqlBean.getDb_connMap());
			jdbcConBean = AnalysisUtil.setJdbcBeanSql(propertyMap);
		}
		String SQL = SqlBeanHelper.AnalyticMultiTable(sqlBean);
		log.debug("SQL = " + SQL);
		if (sqlBean.getIf_count()) {
			String countSQL = SqlBeanHelper.AnalyticMultiTableCount(sqlBean);
			log.debug("countSQL = " + countSQL);
			List<Map<String, Object>> countList = jdbcUtil.select(jdbcConBean, countSQL);
			Object count = countList.get(0).get("count");
			result.setCount(Integer.parseInt(count.toString()));
		}
		List<Map<String, Object>> list = jdbcUtil.select(jdbcConBean, SQL);
		result.setResults(list);
		result.setLine_count(list.size());
		result.setRet_code("0");
		return result;
	}
}
