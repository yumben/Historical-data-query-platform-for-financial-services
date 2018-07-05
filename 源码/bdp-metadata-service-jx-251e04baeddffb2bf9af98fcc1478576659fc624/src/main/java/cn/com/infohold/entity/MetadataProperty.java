package cn.com.infohold.entity;

import java.io.Serializable;
import java.util.UUID;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@TableName("metadata_property")
public class MetadataProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 元数据属性id
     */
    @TableId("metadata_property_id")
	private String metadataPropertyId = UUID.randomUUID().toString();
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
	private String propertyCode;


	public String getMetadataPropertyId() {
		return metadataPropertyId;
	}

	public void setMetadataPropertyId(String metadataPropertyId) {
		this.metadataPropertyId = metadataPropertyId;
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

	public String getPropertyCode() {
		return propertyCode;
	}

	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}

}
