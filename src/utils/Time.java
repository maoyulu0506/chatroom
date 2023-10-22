package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

//该类的作用是用来获取当前的日期和时间的
public class Time{
    public static String getDate(){
        Date d = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return sdf.format(d);
    }
}