package se.lu.bos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import se.lu.bos.util.TimeUtil;

import javax.persistence.*;
import java.util.*;

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

    @Transient
    private static Comparator<Hit> hitComparator = new Comparator<Hit>() {

        @Override
        public int compare(Hit o1, Hit o2) {
            return o1.getTime().compareTo(o2.getTime());
        }
    };

    @Transient
    private static Comparator<GameObject> gameObjectComparator = new Comparator<GameObject>() {

        @Override
        public int compare(GameObject o1, GameObject o2) {
            if(o1.getTimeOfKill() != null && o2.getTimeOfKill() != null) {
                return o1.getTimeOfKill().compareTo(o2.getTimeOfKill());
            }
            if(o1.getTimeOfKill() == null) return -1;
            if(o2.getTimeOfKill() == null) return 1;
            return 0;
        }
    };


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String rootFileName;

    private Integer startingAmmo;
    private Integer finalAmmo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "stats_hits_inflicted")
    private List<Hit> hits = new ArrayList<Hit>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "stats_hits_taken")
    private List<Hit> hitsTaken = new ArrayList<Hit>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "stats_gameobject_kills")
    private List<GameObject> kills = new ArrayList<GameObject>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "stats_gameobjects")
    private List<GameObject> associatedObjects = new ArrayList<GameObject>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "stats_all_gameobjects")
    private List<GameObject> allGameObjects = new ArrayList<GameObject>();

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

    public List<GameObject> getAssociatedObjects() {
        return associatedObjects;
    }

    public void setAssociatedObjects(List<GameObject> associatedObjects) {
        this.associatedObjects = associatedObjects;
    }

    public List<GameObject> getKills() {
        Collections.sort(kills, gameObjectComparator);
        return kills;
    }


    public void setKills(List<GameObject> kills) {
        this.kills = kills;
    }


    public List<Hit> getHits() {
        Collections.sort(hits, hitComparator);
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public List<Hit> getHitsTaken() {
        return hitsTaken;
    }

    public void setHitsTaken(List<Hit> hitsTaken) {
        this.hitsTaken = hitsTaken;
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
        int count = 0;
        for(GameObject kill : kills) {
            if(kill.getGameObjectType() == GameObjectType.VEHICLE) {
                count++;
            }
        }
        return count;
    }

    @Transient
    public Integer getAircraftHitNotKilledCount() {
        int count = 0;
        for(GameObject go : associatedObjects) {
            if(go.getGameObjectType() == GameObjectType.VEHICLE && go.getState() == State.ALIVE && !go.getGameObjectId().equals(this.playerId)) {
                count++;
            }
        }
        return count;
    }

    @Transient
    public Integer getPilotKillCount() {
        int count = 0;
        for(GameObject kill : kills) {
            if(kill.getGameObjectType() == GameObjectType.PILOT) {
                count++;
            }
        }
        return count;
    }

    @Transient
    public Integer getNonExplosiveHits() {
        int count = 0;
        for(Hit hit : hits) {
            if(!hit.getAmmo().contains("explosion")) {
                count++;
            }
        }
        return count;
    }

    @Transient
    public Integer getHitsOfType(String ammo) {
        int count = 0;
        for(Hit hit : hits) {
            if(hit.getAmmo().equals(ammo)) {
                count++;
            }
        }
        return count;
    }

    @Transient
    public List<String> getUniqueAmmoTypes() {
        List<String> types = new ArrayList<String>();
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
                "id=" + id +
                ", gameDate='" + gameDate + '\'' +
                ", gameTime='" + gameTime + '\'' +
                ", missionName='" + missionName + '\'' +
                ", totalDuration='" + totalDuration + '\'' +
                ", reportFileDate=" + reportFileDate +
                ", hitComparator=" + hitComparator +
                ", gameObjectComparator=" + gameObjectComparator +
                ", rootFileName='" + rootFileName + '\'' +
                ", startingAmmo=" + startingAmmo +
                ", finalAmmo=" + finalAmmo +
                ", hits=" + hits +
                ", hitsTaken=" + hitsTaken +
                ", kills=" + kills +
                ", associatedObjects=" + associatedObjects +
                ", playerId=" + playerId +
                ", pilotName='" + pilotName + '\'' +
                ", pilotPlane='" + pilotPlane + '\'' +
                ", created=" + created +
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

    public void setAllGameObjects(List<GameObject> allGameObjects) {
        this.allGameObjects = allGameObjects;
    }

    public List<GameObject> getAllGameObjects() {
        return allGameObjects;
    }

    @Transient
    public List<GameObject> getGameObjectHierarchy() {
        List<GameObject> outList = new ArrayList<GameObject>();
        for(GameObject g : allGameObjects) {
            for(GameObject child : allGameObjects) {
                if(child.getParentId().equals(g.getGameObjectId())) {
                    g.getChildren().add(child);
                }
            }
            if(g.getParentId() == null || g.getParentId() == -1) {
                outList.add(g);
            }
        }
        return outList;
    }


}
