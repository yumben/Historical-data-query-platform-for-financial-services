package cn.com.infohold.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class MetadataProperty implements Serializable {

	private static final long serialVersionUID = 1L;

	private String metadataPropertyId;
	private String metadataId;
	private String classPropertyId;
	private String propertyValue;
	private String createDate;
	private String createName;
	private String editDate;
	private String editName;
	private String propertyCode;

}
