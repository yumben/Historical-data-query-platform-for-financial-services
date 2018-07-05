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
@TableName("metamodel_datatype_enumerate")
public class MetamodelDatatypeEnumerate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 枚举id
     */
    @TableId("enumerate_id")
	private String enumerateId = UUID.randomUUID().toString();
    /**
     * 数据类型id
     */
	@TableField("datatype_id")
	private String datatypeId;
    /**
     * 枚举项id
     */
	@TableField("enumitems_id")
	private String enumitemsId;
    /**
     * 枚举代码
     */
	@TableField("enumerate_code")
	private String enumerateCode;
    /**
     * 枚举名称
     */
	@TableField("enumerate_name")
	private String enumerateName;
    /**
     * 枚举描述
     */
	@TableField("enumerate_describe")
	private String enumerateDescribe;
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


	public String getEnumerateId() {
		return enumerateId;
	}

	public void setEnumerateId(String enumerateId) {
		this.enumerateId = enumerateId;
	}

	public String getDatatypeId() {
		return datatypeId;
	}

	public void setDatatypeId(String datatypeId) {
		this.datatypeId = datatypeId;
	}

	public String getEnumitemsId() {
		return enumitemsId;
	}

	public void setEnumitemsId(String enumitemsId) {
		this.enumitemsId = enumitemsId;
	}

	public String getEnumerateCode() {
		return enumerateCode;
	}

	public void setEnumerateCode(String enumerateCode) {
		this.enumerateCode = enumerateCode;
	}

	public String getEnumerateName() {
		return enumerateName;
	}

	public void setEnumerateName(String enumerateName) {
		this.enumerateName = enumerateName;
	}

	public String getEnumerateDescribe() {
		return enumerateDescribe;
	}

	public void setEnumerateDescribe(String enumerateDescribe) {
		this.enumerateDescribe = enumerateDescribe;
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
