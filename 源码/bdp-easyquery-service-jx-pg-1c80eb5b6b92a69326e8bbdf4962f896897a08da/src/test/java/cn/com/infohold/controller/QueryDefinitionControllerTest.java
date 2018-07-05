/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.infohold.controller;

import bdp.commons.easyquery.ret.QueryRetBean;
import cn.easybdp.basic.util.http.BasicHttpUtil;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lfh
 */
public class QueryDefinitionControllerTest {
     public void testDefinitionGet() throws IOException {
        String query_template_id = "afec4080-acd7-4418-bf05-576d5be68eca";
        //String token = "{\"unit_id\":\"18521\"}";
        Map<String, String> param = new HashMap<>();
        //param.put("token", token);
        param.put("query_template_id", query_template_id);
        String url = "http://localhost:9898/definitionGet";

        BasicHttpUtil btu = BasicHttpUtil.getInstance(); 
        String data = btu.postRequst(url, param);
        System.out.println("data=" + data);
    }

    public static void main(String args[]) throws IOException {
        QueryDefinitionControllerTest mtt = new QueryDefinitionControllerTest();
        mtt.testDefinitionGet();
    }
}
