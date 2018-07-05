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
@TableName("metamodel_classgroup")
public class MetamodelClassgroup implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类组合关系id
     */
    @TableId("classgroup_id")
	private String classgroupId = UUID.randomUUID().toString();
    /**
     * 类id
     */
	@TableField("class_id")
	private String classId;
    /**
     * 关系代码
     */
	@TableField("relationship_code")
	private String relationshipCode;
    /**
     * 关系名称
     */
	@TableField("relationship_name")
	private String relationshipName;
    /**
     * 被组合类id
     */
	@TableField("classgrouped_id")
	private String classgroupedId;
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
	private String classCode;
	
	@TableField(exist = false)
	private String className;
	
	@TableField(exist = false)
	private String classCoded;
	
	@TableField(exist = false)
	private String classNamed;
	
	public String getClassgroupId() {
		return classgroupId;
	}

	public void setClassgroupId(String classgroupId) {
		this.classgroupId = classgroupId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getRelationshipCode() {
		return relationshipCode;
	}

	public void setRelationshipCode(String relationshipCode) {
		this.relationshipCode = relationshipCode;
	}

	public String getRelationshipName() {
		return relationshipName;
	}

	public void setRelationshipName(String relationshipName) {
		this.relationshipName = relationshipName;
	}

	public String getClassgroupedId() {
		return classgroupedId;
	}

	public void setClassgroupedId(String classgroupedId) {
		this.classgroupedId = classgroupedId;
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
