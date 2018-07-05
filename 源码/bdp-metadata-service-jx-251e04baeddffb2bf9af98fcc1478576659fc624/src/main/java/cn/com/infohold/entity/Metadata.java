package cn.com.infohold.entity;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Data
public class Metadata implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 元数据id
	 */
	@TableId("metadata_id")
	private String metadataId = UUID.randomUUID().toString();
	/**
	 * 元数据代码
	 */
	@TableField("metadata_code")
	private String metadataCode;
	/**
	 * 元数据名称
	 */
	@TableField("metadata_name")
	private String metadataName;
	/**
	 * 上级元数据id
	 */
	@TableField("parent_metadata")
	private String parentMetadata;
	/**
	 * 分类目录id
	 */
	@TableField("catalog_id")
	private String catalogId;
	/**
	 * 类id
	 */
	@TableField("class_id")
	private String classId;
	/**
	 * 该元数据在非元数据业务是否显示
	 */
	private String notshow;
	/**
	 * 创建时间
	 */
	@TableField("create_date")
	private String createDate;
	/**
	 * 创建人
	 */
	@TableField("create_name")
	private String createName;
	/**
	 * 修改时间
	 */
	@TableField("edit_date")
	private String editDate;
	/**
	 * 修改人
	 */
	@TableField("edit_name")
	private String editName;

	@TableField(exist = false)
	private String className;

	@TableField(exist = false)
	private String classCode;

	@TableField(exist = false)
	private String pannelpageId;

	/**
	 * 元数据所属上下文目录
	 */
	@TableField(exist = false)
	private String contextCatalog;

	/**
	 * 被关联元数据ID
	 */
	@TableField(exist = false)
	private String beRelationMeta;

	/**
	 * 关联元数据ID
	 */
	@TableField(exist = false)
	private String relationMeta;
	/**
	 * 关联名称
	 */
	@TableField(exist = false)
	private String relationName;

	/**
	 * 被关联元数据ID
	 */
	@TableField(exist = false)
	private String metadataRelationed;
	/**
	 * 关联元数据ID
	 */
	@TableField(exist = false)
	private String metadataRelationId;

	/**
	 * 属性code
	 */
	@TableField(exist = false)
	private String propertyCode;

	/**
	 * 属性值
	 */
	@TableField(exist = false)
	private String propertyValue;

	/**
	 * 属性列表
	 */
	@TableField(exist = false)
	private JSONObject property;
	
	@TableField(exist = false)
	private JSONArray children;
	
	/**
	 * 父级对象
	 */
	@TableField(exist = false)
	private Metadata parent;
	
	

	public String getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}

	public String getMetadataCode() {
		return metadataCode;
	}

	public void setMetadataCode(String metadataCode) {
		this.metadataCode = metadataCode;
	}

	public String getMetadataName() {
		return metadataName;
	}

	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}

	public String getParentMetadata() {
		return parentMetadata;
	}

	public void setParentMetadata(String parentMetadata) {
		this.parentMetadata = parentMetadata;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getNotshow() {
		return notshow;
	}

	public void setNotshow(String notshow) {
		this.notshow = notshow;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getEditDate() {
		return editDate;
	}

	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}

	public String getEditName() {
		return editName;
	}

	public void setEditName(String editName) {
		this.editName = editName;
	}

}
