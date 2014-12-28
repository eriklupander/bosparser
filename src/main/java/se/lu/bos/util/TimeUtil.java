package se.lu.bos.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Period;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormat;

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
    private static DateTimeFormatter timePattern = DateTimeFormat.forPattern("HH:mm:ss");

    public static String gameTickToTime(Integer tick) {

        DateTime dt = DateTime.now();
        dt = dt.withField(DateTimeFieldType.millisOfDay(), tick*20);
        return timePattern.print(dt);
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

    public static String fromSeconds(Integer seconds) {
        //Seconds s = Seconds.seconds(seconds);
        return PeriodFormat.getDefault().print(new Period(0, 0, seconds, 0)
                .normalizedStandard());
    }

    public static Integer toSeconds(String hhmm) {
        long l = timePattern.parseDateTime(hhmm).get(DateTimeFieldType.secondOfDay());
        return new Long(l).intValue();
    }
}
