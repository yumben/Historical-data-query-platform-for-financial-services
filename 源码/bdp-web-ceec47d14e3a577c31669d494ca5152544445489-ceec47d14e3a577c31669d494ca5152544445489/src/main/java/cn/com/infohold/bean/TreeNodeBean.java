package cn.com.infohold.bean;

import java.util.List;

import lombok.Data;

@Data
public class TreeNodeBean {
	private String key;
	private String code;
	private String text;
	private String nodeType;
	private List<TreeNodeBean> nodes;
}
