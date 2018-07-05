package cn.com.infohold.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import bdp.commons.dataservice.param.ExecuteBatchSqlBean;
import bdp.commons.dataservice.param.ExecuteBySqlBean;
import bdp.commons.dataservice.ret.RetBean;
import bdp.commons.metadata.ret.MetaData;
import cn.com.infohold.basic.util.jdbc.BasicJdbcUtil;
import cn.com.infohold.basic.util.jdbc.JdbcConBean;
import cn.com.infohold.service.IDataExecuteBySqlService;
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
public class DataExecuteBySqlServiceImpl implements IDataExecuteBySqlService {

    private static final BasicJdbcUtil jdbcUtil = BasicJdbcUtil.getInstance();

    @Override
    public RetBean executeBySql(ExecuteBySqlBean executeBySqlBean) throws Exception {
        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();

        RetBean result = new RetBean();
        String sql = executeBySqlBean.getSql();
        String db_code = executeBySqlBean.getDb_code();
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
        jdbcUtil.executeUpdate(jdbcConBean, sql, new Object[]{});
        
        endTime = System.currentTimeMillis();
        log.debug("executeUpdate use time {}  {} ", endTime - startTime, sql);
        startTime = System.currentTimeMillis();
        
        result.setRet_code("0");
        return result;
    }

    @Override
    public RetBean executeBatchSql(ExecuteBatchSqlBean executeBatchSqlBean) throws SQLException, Exception {
        RetBean result = new RetBean();

        if (executeBatchSqlBean != null && executeBatchSqlBean.getExecuteBySqlBeans() != null && executeBatchSqlBean.getExecuteBySqlBeans().size() > 0) {
            String db_code = executeBatchSqlBean.getDb_code();
            MetaData metadata = MetadataUtil.getMetadataBymetadataCode(db_code, "i"); // 元数据库表属性信息
            if (null == metadata) {
                result.setRet_code("-1");
                result.setRet_message("元数据不存在！");
                return result;
            }
            Map<String, Object> propertyMap = new HashMap<String, Object>();
            propertyMap.putAll(metadata.getProperty());
            JdbcConBean jdbcConBean = AnalysisUtil.setJdbcBeanSql(propertyMap);
            String sql = "";
            //循环执行语句
            for (ExecuteBySqlBean executeBySqlBean : executeBatchSqlBean.getExecuteBySqlBeans()) {
                sql = executeBySqlBean.getSql();
                jdbcUtil.executeUpdate(jdbcConBean, sql, executeBySqlBean.getObjects());
            }

        }

        result.setRet_code("0");
        return result;
    }

}
