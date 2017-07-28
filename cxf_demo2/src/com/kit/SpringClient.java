package com.kit;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author Administrator
 *http://localhost:8080/cxf_demo2/webservice/helloWorld?wsdl
 */
public class SpringClient {  
    
    public static void main(String args[]) {  
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");  
//        UserInfoService hw = context.getBean("UserInfo", UserInfoService.class);  
//        String response = hw.getUserInfoByKey("username");
//        System.out.println(response);  
        System.out.println(context.getBean("client", UserInfoService.class).getUserInfoByKey("username1"));  
    }  
      
}  