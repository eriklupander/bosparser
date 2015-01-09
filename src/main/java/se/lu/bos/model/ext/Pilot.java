package se.lu.bos.model.ext;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-08
 * Time: 11:58
 * To change this template use File | Settings | File Templates.
 */
public class Pilot {

    private Rank rank;
    private String firstName;
    private String lastName;

    private Integer skill = 2; // 1-4 where 4 is best.

    private Integer missions = 0;  // We may want to init pilots with x number of missions and kills not derived from actual missions flown in BoS?
    private Integer aaKills = 0;
    private Integer groundKills = 0;

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getSkill() {
        return skill;
    }

    public void setSkill(Integer skill) {
        this.skill = skill;
    }

    public Integer getMissions() {
        return missions;
    }

    public void setMissions(Integer missions) {
        this.missions = missions;
    }

    public Integer getAaKills() {
        return aaKills;
    }

    public void setAaKills(Integer aaKills) {
        this.aaKills = aaKills;
    }

    public Integer getGroundKills() {
        return groundKills;
    }

    public void setGroundKills(Integer groundKills) {
        this.groundKills = groundKills;
    }

    @Override
    public String toString() {
        return rank.name() +
                " " + firstName + " " + lastName;
    }
}
