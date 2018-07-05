package cn.com.infohold.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

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
public class Metadata implements Serializable {

	private static final long serialVersionUID = 1L;

	private String metadataId;
	private String metadataCode;
	private String metadataName;
	private String parentMetadata;
	private String catalogId;
	private String classId;
	private String notshow;
	private String createDate;
	private String createName;
	private String editDate;
	private String editName;
	private String className;
	private String classCode;
	private String pannelpageId;
	private String contextCatalog;
	private String beRelationMeta;
	private String relationMeta;
	private String relationName;
	private String metadataRelationed;
	private String metadataRelationId;

	private Metadata parent;

	private List<MetadataProperty> properties;

}
