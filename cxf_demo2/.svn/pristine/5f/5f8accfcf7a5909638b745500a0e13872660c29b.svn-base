package com.kit.jaxws;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import com.kit.UserInfo;
import com.kit.UserInfoService;
import com.kit.util.CXFClientUtil;

/**
 * 
 * @author gh
 * @createtime 2016-12-19
 * @description 创建客户端 调用服务接口
 *
 */
public class InvokeServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JaxWsProxyFactoryBean factoryBean = new JaxWsProxyFactoryBean();
		factoryBean.setServiceClass(UserInfoService.class);
		factoryBean.setAddress("http://localhost:8082/userInfo"); // webService对应的访问地址
		UserInfoService hw = (UserInfoService) factoryBean.create();
		CXFClientUtil.configTimeout(hw);// 设置时间超时
		UserInfo user = new UserInfo();
		user.setName("yang");
		 System.out.println(hw.getUserInfo(user)); //在接口实现类下 选择是否暴露给客服端
		// 如果是true 则表示不暴露 表示不能够调用此方法
		System.out.println(hw.getUserInfoByKey("zhangsan"));
	}

}
