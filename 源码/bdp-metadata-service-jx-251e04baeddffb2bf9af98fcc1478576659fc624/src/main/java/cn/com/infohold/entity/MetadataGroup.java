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
@TableName("metadata_group")
public class MetadataGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 元数据组合id
     */
    @TableId("metadata_group_id")
	private String metadataGroupId = UUID.randomUUID().toString();
    /**
     * 元数据id
     */
	@TableField("metadata_id")
	private String metadataId;
    /**
     * 被组合元数据id
     */
	@TableField("metadatagrouped_id")
	private String metadatagroupedId;
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


	public String getMetadataGroupId() {
		return metadataGroupId;
	}

	public void setMetadataGroupId(String metadataGroupId) {
		this.metadataGroupId = metadataGroupId;
	}

	public String getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}

	public String getMetadatagroupedId() {
		return metadatagroupedId;
	}

	public void setMetadatagroupedId(String metadatagroupedId) {
		this.metadatagroupedId = metadatagroupedId;
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
