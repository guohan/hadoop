package com.kit.jaxws;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

import com.kit.UserInfoService;
import com.kit.UserInfoServiceImpl;
/**
 * 
 * @author gh
 * @createtime 2016-12-19
 * @description 创建服务端 启动服务暴露接口
 *JaxWsServerFactoryBean 为 ServerFactoryBean 子类，功能增强类,可以发布soap1.2版本的协议
 */
public class JaxwsServer {
public static void main(String[] args) {
	System.out.println("start+++++++++,the model is JaxWsServerFactoryBean");
	 JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();  
     factory.setServiceClass(UserInfoServiceImpl.class);  
     factory.setAddress("http://localhost:8081/userInfo");  
     Server server = factory.create();  
     server.start();  
     System.out.println("start success!");
}
}
