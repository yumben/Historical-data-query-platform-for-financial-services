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
@TableName("metamodel_relation")
public class MetamodelRelation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 关联关系id
     */
    @TableId("relation_id")
	private String relationId = UUID.randomUUID().toString();
    /**
     * 关联关系代码
     */
	@TableField("relation_code")
	private String relationCode;
    /**
     * 关联关系名称
     */
	@TableField("relation_name")
	private String relationName;
    /**
     * 类id
     */
	@TableField("class_id")
	private String classId;
    /**
     * 被关联类id
     */
	@TableField("classed_id")
	private String classedId;
    /**
     * 关联类型
     */
	@TableField("relation_type_id")
	private String relationTypeId;
    /**
     * 描述信息
     */
	private String describes;
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
	private String relationTypeName;
	
	@TableField(exist = false)
	private String classCode;
	
	@TableField(exist = false)
	private String className;
	
	@TableField(exist = false)
	private String classCoded;
	
	@TableField(exist = false)
	private String classNamed;

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getRelationCode() {
		return relationCode;
	}

	public void setRelationCode(String relationCode) {
		this.relationCode = relationCode;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassedId() {
		return classedId;
	}

	public void setClassedId(String classedId) {
		this.classedId = classedId;
	}

	public String getRelationTypeId() {
		return relationTypeId;
	}

	public void setRelationTypeId(String relationTypeId) {
		this.relationTypeId = relationTypeId;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
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

	public String getRelationTypeName() {
		return relationTypeName;
	}

	public void setRelationTypeName(String relationTypeName) {
		this.relationTypeName = relationTypeName;
	}

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassCoded() {
		return classCoded;
	}

	public void setClassCoded(String classCoded) {
		this.classCoded = classCoded;
	}

	public String getClassNamed() {
		return classNamed;
	}

	public void setClassNamed(String classNamed) {
		this.classNamed = classNamed;
	}

}
