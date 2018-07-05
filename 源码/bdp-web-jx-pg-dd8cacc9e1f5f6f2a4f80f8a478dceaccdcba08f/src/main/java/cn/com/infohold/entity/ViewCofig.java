package cn.com.infohold.entity;

import java.util.List;

import lombok.Data;

@Data
public class ViewCofig extends ViewTableCofig {
	private static final long serialVersionUID = 1L;
	private List<ViewColumnCofig> columns;

}
