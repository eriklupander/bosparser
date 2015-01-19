package se.lu.bos.ext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-19
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */
public class UnitGroup {
    private Integer clientId;
    private String groupType;
    private Integer size;
    private String name;
    private String type;
    private Float x;
    private Float y;
    private Float z;
    private Float yOri;
    private boolean startsInAir;
    private Integer loadout = 0;
    private Integer aiLevel = 2;

    private List<WayPoint> waypoints = new ArrayList<WayPoint>();

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }

    public Float getyOri() {
        return yOri;
    }

    public void setyOri(Float yOri) {
        this.yOri = yOri;
    }

    public boolean isStartsInAir() {
        return startsInAir;
    }

    public void setStartsInAir(boolean startsInAir) {
        this.startsInAir = startsInAir;
    }

    public Integer getLoadout() {
        return loadout;
    }

    public void setLoadout(Integer loadout) {
        this.loadout = loadout;
    }

    public Integer getAiLevel() {
        return aiLevel;
    }

    public void setAiLevel(Integer aiLevel) {
        this.aiLevel = aiLevel;
    }

    public List<WayPoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<WayPoint> waypoints) {
        this.waypoints = waypoints;
    }
}
