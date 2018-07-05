package cn.com.infohold.entity;

import java.io.Serializable;
import java.util.UUID;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author mojiaxing
 * @since 2017-11-14
 */
@Data
@TableName("metadata_catalog")
public class MetadataCatalog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分类目录id
     */
    @TableId("catalog_id")
	private String catalogId = UUID.randomUUID().toString();
    /**
     * 分类目录代码
     */
	@TableField("catalog_code")
	private String catalogCode;
    /**
     * 分类目录名称
     */
	@TableField("catalog_name")
	private String catalogName;
    /**
     * 上级目录id
     */
	@TableField("parent_catalog")
	private String parentCatalog;
    /**
     * 分类目录类型
     */
	@TableField("catalog_type")
	private String catalogType;
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
	private String contextCatalog;

}
