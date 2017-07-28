package com.kit.server;

import java.rmi.RemoteException;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.kit.UserInfoService;
/**
 * 
 * @author gh
 * @createtime 2016-12-19
 * @description 创建客户端 调用服务接口
 *
 */
public class InvokeFactoryServer {
	public static void main(String[] args) throws RemoteException {
		ClientProxyFactoryBean factoryBean=new ClientProxyFactoryBean();
        factoryBean.setAddress("http://localhost:8083/userinfo");//指定发送请求的webService地址  
        factoryBean.setServiceClass(UserInfoService.class);//指定webService对应的接口  
      //因为我的soap协议是在http 协议的基础上传送xml 格式的数据
      //http 协议 request/response
        factoryBean.getInInterceptors().add(new LoggingInInterceptor());  //添加请求的消息拦截器...
        factoryBean.getOutInterceptors().add(new LoggingOutInterceptor());	//添加响应的消息拦截器...
        UserInfoService userinfoservice=(UserInfoService) factoryBean.create();//创建一个针对WebService对应接口的代理类  
        System.out.println(userinfoservice.getUserInfoByKey("user-name1"));//调用代理类的对应方法，发送相关请求到对应Server  
       
	}
}
