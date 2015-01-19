package se.lu.bos.ext;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: eriklupander
 * Date: 15-01-19
 * Time: 16:06
 * To change this template use File | Settings | File Templates.
 */
public class WaypointAction {
    private ActionType action;
    private Set<Integer> targetClientId;

    public ActionType getAction() {
        return action;
    }

    public void setAction(ActionType action) {
        this.action = action;
    }

    public Set<Integer> getTargetClientId() {
        return targetClientId;
    }

    public void setTargetClientId(Set<Integer> targetClientId) {
        this.targetClientId = targetClientId;
    }
}
