/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.infohold.controller;

import com.jcabi.aspects.Loggable;
import java.io.IOException;

/**
 *
 * @author lfh
 */
@Loggable(Loggable.DEBUG)
public class TestLoggable {
    public String testlog( String name ){
        return "hello " +name ;
    }
    
    public static void main(String args[]) throws IOException { 
        TestLoggable t = new TestLoggable();
        t.testlog(" bdp");
    }
    
}
