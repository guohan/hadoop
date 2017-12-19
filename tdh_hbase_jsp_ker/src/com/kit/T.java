package com.kit;

import java.text.SimpleDateFormat;
import java.util.Date;

public class T {

	
    public static void main(String[] args) throws Exception {  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        Date date1 = sdf.parse("2017-04-07 09:06:11.0");  
        Date date2 = sdf.parse("2017-04-07 09:06:11.0");  
        long l = date2.getTime() - date1.getTime();  
        long day = l / (24 * 60 * 60 * 1000);  
        long hour = (l / (60 * 60 * 1000) - day * 24);  
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);  
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);  
        System.out.println(day + "天" + hour + "小时" + min +"分" + s + "秒");  
        
        long day1=  (l / (24 * 60 * 60 * 1000)*24+ (l / (60 * 60 * 1000) - day * 24))*60+ ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        System.out.println(day1);
    }  
}
//row172354209_5Qualifier:  TASK_COMMIT_TIME familyinfovalue2017-04-07 09:06:11.0
//row172354209_5Qualifier:  TASK_PRIORITY familyinfovalue1
//row172354209_5Qualifier:  TASK_REACH_TIME familyinfovalue2017-04-07 09:06:11.0