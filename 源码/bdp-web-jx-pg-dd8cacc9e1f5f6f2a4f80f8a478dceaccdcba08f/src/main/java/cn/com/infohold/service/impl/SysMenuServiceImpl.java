package cn.com.infohold.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.param.DeteleBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.param.InsertBean;
import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.param.QueryBeanOrder;
import bdp.commons.dataservice.param.UpdateBean;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.sys.SysMenu;
import bdp.commons.util.BeanUtil;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.service.ICommonsDataService;
import cn.com.infohold.service.ISysMenuService;
import cn.com.infohold.util.TreeUtil;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author huangdi
 * @since 2017-08-26
 */
@Log4j2
@Service
public class SysMenuServiceImpl implements ISysMenuService {

	@Autowired
	ICommonsDataService commonsDataServiceImpl;

	@Override
	public List<Map<String, Object>> getMenuTree(String token) {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();

		ExecuteBySqlBean executeBySqlBean = new ExecuteBySqlBean();
		executeBySqlBean.setDb_code("bdp_basic_auth");
		executeBySqlBean.setSql("SELECT * FROM sys_menu WHERE is_valid='1' order by menu_order ");
		executeBySqlBean.setSys_token(token);

		RetBean retBean = commonsDataServiceImpl.queryDataSql(executeBySqlBean);
		endTime = System.currentTimeMillis();
		log.debug("queryDataSql use time {}  {} ", endTime - startTime, executeBySqlBean);
		startTime = System.currentTimeMillis();

		List<Map<String, Object>> menuList = retBean.getResults();
		List<Map<String, Object>> result = TreeUtil.intiTree(menuList, "0", "parent_id", "menu_code", "childrens");

		endTime = System.currentTimeMillis();
		log.debug("intiTree use time {}  {} ", endTime - startTime);
		startTime = System.currentTimeMillis();

		return result;
	}

	@Override
	public RetBean selectMenuList(SysMenu entity, int pageNo, int pageSize, String flag) throws Exception {
		QueryBean queryBean = new QueryBean();
		// 排序
		List<QueryBeanOrder> queryBeanOrders = new ArrayList<QueryBeanOrder>();
		QueryBeanOrder queryBeanOrder = new QueryBeanOrder();
		queryBeanOrder.setField("menu_order");// 按创建时间排序
		queryBeanOrder.setType("ASC");
		queryBeanOrders.add(queryBeanOrder);
		queryBean.setOrders(queryBeanOrders);
		// 页数页码
		if (pageSize > 0) {
			queryBean.setLimit(pageSize);
		}
		if (pageNo > 0) {
			queryBean.setSkip(pageSize * (pageNo - 1));
		}
		List<QueryBeanCondition> conditionBeans = BeanUtil.toConditionBean(entity);
		queryBean.setConditions(conditionBeans);
		queryBean.setTable("bdpbasicdatabase_sys_menu");
		queryBean.setIf_count(true);
		RetBean retBean = commonsDataServiceImpl.queryData(queryBean);
		retBean.setCurPage(pageNo);
		retBean.setPageSize(pageSize);
		int totalCount = retBean.getCount();
		int totalPage = (totalCount + pageSize - 1) / pageSize;
		retBean.setTotal(totalCount);
		retBean.setTotalPage(totalPage);
		return retBean;
	}

	@Override
	public List<Map<String, Object>> selectMenuAll() throws Exception {
		long startTime = System.currentTimeMillis();
		long endTime = System.currentTimeMillis();

		ExecuteBySqlBean executeBySqlBean = new ExecuteBySqlBean();
		executeBySqlBean.setDb_code("bdp_basic_auth");
		executeBySqlBean.setSql("SELECT * FROM sys_menu WHERE is_valid='1' order by menu_order ");
		// executeBySqlBean.setSys_token(token);

		RetBean retBean = commonsDataServiceImpl.queryDataSql(executeBySqlBean);
		endTime = System.currentTimeMillis();
		log.debug("queryDataSql use time {}  {} ", endTime - startTime, executeBySqlBean);
		startTime = System.currentTimeMillis();

		List<Map<String, Object>> result = retBean.getResults();

		endTime = System.currentTimeMillis();
		log.debug("intiTree use time {}  {} ", endTime - startTime);
		startTime = System.currentTimeMillis();

		return result;
	}

	@Override
	public boolean deleteById(SysMenu entity) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("menu_code", entity.getMenu_code());
		DeteleBean deteleBean = BeanUtil.toDeteleBean(conditionMap);
		deteleBean.setTableCode("bdpbasicdatabase_sys_menu");
		RetBean retBean = commonsDataServiceImpl.deleteData(deteleBean);
		if ("0".equals(retBean.getRet_code())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateById(SysMenu entity) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("menu_code", entity.getMenu_code());
		UpdateBean updateBean = BeanUtil.toUpdateBean(entity, conditionMap);
		updateBean.setTableCode("bdpbasicdatabase_sys_menu");
		RetBean retBean = commonsDataServiceImpl.updateData(updateBean);
		if ("0".equals(retBean.getRet_code())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public SysMenu selectById(SysMenu entity) throws Exception {
		ExecuteBySqlBean executeBySqlBean = new ExecuteBySqlBean();
		executeBySqlBean.setDb_code("bdp_basic_auth");
		executeBySqlBean.setSql("SELECT * FROM sys_menu WHERE is_valid='1' and menu_code='" + entity.getMenu_code()
				+ "' order by menu_order ");
		RetBean retBean = commonsDataServiceImpl.queryDataSql(executeBySqlBean);
		List<Map<String, Object>> result = retBean.getResults();
		if (result.size()>0) {
			Map<String, Object> map=result.get(0);
			JSONObject jsonObject=new JSONObject();
			jsonObject.putAll(map);
			SysMenu sysMenu=new SysMenu();
			sysMenu=BasicJsonUtil.getInstance().toJavaBean(jsonObject.toJSONString(), SysMenu.class);
			return sysMenu;
		}else {
			return null;
		}
	}

	@Override
	public boolean insert(SysMenu entity) {
		Map<String, Object> conditionMap = new HashMap<String, Object>();
		conditionMap.put("menu_code", entity.getMenu_code());
		InsertBean insertBean = BeanUtil.toInsertBean(entity);
		insertBean.setTableCode("bdpbasicdatabase_sys_menu");
		RetBean retBean = commonsDataServiceImpl.insertData(insertBean);
		if ("0".equals(retBean.getRet_code())) {
			return true;
		} else {
			return false;
		}
	}
}
