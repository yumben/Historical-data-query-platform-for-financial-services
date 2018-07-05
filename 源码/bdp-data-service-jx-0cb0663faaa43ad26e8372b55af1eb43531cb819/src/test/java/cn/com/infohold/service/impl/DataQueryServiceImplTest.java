/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.infohold.service.impl;

import bdp.commons.dataservice.param.QueryBean;
import bdp.commons.dataservice.ret.RetBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;
import cn.com.infohold.util.AnalysisUtil;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author lfh
 */
public class DataQueryServiceImplTest {
     
    
    public static void geneOne(String encoding, String path) throws IOException, Exception {
        File jfile = new File(path);
        String out = jfile.getAbsolutePath() + ".out.json";
        String json = FileUtils.readFileToString(jfile, encoding);
 
        System.out.println("json=" + json);
        DataQueryServiceImpl pqs = new DataQueryServiceImpl();

        long start = System.currentTimeMillis();
        QueryBean queryBean = AnalysisUtil.toQueryBean(json, QueryBean.class);
        RetBean outdata = pqs.queryListByJson(queryBean);
        long end = System.currentTimeMillis();

        System.out.println("use " + (end - start) + " ms");

        File outfile = new File(out);
        String outs = BasicJsonUtil.getInstance().toJsonString(outdata);
        FileUtils.write(outfile, outs, encoding);
    }

    
    public static void main(String[] args) throws IOException, Exception {
        String encoding = "UTF-8";
        String path = "D:\\Downloads\\url和json报文\\url和json报文\\数据服务.json";
         geneOne(encoding, path); 
    }

}
