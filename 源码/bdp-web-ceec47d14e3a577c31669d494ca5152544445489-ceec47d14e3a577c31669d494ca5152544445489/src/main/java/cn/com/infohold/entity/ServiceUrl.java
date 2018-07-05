package cn.com.infohold.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

import com.baomidou.mybatisplus.annotations.TableField;

/**
 * <p>
 * 
 * </p>
 *
 * @author huangdi
 * @since 2017-11-02
 */
@Data
@TableName("service_url")
public class ServiceUrl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * url的id
     */
    @TableId(value = "url_id")
	private String urlId = UUID.randomUUID().toString();;
    /**
     * url代码
     */
	@TableField("url_code")
	private String urlCode;
    /**
     * url名称
     */
	@TableField("url_name")
	private String urlName;
    /**
     * url地址值
     */
	@TableField("url_value")
	private String urlValue;


}
