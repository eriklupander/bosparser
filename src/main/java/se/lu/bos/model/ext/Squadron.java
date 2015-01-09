package se.lu.bos.model.ext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-09
 * Time: 12:21
 * To change this template use File | Settings | File Templates.
 */
public class Squadron {

    private String name;
    private PlaneType planeType;

    private List<Pilot> pilots = new ArrayList<>();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlaneType getPlaneType() {
        return planeType;
    }

    public void setPlaneType(PlaneType planeType) {
        this.planeType = planeType;
    }

    public List<Pilot> getPilots() {
        return pilots;
    }

    public void setPilots(List<Pilot> pilots) {
        this.pilots = pilots;
    }
}
