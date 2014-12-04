package se.lu.bos.model;

import se.lu.bos.util.TimeUtil;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-11-30
 * Time: 21:49
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="hit")
public class Hit {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String ammo;
    private Long time;
    private Float damage;

    private Integer attackerId;
    private Integer targetId;

    private String target;

//    @ManyToOne(cascade = CascadeType.ALL)
//    private GameObject attacker;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    private GameObject target;

    public Hit() {
    }

    public Hit(String ammo, Long time, Integer attackerId, Integer targetId) {
        this.ammo = ammo;
        this.time = time;
        this.attackerId = attackerId;
        this.targetId = targetId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getAmmo() {
        return ammo;
    }

    public void setAmmo(String ammo) {
        this.ammo = ammo;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Float getDamage() {
        return damage;
    }

    public void setDamage(Float damage) {
        this.damage = damage;
    }

    public Integer getAttackerId() {
        return attackerId;
    }

    public void setAttackerId(Integer attackerId) {
        this.attackerId = attackerId;
    }

    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }

    // Each tick is 1/50 of a second, e.g. 20 ms
    @Transient
    public String getGameTime() {
        try {
            if(this.time == null) return "";
            return TimeUtil.gameTickToTime(this.time.intValue());
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "";
        }
    }

    @Override
    public String toString() {
        return "Hit{" +
                "id=" + id +
                ", ammo='" + ammo + '\'' +
                ", time=" + time +
                ", damage=" + damage +
                ", attackerId=" + attackerId +
                ", targetId=" + targetId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hit hit = (Hit) o;

        if (!ammo.equals(hit.ammo)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ammo.hashCode();
    }
}
