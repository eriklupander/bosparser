package se.lu.bos.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2015-01-09
 * Time: 22:49
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class FlightPosition {
    @Id
    @GeneratedValue
    private Long id;

    private Float x;
    private Float y;
    private Float z;

    private Integer missionTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getMissionTime() {
        return missionTime;
    }

    public void setMissionTime(Integer missionTime) {
        this.missionTime = missionTime;
    }
}
