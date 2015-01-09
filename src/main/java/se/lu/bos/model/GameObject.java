package se.lu.bos.model;

import se.lu.bos.util.TimeUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2014-11-30
 * Time: 22:11
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="game_object")
public class GameObject {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Integer gameObjectId;
    private String name;
    private String type;
    private String country;
    private Integer parentId = -1;

    @Enumerated(EnumType.STRING)
    private State state = State.ALIVE;

    @Enumerated(EnumType.STRING)
    private GameObjectType gameObjectType;

    private Integer timeOfKill;

    private float spawnedXPos, spawnedZPos;
    private float killedXPos, killedZPos;

    public float getSpawnedXPos() {
        return spawnedXPos;
    }

    public void setSpawnedXPos(float spawnedXPos) {
        this.spawnedXPos = spawnedXPos;
    }

    public float getSpawnedZPos() {
        return spawnedZPos;
    }

    public void setSpawnedZPos(float spawnedZPos) {
        this.spawnedZPos = spawnedZPos;
    }

    public float getKilledXPos() {
        return killedXPos;
    }

    public void setKilledXPos(float killedXPos) {
        this.killedXPos = killedXPos;
    }

    public float getKilledZPos() {
        return killedZPos;
    }

    public void setKilledZPos(float killedZPos) {
        this.killedZPos = killedZPos;
    }

    @OneToMany(cascade = CascadeType.ALL)
    private List<GameObject> children = new ArrayList<GameObject>();

    public GameObject() {
    }

    public GameObject(Integer gameObjectId, String name, String type, GameObjectType gameObjectType, Integer parentId, String country) {
        this.gameObjectId = gameObjectId;
        this.name = name;
        this.type = type;
        this.gameObjectType = gameObjectType;
        this.parentId = parentId;
        this.country = country;
    }

    public Integer getGameObjectId() {
        return gameObjectId;
    }

    public void setGameObjectId(Integer gameObjectId) {
        this.gameObjectId = gameObjectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }

    public void setGameObjectType(GameObjectType gameObjectType) {
        this.gameObjectType = gameObjectType;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public List<GameObject> getChildren() {
        return children;
    }

    public void setChildren(List<GameObject> children) {
        this.children = children;
    }

    public Integer getTimeOfKill() {
        return timeOfKill;
    }

    public void setTimeOfKill(Integer timeOfKill) {
        this.timeOfKill = timeOfKill;
    }

    // Each tick is 1/50 of a second, e.g. 20 ms
    @Transient
    public String getGameTime() {
        try {
            if(this.timeOfKill == null) return "";
            return TimeUtil.gameTickToTime(this.timeOfKill);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "";
        }
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "id=" + id +
                ", gameObjectId=" + gameObjectId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", parentId=" + parentId +
                ", state=" + state +
                ", gameObjectType=" + gameObjectType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameObject that = (GameObject) o;

        if (!gameObjectId.equals(that.gameObjectId)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return gameObjectId.hashCode();
    }
}
