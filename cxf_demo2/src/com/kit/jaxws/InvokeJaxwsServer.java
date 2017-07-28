package com.kit.jaxws;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.kit.UserInfo;
import com.kit.UserInfoService;

/**
 * 
 * @author gh
 * @createtime 2016-12-19
 * @description 创建客户端 调用服务接口
 *
 */
public class InvokeJaxwsServer {
	  public static void main(String[] args) {  
	        JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();  
	        factoryBean.setServiceClass(UserInfoService.class);  
	        factoryBean.setAddress("http://localhost:8081/userInfo");  //webService对应的访问地址 
	        UserInfoService hw = (UserInfoService) factoryBean.create();  
	        UserInfo user = new UserInfo();  
	        user.setName("yang");  
	        System.out.println(hw.getUserInfo(user));  
	        System.out.println(hw.getUserInfoByKey("zhangsan"));  
	    }  
}
