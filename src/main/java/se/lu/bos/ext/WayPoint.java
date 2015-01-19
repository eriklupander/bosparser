package se.lu.bos.ext;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-19
 * Time: 16:05
 * To change this template use File | Settings | File Templates.
 */
public class WayPoint {

    private Float x;
    private Float y;
    private Float z;

    private WaypointAction action;

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

    public WaypointAction getAction() {
        return action;
    }

    public void setAction(WaypointAction action) {
        this.action = action;
    }
}
