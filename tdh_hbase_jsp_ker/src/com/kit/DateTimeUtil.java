package com.kit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

	public static String getMin(String commitTime, String reachTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String cc = null;
		try {
			if (commitTime.equalsIgnoreCase("null")
					|| reachTime.equalsIgnoreCase("null") || commitTime == null
					|| reachTime == null) {
				return String.valueOf(0)+"分钟";
			}
			Date date1 = sdf.parse(reachTime);
			Date date2 = sdf.parse(commitTime);
			long l = date2.getTime() - date1.getTime();
			long day = l / (24 * 60 * 60 * 1000);
			long hour = (l / (60 * 60 * 1000) - day * 24);
//			long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
//			long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60
//					- min * 60);
//			System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒");
			long ms = (l / (24 * 60 * 60 * 1000) * 24
					+ (l / (60 * 60 * 1000) - day * 24)) * 60
					+ ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
			System.out.println(ms);
			cc = String.valueOf(ms);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cc+"分钟";
	}

}
