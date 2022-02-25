package cycling;

/**
 * This enum is used to represent the state of a stage.
 * 
 * @author Thomas Newbold
 * @version 1.0
 * 
 */
public enum StageState {

    /**
     * Used for stages still in preperation - i.e. segments are still being
     * added.
     */
    BUILDING,

    /**
     * Used for stages waiting for results
     */
    WAITING;
}
