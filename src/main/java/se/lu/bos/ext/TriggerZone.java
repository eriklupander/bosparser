package se.lu.bos.ext;

import java.util.HashSet;
import java.util.Set;

/**
 * Defines a zone at a world coordinate having a radius.
 *
 * Holds an ID list of subjects that will trigger all actions on ENTER.
 */
public class TriggerZone {

    private Float x;
    private Float y;
    private Float z;

    private Integer radius;

    private Set<Integer> subjects  = new HashSet<Integer>();
    private Set<Action> actions = new HashSet<Action>();
}
