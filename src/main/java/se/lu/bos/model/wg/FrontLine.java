package se.lu.bos.model.wg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-09
 * Time: 12:28
 * To change this template use File | Settings | File Templates.
 */
public class FrontLine {

    private List<XZCoord> coords = new ArrayList<>();

    public List<XZCoord> getCoords() {
        return coords;
    }

    public void setCoords(List<XZCoord> coords) {
        this.coords = coords;
    }
}
