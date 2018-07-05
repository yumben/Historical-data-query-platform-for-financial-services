package cn.com.infohold.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jcabi.aspects.Loggable;

import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.easyquery.param.QueryParams;
import bdp.commons.easyquery.ret.EasyQuery;
import cn.com.infohold.service.IEasyQueryService;
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
public class EasyQueryController {
	@Autowired
	IEasyQueryService easyQueryServiceImpl;

	/**
	 * 查询数据
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/queryDataList")
	public RetBean queryDataList(@RequestParam String param) throws Exception {
		long s = System.currentTimeMillis();
		RetBean ret = new RetBean();
		try {
			QueryParams queryParams = AnalysisUtil.toQueryBean(param, QueryParams.class);
			ret = easyQueryServiceImpl.queryDataList(queryParams);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		long s2 = System.currentTimeMillis();
		log.debug("queryDataList简易查询耗时时间：" + (s2 - s) + "ms   参数：" + param);
		return ret;
	}

	/**
	 * 查询总记录数
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/queryTotalCount")
	public RetBean queryTotalCount(@RequestParam String param) throws Exception {
		long s = System.currentTimeMillis();
		RetBean ret = new RetBean();
		try {
			QueryParams queryParams = AnalysisUtil.toQueryBean(param, QueryParams.class);
			ret = easyQueryServiceImpl.queryTotalCount(queryParams);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		long s2 = System.currentTimeMillis();
		log.debug("queryTotalCount简易查询耗时时间：" + (s2 - s) + "ms   参数：" + param);
		return ret;
	}

	/**
	 * 查询合计
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/queryTotal")
	public RetBean queryTotal(@RequestParam String param) throws Exception {
		long s = System.currentTimeMillis();
		RetBean ret = new RetBean();
		try {
			QueryParams queryParams = AnalysisUtil.toQueryBean(param, QueryParams.class);
			ret = easyQueryServiceImpl.queryTotal(queryParams);
		} catch (Exception ex) {
			ret.setRet_code("-1");
			ret.setRet_message(ex.getLocalizedMessage());
			log.error(ex);
			throw ex;
		}
		long s2 = System.currentTimeMillis();
		log.debug("queryTotal简易查询耗时时间：" + (s2 - s) + "ms   参数：" + param);
		return ret;
	}

	/**
	 * 查询模板
	 * 
	 * @param query_template_id
	 * @return
	 * @throws Exception
	 */
	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/selectEasyQueryTemplate")
	public EasyQuery selectEasyQueryTemplate(@RequestParam String query_template_id, @RequestParam String token)
			throws Exception {
		EasyQuery ret = new EasyQuery();
		try {
			ret = easyQueryServiceImpl.easyQueryTemplate(query_template_id, token);
		} catch (Exception ex) {
			log.error(ex);
			ex.printStackTrace();
			throw ex;
		}
		return ret;
	}

	/**
	 * 导出数据
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/exportEasyqueryData", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String exportEasyqueryData(@RequestParam String param, HttpServletResponse response) throws Exception {
		String ret = "";
		try {
			QueryParams queryParams = AnalysisUtil.toQueryBean(param, QueryParams.class);
			ret = easyQueryServiceImpl.exportEasyqueryData(queryParams);
		} catch (Exception ex) {
			log.error(ex);
			throw ex;
		}
		return ret;
	}

	/**
	 * 查询模板列表，带分页
	 * 
	 * @param query_template_id
	 * @return
	 * @throws Exception
	 */
	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/selectTemplate")
	public RetBean selectTemplate(@RequestParam String param, HttpServletRequest request) throws Exception {
		RetBean ret = new RetBean();
		try {
			QueryParams queryParams = AnalysisUtil.toQueryBean(param, QueryParams.class);
			ret = easyQueryServiceImpl.selectTemplate(queryParams, false);
		} catch (Exception ex) {
			log.error(ex);
			ex.printStackTrace();
			throw ex;
		}
		return ret;
	}

	@Loggable(Loggable.DEBUG)
	@RequestMapping(value = "/selectTemplateTree")
	public RetBean selectTemplateTree(@RequestParam String param, HttpServletRequest request) throws Exception {
		RetBean ret = new RetBean();
		try {
			QueryParams queryParams = AnalysisUtil.toQueryBean(param, QueryParams.class);
			queryParams.setPageSize(9999);
			queryParams.setCurPage(1);
			ret = easyQueryServiceImpl.selectTemplateTree(queryParams);

		} catch (Exception ex) {
			log.error(ex);
			throw ex;
		}
		return ret;
	}
}
