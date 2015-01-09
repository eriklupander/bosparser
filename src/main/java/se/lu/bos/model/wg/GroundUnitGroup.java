package se.lu.bos.model.wg;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to represent ground units on the divisional or similar level.
 *
 * Consists of a list of GroundUnits (with "formation" and a directional heading to simplify later rendering
 */
public class GroundUnitGroup {

    private String name;
    private String description;

    private Float direction; // 0-360 degrees, use as YOri later in BoS
    private GroundUnitGroupFormation formation; // LINE, COLUMN, WEDGE...
    private XZCoord position;

    private List<GroundUnit> units = new ArrayList<>();

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

    public Float getDirection() {
        return direction;
    }

    public void setDirection(Float direction) {
        this.direction = direction;
    }

    public GroundUnitGroupFormation getFormation() {
        return formation;
    }

    public void setFormation(GroundUnitGroupFormation formation) {
        this.formation = formation;
    }

    public XZCoord getPosition() {
        return position;
    }

    public void setPosition(XZCoord position) {
        this.position = position;
    }

    public List<GroundUnit> getUnits() {
        return units;
    }

    public void setUnits(List<GroundUnit> units) {
        this.units = units;
    }
}
