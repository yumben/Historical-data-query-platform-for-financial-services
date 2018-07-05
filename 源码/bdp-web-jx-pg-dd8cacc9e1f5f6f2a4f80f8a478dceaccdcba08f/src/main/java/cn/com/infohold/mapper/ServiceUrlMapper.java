package cn.com.infohold.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import cn.com.infohold.entity.ServiceUrl;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huangdi
 * @since 2017-11-02
 */
public interface ServiceUrlMapper extends BaseMapper<ServiceUrl> {

	public List<Map<String, Object>> selectQueryTemplate(String query_template_id);

	public List<Map<String, Object>> selectQueryCondition(String query_template_id);

	public List<Map<String, Object>> selectQueryFields(String query_template_id);

	public List<Map<String, Object>> selectQueryGroup(String query_template_id);

	public List<Map<String, Object>> selectQueryTables(String query_template_id);

	public List<Map<String, Object>> selectQueryOrder(String query_template_id);

	public List<Map<String, Object>> selectAuthOperations(@Param("query_template_id") String query_template_id,
			@Param("token") String token);

	public List<Map<String, Object>> selectToken(String token);

	public List<Map<String, Object>> selectMetadataPK(Object[] ids);

	public List<Map<String, Object>> selectMetadataDictType(Object[] ids);

	public List<Map<String, Object>> getUnitList();
}