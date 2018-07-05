package cn.com.infohold.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.core.BasePage;
import cn.com.infohold.core.service.IService;
import cn.com.infohold.entity.ViewTableCofig;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-17
 */
public interface IViewTableCofigService extends IService<ViewTableCofig> {

	List<Map<String, Object>> selectData(String table, String json,String... orderBy) throws SQLException, Exception;

	void delData(String table, String json) throws SQLException, Exception;

	void updateData(String table, String json) throws SQLException, Exception;

	void addData(String table, String json) throws SQLException, Exception;

	List<Map<String, Object>> selectDataObj(String table, String json) throws SQLException, Exception;

	BasePage<Map<String, Object>> selectData(String table, String json, int indexPage, int parseSize) throws Exception;

	JSONObject selectTable(String tableName) throws SQLException, Exception;

	void insertObj(String json) throws Exception;

	RetBean selectDataByMetadataCode(String table, String json, int pageIndex, int pageSize)
			throws SQLException, Exception;

}
