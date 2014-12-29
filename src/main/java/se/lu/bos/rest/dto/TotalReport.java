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
    private int missions;
    private Long missionsSurvived;
    private long missionsDestroyed;
    private String totalFlightTime;
    private int totalFlightTimeSeconds;
    private int kills;
    private int hits;
    private TreeMap<Object, Long> killsByTargetType = new TreeMap<Object, Long>();
    private TreeMap<Object, Long> hitsByAmmoType = new TreeMap<Object, Long>();
    private TreeMap sortiesPerPlaneType = new TreeMap<Object, Long>();
    private Map<String, Long> killsInPlaneType = new TreeMap<String, Long>();


    public void setMissions(int missions) {
        this.missions = missions;
    }

    public int getMissions() {
        return missions;
    }

    public void setMissionsSurvived(Long missionsSurvived) {
        this.missionsSurvived = missionsSurvived;
    }

    public Long getMissionsSurvived() {
        return missionsSurvived;
    }

    public void setMissionsDestroyed(long missionsDestroyed) {
        this.missionsDestroyed = missionsDestroyed;
    }

    public long getMissionsDestroyed() {
        return missionsDestroyed;
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

    public TreeMap<Object, Long> getKillsByTargetType() {
        return killsByTargetType;
    }

    public void setKillsByTargetType(TreeMap<Object, Long> killsByTargetType) {
        this.killsByTargetType = killsByTargetType;
    }

    public void setHitsByAmmoType(TreeMap<Object, Long> hitsByAmmoType) {
        this.hitsByAmmoType = hitsByAmmoType;
    }

    public TreeMap<Object, Long> getHitsByAmmoType() {
        return hitsByAmmoType;
    }

    public void setSortiesPerPlaneType(TreeMap sortiesPerPlaneType) {
        this.sortiesPerPlaneType = sortiesPerPlaneType;
    }

    public TreeMap getSortiesPerPlaneType() {
        return sortiesPerPlaneType;
    }

    public void setKillsInPlaneType(Map<String, Long> killsInPlaneType) {
        this.killsInPlaneType = killsInPlaneType;
    }

    public Map<String, Long> getKillsInPlaneType() {
        return killsInPlaneType;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getHits() {
        return hits;
    }
}
