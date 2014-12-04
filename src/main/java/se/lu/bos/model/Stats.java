package se.lu.bos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import se.lu.bos.util.TimeUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-11-30
 * Time: 21:29
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "report_stats")
public class Stats {

    @Id
    @GeneratedValue
    private Long id;

    private String gameDate;
    private String gameTime;
    private String missionName;
    private String totalDuration;
    private Date reportFileDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String rootFileName;

    private Integer startingAmmo;
    private Integer finalAmmo;

    @ElementCollection
    private List<String> textualLog = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Hit> hits = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "stats_gameobject_kills")
    private List<GameObject> kills = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "stats_gameobjects")
    private List<GameObject> associatedObjects = new ArrayList<>();

    private Integer playerId;
    private String pilotName;
    private String pilotPlane;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Lob
    private String fullLog;

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getGameDate() {
        return gameDate;
    }

    public void setGameDate(String gameDate) {
        this.gameDate = gameDate;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    public String getRootFileName() {
        return rootFileName;
    }

    public void setRootFileName(String rootFileName) {
        this.rootFileName = rootFileName;
    }

    public List<String> getTextualLog() {
        return textualLog;
    }

    public void setTextualLog(List<String> textualLog) {
        this.textualLog = textualLog;
    }

    public List<GameObject> getAssociatedObjects() {
        return associatedObjects;
    }

    public void setAssociatedObjects(List<GameObject> associatedObjects) {
        this.associatedObjects = associatedObjects;
    }

    public List<GameObject> getKills() {
        return kills;
    }


    public void setKills(List<GameObject> kills) {
        this.kills = kills;
    }


    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public Integer getStartingAmmo() {
        return startingAmmo;
    }

    public void setStartingAmmo(Integer startingAmmo) {
        this.startingAmmo = startingAmmo;
    }

    public Integer getFinalAmmo() {
        return finalAmmo;
    }

    public void setFinalAmmo(Integer finalAmmo) {
        this.finalAmmo = finalAmmo;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonIgnore
    public String getFullLog() {
        return fullLog;
    }

    public void setFullLog(String fullLog) {
        this.fullLog = fullLog;
    }

    @Transient
    public Integer getAircraftKillCount() {
        long count = kills.stream().filter(g -> g.getState() == State.KILLED && g.getGameObjectType() == GameObjectType.PLANE).count();
        return new Integer((int) count);
    }

    @Transient
    public Integer getAircraftHitNotKilledCount() {
        long count = associatedObjects.stream().filter(g -> !g.getGameObjectId().equals(this.playerId) && g.getState() == State.ALIVE && g.getGameObjectType() == GameObjectType.PLANE).count();
        return new Integer((int) count);
    }

    @Transient
    public Integer getPilotKillCount() {
        long count = kills.stream().filter(g -> g.getState() == State.KILLED && g.getGameObjectType() == GameObjectType.PILOT).count();
        return new Integer((int) count);
    }

    @Transient
    public Integer getNonExplosiveHits() {
        return new Integer((int) hits.stream().filter(p -> !p.getAmmo().contains("explosion")).count());
    }

    @Transient
    public Integer getHitsOfType(String ammo) {
        return new Integer((int) hits.stream().filter(p -> p.getAmmo().equals(ammo)).count());
    }

    @Transient
    public List<String> getUniqueAmmoTypes() {
        List<String> types = new ArrayList<>();
        for(Hit h : hits) {
            if(!types.contains(h.getAmmo())) {
                types.add(h.getAmmo());
            }
        }
        return types;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "startingAmmo=" + startingAmmo +
                ", finalAmmo=" + finalAmmo +
                ", textualLog=" + textualLog +
                ", hits=" + hits +
                ", kills=" + kills +
                ", pilotName='" + pilotName + '\'' +
                ", pilotPlane='" + pilotPlane + '\'' +
                '}';
    }

    public void setPilotName(String pilotName) {
        this.pilotName = pilotName;
    }

    public String getPilotName() {
        return pilotName;
    }

    public void setPilotPlane(String pilotPlane) {
        this.pilotPlane = pilotPlane;
    }

    public String getPilotPlane() {
        return pilotPlane;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setReportFileDate(Date reportFileDate) {
        this.reportFileDate = reportFileDate;
    }

    public Date getReportFileDate() {
        return reportFileDate;
    }

    @Transient
    public String getRealDate() {
        if(this.reportFileDate == null) {
            return "";
        }
        return TimeUtil.parseDate(this.reportFileDate);
    }
}
