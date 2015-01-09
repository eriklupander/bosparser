package se.lu.bos.model.wg;

/**
 * Should be used on the strategic level - i.e. from perhaps company or batallion and upwards to regiment?
 *
 * Can then be used as some kind of "proxy object" for generating ground-forces in-game.
 */
public class GroundUnit {

    private String name;
    private String description;

    private GroundUnitType groundUnitType;

    private int attackStrength;
    private int defenseStrength;
    private int attackSpeedMetersPerDay;
    private int movementSpeedMetersPerDay;

    private GroundUnitGroupFormationPreference preferredPositionInGroup;

    private XZCoord position;

    private GroundUnitActivity activity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GroundUnitType getGroundUnitType() {
        return groundUnitType;
    }

    public void setGroundUnitType(GroundUnitType groundUnitType) {
        this.groundUnitType = groundUnitType;
    }

    public int getAttackStrength() {
        return attackStrength;
    }

    public void setAttackStrength(int attackStrength) {
        this.attackStrength = attackStrength;
    }

    public int getDefenseStrength() {
        return defenseStrength;
    }

    public void setDefenseStrength(int defenseStrength) {
        this.defenseStrength = defenseStrength;
    }

    public int getAttackSpeedMetersPerDay() {
        return attackSpeedMetersPerDay;
    }

    public void setAttackSpeedMetersPerDay(int attackSpeedMetersPerDay) {
        this.attackSpeedMetersPerDay = attackSpeedMetersPerDay;
    }

    public int getMovementSpeedMetersPerDay() {
        return movementSpeedMetersPerDay;
    }

    public void setMovementSpeedMetersPerDay(int movementSpeedMetersPerDay) {
        this.movementSpeedMetersPerDay = movementSpeedMetersPerDay;
    }
}
