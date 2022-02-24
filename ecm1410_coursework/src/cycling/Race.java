package cycling;

import java.util.ArrayList;
import java.time.LocalDateTime;

/** 
 * Race encapsulates tour races, each of which has a number of associated
 * Stages.
 * 
 * @author Thomas Newbold
 * @version 1.0
 * 
 */
public class Race {
    // Static class attributes
    private static int idMax = 0;
    private static ArrayList<Race> allRaces = new ArrayList<Race>();

    // make this private? instance only accessable by static methods (id param)
    public static Race getRace(int raceId) { return allRaces.get(raceId); }
    public static void removeRace(int raceId) {
        allRaces.remove(raceId);
        Race.idMax--;
        for(int i=raceId;i<allRaces.size();i++) {
            getRace(i).raceId--;
        }
    }

    // Instance attributes
    private int raceId;
    private String raceName;
    private String raceDescription;
    private ArrayList<Integer> stageIds;

    /**
     * Race constructor; creates new race and adds to allRaces array.
     * 
     * @param name The name of the new race
     * @param description The description for the new race
     */
    public Race(String name, String description) {
        this.raceId = idMax++;
        this.raceName = name;
        this.raceDescription = description;
        this.stageIds = new ArrayList<Integer>();
        Race.allRaces.add(this);
    }

    /**
     * @return A string representation of the race instance
     */
    public String toString() {
        String id = Integer.toString(this.raceId);
        String name = this.raceName;
        String description = this.raceDescription;
        String list = this.stageIds.toString();
        return String.format("Race[%s]: %s; %s; StageIds=%s;", id, name, description, list);
    }

    /**
     * @param id The ID of the race
     * @return A string representation of the race instance
     */
    public static String toString(int id) {
        return getRace(id).toString();
    }

    /**
     * @return The integer raceId for the race instance
     */
    public int getRaceId() { return this.raceId; }

    /**
     * @return The string raceName for the race instance
     */
    public String getRaceName() { return this.raceName; }

    /**
     * @param id The ID of the race
     * @return The string raceName for the race with the associated id
     */
    public static String getRaceName(int id) {
        return getRace(id).raceName;
    }

    /**
     * @return The string raceDescription for the race instance
     */
    public String getRaceDescription() { return this.raceDescription; }

    /**
     * @param id The ID of the race
     * @return The string raceDescription for the race with the associated id
     */
    public static String getRaceDescription(int id) {
        return getRace(id).raceDescription;
    }

    /**
     * @return An integer array of stage IDs for the race instance
     */
    public int[] getStages() {
        int length = this.stageIds.size();
        int[] stageIdsArray = new int[length];
        for(int i=0; i<length; i++) {
            stageIdsArray[i] = this.stageIds.get(i);
        }
        return stageIdsArray;
    }

    /**
     * @param name The new name for the race instance
     */
    public void setRaceName(String name) {
        this.raceName = name;
    }

    /**
     * @param id The ID of the race to be updated
     * @param name The new name for the race instance
     */
    public static void setRaceName(int id, String name) {
        getRace(id).setRaceName(name);
    }

    /**
     * @param description The new description for the race instance
     */
    public void setRaceDescription(String description) {
        this.raceDescription = description;
    }

    /**
     * @param id The ID of the race to be updated
     * @param description The new description for the race instance
     */
    public static void setRaceDescription(int id, String description) {
        getRace(id).setRaceDescription(description);
    }

    /**
     * Creates a new stage and adds the ID to the stageIds array.
     * 
     * @param name The name of the new stage
     * @param description The description of the new stage
     * @param length The length of the new stage (in km)
     * @param startTime The date and time at which the stage will be held
     * @param type The StageType, used to determine the point distribution
     */
    public void addStageToRace(String name, String description, double length,
                               LocalDateTime startTime, StageType type) {
        Stage newStage = new Stage(name, description, length, startTime, type);
        this.stageIds.add(newStage.getStageId());
    }

    /**
     * Creates a new stage and adds the ID to the stageIds array.
     * 
     * @param id The ID of the race to which the stage will be added
     * @param name The name of the new stage
     * @param description The description of the new stage
     * @param length The length of the new stage (in km)
     * @param startTime The date and time at which the stage will be held
     * @param type The StageType, used to determine the point distribution
     */
    public static void addStageToRace(int id, String name, String description,
                                      double length, LocalDateTime startTime,
                                      StageType type) {
        getRace(id).addStageToRace(name, description, length, startTime, type);
    }

    /**
     * Removes a stageId from the array of stageIds for a race instance.
     * 
     * @param stageId The ID of the stage to be removed
     */
    public void removeStageFromRace(int stageId) {
        int index = -1;
        for(int i=0;i<this.stageIds.size();i++) {
            int sId = stageIds.get(i);
            if(sId>stageId) {
                stageIds.set(i,--sId);
            } else if (sId==stageId) {
                index = i;
            }
        }
        this.stageIds.remove(index);
    }

    /**
     * Removes a stageId from the array of stageIds for all race instances,
     * as well as from the static array of all stages in the Stage class.
     * 
     * @param stageId The ID of the stage to be removed
     */
    public static void removeStage(int stageId) {
        for (Race race : allRaces) {
            race.removeStageFromRace(stageId);
        }
        Stage.removeStage(stageId);
    }
}
