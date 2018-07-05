package cn.com.infohold.service;


import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import cn.com.infohold.entity.Metadata;





/**
 * 元数据菜单服务类
 * @author 54754
 *
 */
public interface IMetadataDatabaseService{
	public Map<String ,String>  addTableColumn(Metadata metadataVO,String[] propertyValue) throws Exception ;
	
	public boolean creatDb(String[] propertyValue, String baseName) throws Exception;
	
	public Metadata getMetadataVO(String metadataId) throws Exception ;
	
	public boolean  delTableColumn(Metadata metadataVO) throws Exception ;
	
	public boolean  dropDatabase(Metadata metadataVO) throws Exception ;
		
	public boolean  delTable(Metadata metadataVO) throws Exception ;
	
	public Map<String ,String> updateTable(Metadata metadataVO,String  newName) throws Exception ;
	
	public Map<String, String> updateTableColumn(Metadata metadataVO,String[] propertyValue) throws Exception;
	
	public boolean  updateDatabase(Metadata metadataVO,String[] propertyValue, String code) throws Exception;

	public String changeUrlDatabaseName (String dbUrl ,String databaseName);
	
	//public Map<String ,String> checkConnectDatabase(Metadata metadataVO, String[] propertyValue,String code) throws Exception ;
	
	public Map<String ,String> checkTableIsExist(Metadata metadataVO,String tableName) throws SQLException, Exception;
	
	public Map<String, String> checkConnectDatabase(Metadata metadataVO, String[] propertyValue)
			throws Exception;
	public Map<String, String> addPostGreCol(Metadata metadataVO, String[] propertyValue,String tableName) throws Exception ;
	
	public Map<String ,String> createIndexTables(Metadata metadataVO, String[] dimFieids, String[] dimParentFieids, String[] ruleFieids, String[] ruleParentFieids) throws Exception;
	
	public Map<String ,String > addCheckDatabaseOperator(Metadata metadata,String[] propertyArr);
	
	public Map<String ,String > updateCheckDatabaseOperator(Metadata metadata,String[] propertyArr);
	

}
