package cn.com.infohold.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bdp.commons.dataservice.bean.SqlBean;
import bdp.commons.dataservice.param.BatchOperationBean;
import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.service.IDataQueryService;
import cn.com.infohold.service.IDataSplitQueryService;
import cn.com.infohold.util.AnalysisUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
@RestController
@Log4j2
public class DataQueryController {

	@Autowired
	IDataQueryService dataQueryServiceImpl;
	@Autowired
	IDataSplitQueryService dataSplitQueryServiceImpl;

	@RequestMapping(value = "/query")
	public RetBean dataQuiry(@RequestParam String param) throws Exception {
		long start = System.currentTimeMillis();
		RetBean ret = new RetBean();
		ret.setRet_code("0");
		ret.setRet_message("");
		try {
			log.debug("数据服务接受参数  {} ", param);
			QueryBean queryBean = AnalysisUtil.toQueryBean(param, QueryBean.class);
			ret = dataQueryServiceImpl.queryListByJson(queryBean);
			long end = System.currentTimeMillis();
			log.debug("数据服务查询耗时  {} ms", (end - start));
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	@RequestMapping(value = "/queryBySql")
	public RetBean queryBySql(@RequestParam String param) throws Exception {
		RetBean ret = new RetBean();
		try {
			ExecuteBySqlBean executeBySqlBean = AnalysisUtil.toQueryBean(param, ExecuteBySqlBean.class);
			ret = dataQueryServiceImpl.queryBySql(executeBySqlBean);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	@RequestMapping(value = "/queryByList")
	public RetBean queryByList(@RequestParam String param) throws Exception {
		ForkJoinPool FJP = AnalysisUtil.getForkJoinPool();

		RetBean ret = new RetBean();
		Map<String, List<Map<String, Object>>> batchResults = new HashMap<String, List<Map<String, Object>>>();
		try {
			BatchOperationBean batchOperationBean = AnalysisUtil.toQueryBean(param, BatchOperationBean.class);
			// 生成任务
			List<ForkJoinTask<Object>> fjtList = batchOperationBean.getQueryBeans().parallelStream().map(queryBean -> {
				Callable<Object> ca = new Callable<Object>() {
					@Override
					public ForkJoinTask<Object> call() throws Exception {
						RetBean temp = dataQueryServiceImpl.queryListByJson(queryBean);
						batchResults.put(queryBean.getTable(), temp.getResults());
						return null;
					}
				};
				ForkJoinTask<Object> fjt = FJP.submit(ca);
				return fjt;
			}).collect(Collectors.toList());
			// 等待结束
			for (ForkJoinTask<Object> fjt : fjtList) {
				fjt.get();
			}
			// for (QueryBean queryBean : batchOperationBean.getQueryBeans()) {
			// RetBean temp = dataQueryServiceImpl.queryListByJson(queryBean);
			// batchResults.put(queryBean.getTable(), temp.getResults());
			// }
			ret.setBatchResults(batchResults);
			ret.setRet_code("0");
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	@RequestMapping(value = "/queryBatchSql")
	public RetBean queryBatchSql(@RequestParam String param) throws Exception {
		RetBean ret = new RetBean();
		try {
			ExecuteBatchSqlBean executeBatchSqlBean = AnalysisUtil.toQueryBean(param, ExecuteBatchSqlBean.class);
			ret = dataQueryServiceImpl.queryBatchSql(executeBatchSqlBean);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	/**
	 * 多表查询
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryMultiTable")
	public RetBean queryMultiTable(@RequestParam String param) throws Exception {
		RetBean ret = new RetBean();
		try {
			SqlBean sqlBean = AnalysisUtil.toQueryBean(param, SqlBean.class);
			ret = dataQueryServiceImpl.queryMultiTable(sqlBean);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	/**
	 * 取数据
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryDataList")
	public RetBean queryDataList(@RequestParam String param) throws Exception {
		RetBean ret = new RetBean();
		try {
			QueryBean queryBean = AnalysisUtil.toQueryBean(param, QueryBean.class);
			ret = dataSplitQueryServiceImpl.queryDataList(queryBean);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	/**
	 * 取总记录数
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryTotalCount")
	public RetBean queryTotalCount(@RequestParam String param) throws Exception {
		RetBean ret = new RetBean();
		try {
			QueryBean queryBean = AnalysisUtil.toQueryBean(param, QueryBean.class);
			ret = dataSplitQueryServiceImpl.queryTotalCount(queryBean);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	/**
	 * 取合计
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/queryTotal")
	public RetBean queryTotal(@RequestParam String param) throws Exception {
		RetBean ret = new RetBean();
		try {
			QueryBean queryBean = AnalysisUtil.toQueryBean(param, QueryBean.class);
			ret = dataSplitQueryServiceImpl.queryTotal(queryBean);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		return ret;
	}

}
