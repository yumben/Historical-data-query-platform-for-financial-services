package cn.com.infohold.service;

import java.io.IOException;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletResponse;

import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.param.QueryParams;
import bdp.commons.easyquery.ret.EasyQuery;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-08-03
 */
public interface IEasyQueryService {

	/**
	 * 灵活查询数据
	 * 
	 * @param queryParams
	 * @return
	 * @throws IOException
	 * @throws ScriptException
	 */
	RetBean queryDataList(QueryParams queryParams) throws Exception;

	/**
	 * 灵活查询数据
	 * 
	 * @param queryParams
	 * @return
	 * @throws IOException
	 * @throws ScriptException
	 */
	RetBean queryTotalCount(QueryParams queryParams) throws Exception;

	/**
	 * 灵活查询数据
	 * 
	 * @param queryParams
	 * @return
	 * @throws IOException
	 * @throws ScriptException
	 */
	RetBean queryTotal(QueryParams queryParams) throws Exception;

	/**
	 * 灵活查询数据(汇总)
	 * 
	 * @param queryParams
	 * @return
	 * @throws IOException
	 */
	RetBean getSummaryEasyQueryDataJson(QueryParams queryParams) throws IOException;

	/**
	 * 灵活查询导出
	 * 
	 * @param queryParams
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	String exportEasyqueryData(QueryParams queryParams) throws IOException, Exception;

	/**
	 * 根据模板ID查询模板信息
	 * 
	 * @param query_template_id-模板ID
	 * @return
	 * @throws IOException
	 */
	EasyQuery easyQueryTemplate(String query_template_id, String token) throws IOException;

	/**
	 * 根据模板ID查询数据库表
	 * 
	 * @param query_template_id
	 * @return
	 * @throws IOException
	 */
	// List<QueryTables> selectQueryTables(String
	// query_template_id,List<QueryBean> queryBeans) throws IOException;
	// /**
	// * 根据模板ID查询分组表
	// * @param query_template_id
	// * @return
	// * @throws IOException
	// */
	// List<QueryGroups> selectQueryGroups(String
	// query_template_id,List<QueryBean> queryBeans) throws IOException;
	// /**
	// * 根据模板ID查询字段表
	// * @param query_template_id
	// * @return
	// * @throws IOException
	// */
	// List<QueryFields> selectQueryFields(String
	// query_template_id,List<QueryBean> queryBeans) throws IOException;
	// /**
	// * 根据模板ID查询条件表
	// * @param query_template_id
	// * @return
	// * @throws IOException
	// */
	// List<QueryConditions> selectQueryConditions(String
	// query_template_id,List<QueryBean> queryBeans) throws IOException;
	/**
	 * 查询模板列表，带分页
	 * 
	 * @param queryParams
	 * @return
	 * @throws IOException
	 */
	RetBean selectTemplate(QueryParams queryParams, boolean isHavPrivilege) throws IOException;

	RetBean selectTemplateTree(QueryParams queryParams) throws Exception;

}
