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
@TableName("metamodel_datatype_enumitems")
public class MetamodelDatatypeEnumitems implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 枚举项id
     */
    @TableId("enumitems_id")
	private String enumitemsId = UUID.randomUUID().toString();
    /**
     * 枚举项代码
     */
	@TableField("enumitems_code")
	private String enumitemsCode;
    /**
     * 枚举项名称
     */
	@TableField("enumitems_name")
	private String enumitemsName;
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


	public String getEnumitemsId() {
		return enumitemsId;
	}

	public void setEnumitemsId(String enumitemsId) {
		this.enumitemsId = enumitemsId;
	}

	public String getEnumitemsCode() {
		return enumitemsCode;
	}

	public void setEnumitemsCode(String enumitemsCode) {
		this.enumitemsCode = enumitemsCode;
	}

	public String getEnumitemsName() {
		return enumitemsName;
	}

	public void setEnumitemsName(String enumitemsName) {
		this.enumitemsName = enumitemsName;
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

}
