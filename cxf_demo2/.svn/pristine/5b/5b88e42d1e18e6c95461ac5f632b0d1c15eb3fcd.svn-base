package com.kit.server;

import org.apache.cxf.frontend.ServerFactoryBean;

import com.kit.UserInfoService;
import com.kit.UserInfoServiceImpl;
  
/**
 * 
 * @author gh
 *@createtime 2016-12-21
 *@description 
 *ServerFactoryBean为JaxWsServerFactoryBean的父类不可以发布soap1.2版本的协议
 */

public class FactoryServer {  
  
    public static void main(String[] args) {  
    	System.out.println("start+++++++++,the model is ServerFactoryBean");
        
    	UserInfoServiceImpl userinfoserviceimpl=new UserInfoServiceImpl();
        ServerFactoryBean factoryBean=new ServerFactoryBean();
        factoryBean.setAddress("http://localhost:8083/userinfo");//webService对应的访问地址  
        factoryBean.setServiceClass(UserInfoService.class);//指定webService服务类型  
        factoryBean.setServiceBean(userinfoserviceimpl);//webService对应的bean对象  
        factoryBean.create();//这样就创建了一个webService的服务端
        System.out.println("webservice start success!");
    }
}  
