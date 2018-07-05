package cn.com.infohold.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.param.QueryBeanCondition;
import bdp.commons.dataservice.param.QueryBeanField;
import cn.com.infohold.service.ICommonsDataService;

/**
 * 控制层
 * 
 * @author
 * 
 */
@Controller
@RequestMapping("/dictionaryControll")
public class DictionaryController  {
	@Autowired
	ICommonsDataService commonsDataServiceImpl;

	@RequestMapping(value = "/getDictionary", produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String getDictionary(HttpServletRequest request) throws IOException {

		String relation_type = request.getParameter("relation_type");
		String relation_table = request.getParameter("relation_table");
		String relation_field = request.getParameter("relation_field");
		QueryBean queryBean = new QueryBean();
		QueryBeanField queryBeanField = new QueryBeanField();
		List<QueryBeanField> fields = new ArrayList<QueryBeanField>();
		List<QueryBeanCondition> conditions = new ArrayList<QueryBeanCondition>();
		queryBean.setTable(relation_table);
		if ("dict".equals(relation_type)) {// 查询字典
		
			// 字段
			queryBeanField.setName("dictdata_value");//字段暂时写死，以后要修改
			queryBeanField.setAlias("value");
			fields.add(queryBeanField);
			queryBeanField = new QueryBeanField();
			queryBeanField.setName("dictdata_name");//字段暂时写死，以后要修改
			queryBeanField.setAlias("label");
			fields.add(queryBeanField);
			queryBean.setFields(fields);
			// 条件
			QueryBeanCondition queryBeanCondition = new QueryBeanCondition();
			queryBeanCondition.setField1("dict_type");//字段暂时写死，以后要修改
			queryBeanCondition.setCond("=");
			queryBeanCondition.setValue1(relation_field);
			conditions.add(queryBeanCondition);
			queryBean.setConditions(conditions);
			
		} else if ("fk".equals(relation_type)) {// 外键
			if (relation_field!=null) {
				String[] relation_field_arr = relation_field.split(",");//字段分组
				// 字段
				queryBeanField.setName(relation_field_arr[0]);
				queryBeanField.setAlias("value");
				fields.add(queryBeanField);
				queryBeanField = new QueryBeanField();
				queryBeanField.setName(relation_field_arr[1]);
				queryBeanField.setAlias("label");
				fields.add(queryBeanField);
				queryBean.setFields(fields);
			}
			
		}else {
			return null;
		}
		return commonsDataServiceImpl.queryDictionary(queryBean);
	}

}
