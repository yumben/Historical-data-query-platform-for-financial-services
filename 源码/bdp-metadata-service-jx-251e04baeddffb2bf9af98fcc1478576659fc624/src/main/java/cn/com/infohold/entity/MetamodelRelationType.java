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
@TableName("metamodel_relation_type")
public class MetamodelRelationType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联类型id
     */
    @TableId("relation_type_id")
	private String relationTypeId = UUID.randomUUID().toString();
    /**
     * 关联类型名称
     */
	@TableField("relation_type_name")
	private String relationTypeName;
    /**
     * 描述
     */
	private String describes;
    /**
     * 父关联类型id
     */
	@TableField("parentrela_id")
	private String parentrelaId;


	public String getRelationTypeId() {
		return relationTypeId;
	}

	public void setRelationTypeId(String relationTypeId) {
		this.relationTypeId = relationTypeId;
	}

	public String getRelationTypeName() {
		return relationTypeName;
	}

	public void setRelationTypeName(String relationTypeName) {
		this.relationTypeName = relationTypeName;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public String getParentrelaId() {
		return parentrelaId;
	}

	public void setParentrelaId(String parentrelaId) {
		this.parentrelaId = parentrelaId;
	}

}
