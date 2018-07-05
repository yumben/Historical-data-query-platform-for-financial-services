package cn.com.infohold.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author huangdi
 * @since 2017-11-28
 */
@Data
@TableName("v_metadata_property")
public class VMetadataProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 元数据属性id
     */
	@TableField("metadata_property_id")
	private String metadataPropertyId;
    /**
     * 上级元数据id
     */
	@TableField("parent_metadata")
	private String parentMetadata;
    /**
     * 元数据id
     */
	@TableField("metadata_id")
	private String metadataId;
    /**
     * 元模型类属性id
     */
	@TableField("class_property_id")
	private String classPropertyId;
    /**
     * 属性值
     */
	@TableField("property_value")
	private String propertyValue;
    /**
     * 属性代码
     */
	@TableField("property_code")
	private String propertyCode;
    /**
     * 属性名称
     */
	@TableField("property_name")
	private String propertyName;
    /**
     * 是否为空
     */
	private String iscannull;
    /**
     * 是否显示
     */
	private String isshow;
    /**
     * 继承性
     */
	private String inheritance;
    /**
     * 数据类型id
     */
	@TableField("datatype_id")
	private String datatypeId;
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
     * 分类目录id
     */
	@TableField("catalog_id")
	private String catalogId;
    /**
     * 类id
     */
	@TableField("class_id")
	private String classId;


	public String getMetadataPropertyId() {
		return metadataPropertyId;
	}

	public void setMetadataPropertyId(String metadataPropertyId) {
		this.metadataPropertyId = metadataPropertyId;
	}

	public String getParentMetadata() {
		return parentMetadata;
	}

	public void setParentMetadata(String parentMetadata) {
		this.parentMetadata = parentMetadata;
	}

	public String getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}

	public String getClassPropertyId() {
		return classPropertyId;
	}

	public void setClassPropertyId(String classPropertyId) {
		this.classPropertyId = classPropertyId;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getIscannull() {
		return iscannull;
	}

	public void setIscannull(String iscannull) {
		this.iscannull = iscannull;
	}

	public String getIsshow() {
		return isshow;
	}

	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}

	public String getInheritance() {
		return inheritance;
	}

	public void setInheritance(String inheritance) {
		this.inheritance = inheritance;
	}

	public String getDatatypeId() {
		return datatypeId;
	}

	public void setDatatypeId(String datatypeId) {
		this.datatypeId = datatypeId;
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

}
