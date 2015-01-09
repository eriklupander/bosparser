package se.lu.bos.model.wg;

/**
 * Can be used to position a unit within its group in some manner. For example - an artillery unit should be at the back,
 * infantry or armor in the front. HQ perhaps in the middle. A recon or motorized unit perhaps on a flank.
 */
public enum GroundUnitGroupFormationPreference {
    FRONT, BACK, CENTRAL, FLANK;
}
