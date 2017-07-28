package com.kit.util;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

/**
 * 
 * @author gh
 * @createtime 2016-12-19
 * @description 测试发现如果服务端没有启动或网络环境差， CXF会默认等待一定的时间， 提供了代理的方式来设置超时时间
 */
public class CXFClientUtil {
	public static final int CXF_CLIENT_CONNECT_TIMEOUT = 3 * 1000;
	public static final int CXF_CLIENT_RECEIVE_TIMEOUT = 5 * 1000;

	public static void configTimeout(Object service) {
		Client proxy = ClientProxy.getClient(service);
		HTTPConduit conduit = (HTTPConduit) proxy.getConduit();
		HTTPClientPolicy policy = new HTTPClientPolicy();
		policy.setConnectionTimeout(CXF_CLIENT_CONNECT_TIMEOUT);
		policy.setReceiveTimeout(CXF_CLIENT_RECEIVE_TIMEOUT);
		conduit.setClient(policy);
	}
}
