/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.infohold.controller;

import cn.easybdp.basic.util.http.BasicHttpUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lfh
 */
public class MetadataDictTypeControllerTest {

    public void testSelectMetadataPK() throws IOException {
        String metadata_id = "36377,36376,36382,35561,35581,35588";
        String token = "{\"unit_id\":\"18521\"}";
        Map<String, String> param = new HashMap<>();
        param.put("token", token);
        param.put("metadata_id", metadata_id);
        String url = "http://localhost:8793/metadataDictType/selectMetadataPK";

        BasicHttpUtil btu = BasicHttpUtil.getInstance(); 
        String data = btu.postRequst(url, param);
        System.out.println("data=" + data);
    }
    
     public void testGetMetadataJsonById() throws IOException {
        String metadataId = "36377"; 
        Map<String, String> param = new HashMap<>(); 
        param.put("metadataId", metadataId);
        String url = "http://localhost:8793/metadata/getMetadataJsonById";

        BasicHttpUtil btu = BasicHttpUtil.getInstance(); 
        String data = btu.postRequst(url, param);
        System.out.println("data=" + data);
    }

    public static void main(String args[]) throws IOException {
        MetadataDictTypeControllerTest mtt = new MetadataDictTypeControllerTest();
        mtt.testSelectMetadataPK();
        //mtt.testGetMetadataJsonById();
    }

}
