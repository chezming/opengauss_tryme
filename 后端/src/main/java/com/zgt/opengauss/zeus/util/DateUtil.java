package com.zgt.opengauss.zeus.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhangrb
 * @date 2021/8/30
 */
public class DateUtil {

    public static Date getTimeAddHour(Integer num) {
        String oneHoursAgoTime="";
        Date dt = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        //rightNow.add(Calendar.DATE, -1);
        rightNow.add(Calendar.HOUR, num);
        oneHoursAgoTime = sdf.format(rightNow.getTime());
        return rightNow.getTime();
    }

    public static Date getTimeAddDay(Integer num) {
        Date dt = new Date();
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.DAY_OF_MONTH, num);
        return rightNow.getTime();
    }
}
