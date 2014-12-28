package se.lu.bos.rest.dto;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-12-28
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public class TotalReport {
    private int numberOfMissions;
    private Long numberOfMissionsSurvived;
    private long numberOfMissionsDestroyed;
    private String totalFlightTime;
    private int totalFlightTimeSeconds;
    private TreeMap<Object, Long> numberOfKillsByTargetType;
    private Object[] numberOfKillsByTargetTypeSorted;
    private Object[] numberOfHitsByAmmoTypeSorted;
    private TreeMap<Object, Long> numberOfHitsByAmmoType;
    private TreeMap numberOfSortiesPerPlaneType;
    private Map<String, Long> numberOfKillsInPlaneType;

    public void setNumberOfMissions(int numberOfMissions) {
        this.numberOfMissions = numberOfMissions;
    }

    public int getNumberOfMissions() {
        return numberOfMissions;
    }

    public void setNumberOfMissionsSurvived(Long numberOfMissionsSurvived) {
        this.numberOfMissionsSurvived = numberOfMissionsSurvived;
    }

    public Long getNumberOfMissionsSurvived() {
        return numberOfMissionsSurvived;
    }

    public void setNumberOfMissionsDestroyed(long numberOfMissionsDestroyed) {
        this.numberOfMissionsDestroyed = numberOfMissionsDestroyed;
    }

    public long getNumberOfMissionsDestroyed() {
        return numberOfMissionsDestroyed;
    }

    public String getTotalFlightTime() {
        return totalFlightTime;
    }

    public void setTotalFlightTime(String totalFlightTime) {
        this.totalFlightTime = totalFlightTime;
    }

    public int getTotalFlightTimeSeconds() {
        return totalFlightTimeSeconds;
    }

    public void setTotalFlightTimeSeconds(int totalFlightTimeSeconds) {
        this.totalFlightTimeSeconds = totalFlightTimeSeconds;
    }

    public TreeMap<Object, Long> getNumberOfKillsByTargetType() {
        return numberOfKillsByTargetType;
    }

    public void setNumberOfKillsByTargetType(TreeMap<Object, Long> numberOfKillsByTargetType) {
        this.numberOfKillsByTargetType = numberOfKillsByTargetType;
    }

    public Object[] getNumberOfKillsByTargetTypeSorted() {
        return numberOfKillsByTargetTypeSorted;
    }

    public void setNumberOfKillsByTargetTypeSorted(Object[] numberOfKillsByTargetTypeSorted) {
        this.numberOfKillsByTargetTypeSorted = numberOfKillsByTargetTypeSorted;
    }

    public void setNumberOfHitsByAmmoTypeSorted(Object[] numberOfHitsByAmmoTypeSorted) {
        this.numberOfHitsByAmmoTypeSorted = numberOfHitsByAmmoTypeSorted;
    }

    public Object[] getNumberOfHitsByAmmoTypeSorted() {
        return numberOfHitsByAmmoTypeSorted;
    }

    public void setNumberOfHitsByAmmoType(TreeMap<Object, Long> numberOfHitsByAmmoType) {
        this.numberOfHitsByAmmoType = numberOfHitsByAmmoType;
    }

    public TreeMap<Object, Long> getNumberOfHitsByAmmoType() {
        return numberOfHitsByAmmoType;
    }

    public void setNumberOfSortiesPerPlaneType(TreeMap numberOfSortiesPerPlaneType) {
        this.numberOfSortiesPerPlaneType = numberOfSortiesPerPlaneType;
    }

    public TreeMap getNumberOfSortiesPerPlaneType() {
        return numberOfSortiesPerPlaneType;
    }

    public void setNumberOfKillsInPlaneType(Map<String, Long> numberOfKillsInPlaneType) {
        this.numberOfKillsInPlaneType = numberOfKillsInPlaneType;
    }

    public Map<String, Long> getNumberOfKillsInPlaneType() {
        return numberOfKillsInPlaneType;
    }
}
