package se.lu.bos.rest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import se.lu.bos.dao.StatsDao;
import se.lu.bos.model.*;
import se.lu.bos.rest.dto.TotalReport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 14-12-29
 * Time: 11:49
 * To change this template use File | Settings | File Templates.
 */
@Test
public class DataServiceBeanTest {

    DataServiceBean testee;

    @BeforeMethod
    public void setup() {
        testee = new DataServiceBean();
    }

    @Test
    public void testRxJavaAggregator() {


        TotalReport totalReport = testee.buildAggregateReportUsingRx(buildStatsList());
        assertNotNull(totalReport);
        assertEquals(totalReport.getNumberOfMissions(), 2);
        assertEquals(totalReport.getNumberOfMissionsSurvived().longValue(), 2);
        assertEquals(totalReport.getNumberOfMissionsDestroyed(), 0);
        assertEquals(totalReport.getNumberOfKillsByTargetType().size(), 3);
        assertTrue(totalReport.getNumberOfKillsByTargetType().keySet().contains("IL-2 mod.42"));
        assertTrue(totalReport.getNumberOfKillsByTargetType().keySet().contains("Lagg-3"));
        assertTrue(totalReport.getNumberOfKillsByTargetType().keySet().contains("Pe-2"));
        assertEquals(totalReport.getNumberOfSortiesPerPlaneType().size(), 1);
        assertTrue(totalReport.getNumberOfSortiesPerPlaneType().keySet().contains("FW-190A3"));
    }

    private List<Stats> buildStatsList() {
        List<Stats> statsList = new ArrayList<Stats>();
        for(int a = 0; a < 2 ; a++) {
            Stats stats = new Stats();
            stats.setCreated(new Date());
            stats.setFinalState(State.ALIVE);
            stats.setMissionName("ABC"+a);
            stats.setRootFileName("ROOT"+a);
            stats.setPilotPlane("FW-190A3");
            stats.setPilotName("John Doe");
            stats.setTotalDuration("00:13:45");
            stats.setKills(buildKillsList(a));
            stats.setHits(buildHitsList(a));
            statsList.add(stats);
        }
        return statsList;

    }

    private List<Hit> buildHitsList(int a) {
        List<Hit> hits = new ArrayList<Hit>();
        for(int b = 0; b < 3; b++) {
            Hit hit = new Hit();
            hit.setAmmo("7.92 AP");
            hit.setAttacker("FW-190A3");
            hit.setTarget("IL-2 mod.42");

            hits.add(hit);
        }
        return hits;

    }

    private List<GameObject> buildKillsList(int a) {
        List<GameObject> kills = new ArrayList<GameObject>();
        for(int b = 0; b < 3; b++) {
            GameObject kill = new GameObject();
            kill.setCountry("Soviet Union");
            kill.setName("Plane");
            kill.setType(b == 0 ? "IL-2 mod.42" : (b == 1 ? "Lagg-3" : "Pe-2"));
            kill.setGameObjectType(GameObjectType.VEHICLE);
            kill.setParentId(-1);
            kills.add(kill);
        }
        return kills;
    }

}
