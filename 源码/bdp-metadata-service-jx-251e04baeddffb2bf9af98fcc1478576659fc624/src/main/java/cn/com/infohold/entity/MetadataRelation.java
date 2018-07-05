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
@TableName("metadata_relation")
public class MetadataRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 元数据关联id
     */
    @TableId("metadata_relation_id")
	private String metadataRelationId = UUID.randomUUID().toString();
    /**
     * 元数据id
     */
	@TableField("metadata_id")
	private String metadataId;
    /**
     * 被关联元数据id
     */
	@TableField("metadata_relationed")
	private String metadataRelationed;
    /**
     * 条件id
     */
	@TableField("condition_id")
	private String conditionId;
    /**
     * 关联关系id
     */
	@TableField("relation_id")
	private String relationId;
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


	public String getMetadataRelationId() {
		return metadataRelationId;
	}

	public void setMetadataRelationId(String metadataRelationId) {
		this.metadataRelationId = metadataRelationId;
	}

	public String getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(String metadataId) {
		this.metadataId = metadataId;
	}

	public String getMetadataRelationed() {
		return metadataRelationed;
	}

	public void setMetadataRelationed(String metadataRelationed) {
		this.metadataRelationed = metadataRelationed;
	}

	public String getConditionId() {
		return conditionId;
	}

	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
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
