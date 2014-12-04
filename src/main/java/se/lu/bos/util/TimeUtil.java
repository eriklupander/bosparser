package se.lu.bos.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-12-03
 * Time: 22:18
 * To change this template use File | Settings | File Templates.
 */
public class TimeUtil {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String gameTickToTime(Integer tick) {

        DateTime dt = DateTime.now();
        dt = dt.withField(DateTimeFieldType.millisOfDay(), tick*20);
        return DateTimeFormat.forPattern("HH:mm:ss").print(dt);
    }

    public static String pad(String s) {
        String[] parts = s.split(":");
        StringBuilder buf = new StringBuilder();
        for(String part : parts) {
            if(part.length() == 1) {
                buf.append("0").append(part).append(":");
            } else {
                buf.append(part).append(":");
            }
        }
        if(buf.length() > 0) buf.setLength(buf.length() - 1);
        return buf.toString();
    }

    public static String parseDate(Date date) {
        return sdf.format(date);
    }
}
