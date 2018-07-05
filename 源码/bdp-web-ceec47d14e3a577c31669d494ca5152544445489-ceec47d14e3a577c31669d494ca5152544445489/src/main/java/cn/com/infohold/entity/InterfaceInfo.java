package cn.com.infohold.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author huangdi
 * @since 2017-09-03
 */
@Data
@TableName("interfaceinfo")
public class InterfaceInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Id
     */
	private String _id;
    
    /**
     * 请求Id
     */
	private String requestid;
    /**
     * 请求状态
     */
   
	private String state;
    /**
     * 请求参数
     */
	 @TableField(value = "paramjson")
	private String paramJson;
    /**
     * 文件路径
     */
	 @TableField(value = "filepath")
	private String filePath;
    /**
     * 备注
     */
	private String remark;

	/**
     * 用户id
     */
	private String userid;
	
	/**
	 * 功能id
	 */
	private String func_id;
	
	/**
	 * 导出时间
	 */
	private String export_time;
	
	/**
	 * 创建时间
	 */
	private String create_time;
	
	/**
	 * 文件生成时间
	 */
	private String generate_time;
	
	/**
	 * 备用字段1
	 */
	private String remark1;
	
	/**
	 * 备用字段2
	 */
	private String remark2;
	
	/**
	 * 备用字段3
	 */
	private String remark3;
	
	
	                
	public InterfaceInfo(String state) {
		super();
		this.state = state;
	}

	public InterfaceInfo() {
		super();
	}

	public InterfaceInfo(String state, String filePath,String generate_time) {
		super();
		this.state = state;
		this.filePath = filePath;
		this.generate_time = generate_time;
	}
	

}
