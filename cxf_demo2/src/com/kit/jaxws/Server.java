package com.kit.jaxws;

import javax.xml.ws.Endpoint;

import com.kit.UserInfoService;
import com.kit.UserInfoServiceImpl;

/**
 * 
 * @author gh
 * @createtime 2016-12-19
 * @description 创建服务端 启动服务发布接口
 * 与jaxwsserver的发布方法不一样
 *
 */
public class Server {  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
    	System.out.println("start+++++++++,the model is JaxWsServerFactoryBean");
        UserInfoService implementor = new UserInfoServiceImpl();    
        String address = "http://localhost:8082/userInfo";  //webService对应的访问地址  
        Endpoint.publish(address, implementor);  //创建了一个webService的服务端  
        System.out.println("web service started");  
//        
    }  
  
}  
