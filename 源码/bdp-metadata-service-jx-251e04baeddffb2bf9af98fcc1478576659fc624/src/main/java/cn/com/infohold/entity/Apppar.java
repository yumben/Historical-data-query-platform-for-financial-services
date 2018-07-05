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
 * @since 2017-08-03
 */
@TableName("apppar")
public class Apppar implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("id")
   	private String id = UUID.randomUUID().toString();
    
    @TableField("name")
	private String name;
    
	@TableField("code")
	private String code;
   
	@TableField("value")
	private String value;
   
	@TableField("language")
	private String language;
	
	@TableField("showmsg")
	private String showmsg;
	
	@TableField("describe")
	private String describe;
	
	@TableField("scope")
	private String scope;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getShowmsg() {
		return showmsg;
	}

	public void setShowmsg(String showmsg) {
		this.showmsg = showmsg;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}




}
