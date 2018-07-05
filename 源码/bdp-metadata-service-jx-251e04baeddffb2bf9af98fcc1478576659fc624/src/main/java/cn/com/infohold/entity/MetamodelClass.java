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
@TableName("metamodel_class")
public class MetamodelClass implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 类id
     */
    @TableId("class_id")
	private String classId = UUID.randomUUID().toString();
    /**
     * 类代码
     */
	@TableField("class_code")
	private String classCode;
    /**
     * 类名称
     */
	@TableField("class_name")
	private String className;
    /**
     * 描述信息
     */
	private String describes;
    /**
     * 入口类型
     */
	private String type;
    /**
     * 是否显示
     */
	private String isshow;
    /**
     * 是否可增加
     */
	private String isadd;
    /**
     * 显示图标
     */
	@TableField("display_icon")
	private String displayIcon;
    /**
     * 包id
     */
	@TableField("package_id")
	private String packageId;
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


	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
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

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsshow() {
		return isshow;
	}

	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}

	public String getIsadd() {
		return isadd;
	}

	public void setIsadd(String isadd) {
		this.isadd = isadd;
	}

	public String getDisplayIcon() {
		return displayIcon;
	}

	public void setDisplayIcon(String displayIcon) {
		this.displayIcon = displayIcon;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
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
