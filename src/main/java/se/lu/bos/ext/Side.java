package se.lu.bos.ext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-19
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public class Side {

    private String name;
    private List<UnitGroup> unitGroups = new ArrayList<UnitGroup>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<UnitGroup> getUnitGroups() {
        return unitGroups;
    }

    public void setUnitGroups(List<UnitGroup> unitGroups) {
        this.unitGroups = unitGroups;
    }
}
