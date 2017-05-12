package com.kit.hadoop.kpi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 
 * @author Administrator remote_addr：60.208.6.156 //用户IP地址
 * 
 *         remote_user：- //用户名称
 * 
 *         time_local：[18/Sep/2013:06:49:48 +0000] //记录访问时间
 * 
 *         request："GET /wp-content/uploads/2013/07/rcassandra.png HTTP/1.0"
 *         //记录访问的url与http协议
 * 
 *         status：200 //记录请求状态，成功是200
 * 
 *         body_bytes_sent：185524 //记录发给客户端内容的大小
 * 
 *         http_referer："http://cos.name/category/software/packages/"
 *         //记录从哪个页面访问过来的
 * 
 *         http_user_agent：
 *         "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36"
 *         //记录客户浏览器的信息
 *         
 *         KPI类可以解析每一条日志记录，并存储相应信息。
 */
public class KPI {
	private String remote_addr;
	private String remote_user;
	private String time_local;
	private String request;
	private String status;
	private String body_bytes_sent;
	private String http_refer;
	private String http_user_agent;
	private boolean valid = true;;

	public static KPI parser(String log) {
		System.out.println(log);
		String[] array = log.split(" ");
		StringBuilder sb = new StringBuilder();
		KPI kpi = new KPI();
		if (array.length > 11) {
			kpi.setRemote_addr(array[0]);
			kpi.setRemote_user(array[1]);
			kpi.setTime_local(array[3]);
			kpi.setRequest(array[6]);
			kpi.setStatus(array[8]);
			kpi.setBody_bytes_sent(array[9]);
			kpi.setHttp_refer(array[10]);
			if (array.length > 12) {
				for (int i = 11; i < array.length; i++) {
					sb.append(array[i]);

				}
				kpi.setHttp_user_agent(sb.toString());
			} else {
				kpi.setHttp_user_agent(array[11]);
			}

			if (Integer.parseInt(kpi.getStatus()) >= 400) {
				kpi.valid = false;
			}

		} else {
			kpi.valid = false;
		}

		return kpi;

	}

	public static KPI filterPVs(String log) {
		KPI kpi = KPI.parser(log);
		Set<String> pages = new HashSet<String>();
		pages.add("/about");
		pages.add("/black-ip-list/");
		pages.add("/cassandra-clustor/");
		pages.add("/finance-rhive-repurchase/");
		pages.add("/hadoop-family-roadmap/");
		pages.add("hadoop-hive-intro");
		pages.add("/hadoop-zookeeper-intro/");
		pages.add("/hadoop-mahout-roadmap/");

		if (!pages.contains(kpi.getRequest())) {
			kpi.setValid(false);
		}
		return kpi;
	}

	public static KPI filterIPs(String log) {
		KPI kpi = KPI.parser(log);
		Set<String> pages = new HashSet<String>();
		pages.add("/about");
		pages.add("/black-ip-list/");
		pages.add("/cassandra-clustor/");
		pages.add("/finance-rhive-repurchase/");
		pages.add("/hadoop-family-roadmap/");
		pages.add("hadoop-hive-intro");
		pages.add("/hadoop-zookeeper-intro/");
		pages.add("/hadoop-mahout-roadmap/");

		if (!pages.contains(kpi.getRequest())) {
			kpi.setValid(false);
		}
		return kpi;
	}

	public static KPI filterBrowser(String log) {
		return KPI.parser(log);
	}

	public static KPI filterTime(String log) {
		return KPI.parser(log);
	}
	public static KPI filterDomain(String log) {
		return KPI.parser(log);
	}

	public String getRemote_addr() {
		return remote_addr;
	}
	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}
	public String getRemote_user() {
		return remote_user;
	}
	public void setRemote_user(String remote_user) {
		this.remote_user = remote_user;
	}

	public String getTime_local() {
		return time_local;
	}
	public void setTime_local(String time_local) {
		SimpleDateFormat sd = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss",
				Locale.US);
		Date date = new Date();
		try {
			date = sd.parse(time_local.substring(1));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		this.time_local = sdf.format(date);

	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {

		this.request = request;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBody_bytes_sent() {
		return body_bytes_sent;
	}
	public void setBody_bytes_sent(String body_bytes_sent) {
		this.body_bytes_sent = body_bytes_sent;
	}
	public String getHttp_refer() {
		return http_refer;
	}
	public void setHttp_refer(String http_refer) {
		String str = http_refer.replace("\"", "").replace("https://", "")
				.replace("http://", "");
		http_refer = str.indexOf("/") > 0
				? str.substring(0, str.indexOf("/"))
				: str;
		// System.out.println("http_refer: " + http_refer);
		this.http_refer = http_refer;
	}
	public String getHttp_user_agent() {
		return http_user_agent;
	}
	public void setHttp_user_agent(String http_user_agent) {
		this.http_user_agent = http_user_agent;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder();
		String log = "222.68.172.190 - - [18/Sep/2013:06:49:57 +0000] \"GET /images/my.jpg HTTP/1.1\" 200 19939 \"http://www.angularjs.cn/A00n\" \"Mozilla/5.0 "
				+ "(Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36\"";
		// String[] array = log.split(" ");
		// KPI kpi = new KPI();
		// kpi.setRemote_addr(array[0]);
		// kpi.setRemote_user(array[1]);
		// kpi.setTime_local(array[3]);
		// kpi.setRequest(array[6]);
		// kpi.setStatus(array[8]);
		// kpi.setBody_bytes_sent(array[9]);
		// kpi.setHttp_refer(array[10]);
		// for(int i = 11; i < array.length; i++)
		// {
		// sb.append(array[i]);
		//
		// }
		// kpi.setHttp_user_agent(sb.toString());
		KPI kpi = KPI.parser(log);
		System.out.println(kpi.getRemote_addr());
		System.out.println(kpi.getRemote_user());
		System.out.println(kpi.getTime_local());
		System.out.println(kpi.getRequest());
		System.out.println(kpi.getStatus());
		System.out.println(kpi.getBody_bytes_sent());
		System.out.println(kpi.getHttp_refer());
		System.out.println(kpi.getHttp_user_agent());

	}

}
