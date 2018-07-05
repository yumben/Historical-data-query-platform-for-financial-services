package cn.com.infohold.service;

import bdp.commons.easyquery.ret.EasyQuery;

public interface IEasyQueryService {

	EasyQuery selectEasyQueryTemplate(String id, String token) throws Exception;

}
