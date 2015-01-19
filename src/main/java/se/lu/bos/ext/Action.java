package se.lu.bos.ext;

/**
 * Some Action that can be triggered by a TriggerZone.
 */
public class Action {

    private String name;
    private String label; // Should be transferred to .eng file as subtitle???

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
