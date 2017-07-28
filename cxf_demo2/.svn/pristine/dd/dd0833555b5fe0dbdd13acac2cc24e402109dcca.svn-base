package com.kit;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
/**
 * 
 * @author gh
 * @createtime 2016-12-19
 * @description 定义接口实现类 实现相关接口方法
 *
 */
@WebService
public class UserInfoServiceImpl implements UserInfoService {

	public String getUserInfoByKey(String name) {
		// TODO Auto-generated method stub
		return "the request user name is " + name;
	}

	@WebMethod(exclude = false) // 服务端测试 设置该方法是否暴露给客户端 true 表示不暴露
	public String getUserInfo(UserInfo user) {
		// TODO Auto-generated method stub
		return "the request user name is " + user.getName();
	}

}
