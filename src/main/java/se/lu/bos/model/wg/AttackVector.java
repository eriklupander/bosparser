package se.lu.bos.model.wg;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-09
 * Time: 12:34
 * To change this template use File | Settings | File Templates.
 */
public class AttackVector {

    private XZCoord startCoord;
    private XZCoord targetCoord;

    public XZCoord getStartCoord() {
        return startCoord;
    }

    public void setStartCoord(XZCoord startCoord) {
        this.startCoord = startCoord;
    }

    public XZCoord getTargetCoord() {
        return targetCoord;
    }

    public void setTargetCoord(XZCoord targetCoord) {
        this.targetCoord = targetCoord;
    }
}
