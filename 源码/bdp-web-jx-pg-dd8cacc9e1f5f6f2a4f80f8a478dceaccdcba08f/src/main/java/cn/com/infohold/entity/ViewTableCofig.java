package cn.com.infohold.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-17
 */
@TableName("view_table_cofig")
@Data
public class ViewTableCofig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("table_id")
	private String table_id;
    /**
     * 配置编码
     */
	@TableField("table_code")
	private String tableCode;
    /**
     * 配置名称
     */
	@TableField("table_name")
	private String tableName;
    /**
     * 展示的表元数据ID
     */
	@TableField("metadata_id")
	private String metadataId;

	@TableField("metadata_code")
	private String metadataCode;
	@TableField("view_title")
	private String viewTitle;
    /**
     * table:是表格;form:是表单
     */
	@TableField("view_type")
	private String viewType;
    /**
     * 是否是主键
     */
	@TableField("pk_field")
	private String pkField;
    /**
     * 是否增加操作
     */
	@TableField("is_insert")
	private String isInsert;
    /**
     * 是否修改操作
     */
	@TableField("is_edit")
	private String isEdit;
    /**
     * 是否做查询字段
     */
	@TableField("is_search")
	private String isSearch;
    /**
     * 是否删除操作
     */
	@TableField("is_delete")
	private String isDelete;

}
