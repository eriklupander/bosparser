package se.lu.bos.model.ext;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-08
 * Time: 12:46
 * To change this template use File | Settings | File Templates.
 */
@Test
public class PilotFactoryTest {

    @Test
    public void testEnlisted() {
        for(int a = 0; a < 20; a++) {
            Pilot p = PilotFactory.getEnlistedPilot();
            assertNotNull(p);
            assertNotNull(p.getFirstName());
            assertNotNull(p.getLastName());
            assertEquals(p.getRank().getRankTypeCode(), 0);
            System.out.println(p);
        }
    }

    @Test
    public void testNco() {
        for(int a = 0; a < 20; a++) {
            Pilot p = PilotFactory.getNcoPilot();
            assertNotNull(p);
            assertNotNull(p.getFirstName());
            assertNotNull(p.getLastName());
            assertEquals(p.getRank().getRankTypeCode(), 1);
            System.out.println(p);
        }
    }

    @Test
    public void testOfficer() {
        for(int a = 0; a < 20; a++) {
            Pilot p = PilotFactory.getOfficerPilot();
            assertNotNull(p);
            assertNotNull(p.getFirstName());
            assertNotNull(p.getLastName());
            assertEquals(p.getRank().getRankTypeCode(), 2);
            System.out.println(p);
        }
    }
}
