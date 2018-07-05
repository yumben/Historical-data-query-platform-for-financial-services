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
@TableName("metamodel_datatype")
public class MetamodelDatatype implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据类型id
     */
    @TableId("datatype_id")
	private String datatypeId = UUID.randomUUID().toString();
    /**
     * 包id
     */
	@TableField("package_id")
	private String packageId;
    /**
     * 数据类型代码
     */
	@TableField("datatype_code")
	private String datatypeCode;
    /**
     * 数据类型名称
     */
	@TableField("datatype_name")
	private String datatypeName;
    /**
     * 数据类型描述
     */
	@TableField("datatype_describe")
	private String datatypeDescribe;
    /**
     * 可见性
     */
	private String visibility;
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


	public String getDatatypeId() {
		return datatypeId;
	}

	public void setDatatypeId(String datatypeId) {
		this.datatypeId = datatypeId;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getDatatypeCode() {
		return datatypeCode;
	}

	public void setDatatypeCode(String datatypeCode) {
		this.datatypeCode = datatypeCode;
	}

	public String getDatatypeName() {
		return datatypeName;
	}

	public void setDatatypeName(String datatypeName) {
		this.datatypeName = datatypeName;
	}

	public String getDatatypeDescribe() {
		return datatypeDescribe;
	}

	public void setDatatypeDescribe(String datatypeDescribe) {
		this.datatypeDescribe = datatypeDescribe;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
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
