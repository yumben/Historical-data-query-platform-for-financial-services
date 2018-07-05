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
@Data
@TableName("view_column_cofig")
public class ViewColumnCofig implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId("column_id")
	private String columnId;
	
	@TableField("order_index")
	private String orderIndex;
	@TableField("column_code")
	private String columnCode;
	@TableField("column_name")
	private String columnName;
	/**
	 * text：文本框，radio:单选框，select：下拉框，checked：多选框，
	 */
	@TableField("column_type")
	private String columnType;
	@TableField("column_data_type")
	private String columnDataType;
	@TableField("table_id")
	private String tableId;
	/**
	 * 是否是主键
	 */
	@TableField("is_pk")
	private String isPk;
	/**
	 * 是否增加字段
	 */
	@TableField("is_insert")
	private String isInsert;
	/**
	 * 是否可编辑
	 */
	@TableField("is_edit")
	private String isEdit;
	/**
	 * 是否做查询字段
	 */
	@TableField("is_search")
	private String isSearch;
	@TableField("is_view")
	private String isView;
	/**
	 * 字段是否有超链接
	 */
	private String links;
	@TableField("column_desc")
	private String columnDesc;
	@TableField("column_data_value")
	private String columnDataValue;
	@TableField("view_group")
	private String viewGroup;
	@TableField("relation_type")
	private String relation_type;
	@TableField("relation_table")
	private String relation_table;
	@TableField("relation_field")
	private String relation_field;

}
