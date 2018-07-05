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
@TableName("metamodel_classproperty")
public class MetamodelClassproperty implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 元模型类属性id
     */
    @TableId("property_id")
	private String propertyId = UUID.randomUUID().toString();
    /**
     * 数据类型id
     */
	@TableField("datatype_id")
	private String datatypeId;
    /**
     * 类id
     */
	@TableField("class_id")
	private String classId;
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
     * 继承性
     */
	private String inheritance;
    /**
     * 是否为空
     */
	private String iscannull;
    /**
     * 是否显示
     */
	private String isshow;
    /**
     * 是否编辑
     */
	private String isedit;
    /**
     * 是否加密
     */
	private String ispassword;
    /**
     * 长度
     */
	private String lengths;
    /**
     * 默认值
     */
	@TableField("default_value")
	private String defaultValue;
    /**
     * 显示顺序
     */
	@TableField("display_order")
	private String displayOrder;
    /**
     * 编辑控件名称
     */
	@TableField("editcontrol_name")
	private String editcontrolName;
    /**
     * 继承自那个父类id
     */
	@TableField("inheritance_id")
	private String inheritanceId;
    /**
     * 继承的父类的属性对应的id
     */
	@TableField("parent_property_id")
	private String parentPropertyId;
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
	private String datetypeCode;
	
	@TableField(exist = false)
	private String datetypeName;

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getDatatypeId() {
		return datatypeId;
	}

	public void setDatatypeId(String datatypeId) {
		this.datatypeId = datatypeId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
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

	public String getInheritance() {
		return inheritance;
	}

	public void setInheritance(String inheritance) {
		this.inheritance = inheritance;
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

	public String getIsedit() {
		return isedit;
	}

	public void setIsedit(String isedit) {
		this.isedit = isedit;
	}

	public String getIspassword() {
		return ispassword;
	}

	public void setIspassword(String ispassword) {
		this.ispassword = ispassword;
	}

	public String getLengths() {
		return lengths;
	}

	public void setLengths(String lengths) {
		this.lengths = lengths;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(String displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getEditcontrolName() {
		return editcontrolName;
	}

	public void setEditcontrolName(String editcontrolName) {
		this.editcontrolName = editcontrolName;
	}

	public String getInheritanceId() {
		return inheritanceId;
	}

	public void setInheritanceId(String inheritanceId) {
		this.inheritanceId = inheritanceId;
	}

	public String getParentPropertyId() {
		return parentPropertyId;
	}

	public void setParentPropertyId(String parentPropertyId) {
		this.parentPropertyId = parentPropertyId;
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

	public String getDatetypeCode() {
		return datetypeCode;
	}

	public void setDatetypeCode(String datetypeCode) {
		this.datetypeCode = datetypeCode;
	}

	public String getDatetypeName() {
		return datetypeName;
	}

	public void setDatetypeName(String datetypeName) {
		this.datetypeName = datetypeName;
	}
	

}
