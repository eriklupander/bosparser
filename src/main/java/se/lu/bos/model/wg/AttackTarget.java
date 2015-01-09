package se.lu.bos.model.wg;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-09
 * Time: 12:35
 * To change this template use File | Settings | File Templates.
 */
public class AttackTarget {

    private XZCoord coord;
    private String name;
    private String description;

    public XZCoord getCoord() {
        return coord;
    }

    public void setCoord(XZCoord coord) {
        this.coord = coord;
    }

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
}
