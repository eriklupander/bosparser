package se.lu.bos.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-12-28
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
@Test
public class TimeUtilTest {
    public void testTimeToSecondOfDayHalfHour() {
        Integer secondsOfDay = TimeUtil.toSeconds("00:30:00");
        assertEquals(secondsOfDay, new Integer(1800));
    }

    public void testTimeToSecondOfDayOnePlusHalfHour() {
        Integer secondsOfDay = TimeUtil.toSeconds("01:30:00");
        assertEquals(secondsOfDay, new Integer(5400));
    }

    public void testTimeFromSecondsOnePlusHalfHour() {
        String hm = TimeUtil.fromSeconds(5400);
        assertEquals(hm, "1 hour and 30 minutes");
    }

    public void testTimeFromSecondsTwoDaysOnePlusHalfHour() {
        String hm = TimeUtil.fromSeconds(24*5400+130);
        assertEquals(hm, "1 day, 12 hours, 2 minutes and 10 seconds");
    }
}
