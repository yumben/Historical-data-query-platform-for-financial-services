package cn.com.infohold.entity;

import java.io.Serializable;
import java.util.UUID;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@TableName("metadata_property_key")
public class MetadataPropertyKey implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 元数据属性内容id
     */
	private String id = UUID.randomUUID().toString();
    /**
     * 元数据属性内容代码描述
     */
	private String codedescribe;
    /**
     * 元数据属性内容代码
     */
	private String code;
    /**
     * 元数据属性内容值描述
     */
	private String valuedescribe;
    /**
     * 元数据属性内容值
     */
	private String value;
    /**
     * 元数据属性内容类型描述
     */
	private String typedescribe;
    /**
     * 元数据属性内容类型
     */
	private String type;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodedescribe() {
		return codedescribe;
	}

	public void setCodedescribe(String codedescribe) {
		this.codedescribe = codedescribe;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValuedescribe() {
		return valuedescribe;
	}

	public void setValuedescribe(String valuedescribe) {
		this.valuedescribe = valuedescribe;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTypedescribe() {
		return typedescribe;
	}

	public void setTypedescribe(String typedescribe) {
		this.typedescribe = typedescribe;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
