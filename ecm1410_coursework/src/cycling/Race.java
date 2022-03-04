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

    /**
     * @param raceId The ID of the race instance to fetch
     * @return The race instance with the associated ID
     * @throws IDNotRecognisedException If no race exists with the requested ID
     */
    public static Race getRace(int raceId) throws IDNotRecognisedException {
        if(raceId<Race.idMax && raceId >= 0) {
            return allRaces.get(raceId);
        } else {
            throw new IDNotRecognisedException("raceID out of range");
        }
    }

    /**
     * @param raceId The ID of the race instance to remove
     * @throws IDNotRecognisedException If no race exists with the requested ID
     */
    public static void removeRace(int raceId) throws IDNotRecognisedException {
        if(raceId<Race.idMax && raceId >= 0) {
            for(int id : allRaces.get(raceId).getStages()) {
                Stage.removeStage(id);
            }
            allRaces.remove(raceId);
            Race.idMax--;
            for(int i=raceId;i<allRaces.size();i++) {
                getRace(i).raceId--;
            }
        } else {
            throw new IDNotRecognisedException("raceID out of range");
        }
    }

    // Instance attributes
    private int raceId;
    private String raceName;
    private String raceDescription;
    private ArrayList<Integer> stageIds;

    /**
     * @param name String to be checked
     * @return true if name is valid for the system
     */
    private static boolean validName(String name) {
        if(name==null || name.equals("")) {
            return false;
        } else if(name.length()>30) {
            return false;
        } else if(name.contains(" ")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Race constructor; creates new race and adds to allRaces array.
     * 
     * @param name The name of the new race
     * @param description The description for the new race
     * @throws IllegalNameException If name already exists in the system
     * @throws InvalidNameException If name is empty/null, contains whitespace,
     *                              or is longer than 30 characters
     */
    public Race(String name, String description) throws IllegalNameException,
                InvalidNameException {
        for(Race race : allRaces) {
            if(race.getRaceName().equals(name)) {
                throw new IllegalNameException("name already exists");
            }
        }
        if(!validName(name)) {
            throw new InvalidNameException("invalid name");
        }
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
        return String.format("Race[%s]: %s; %s; StageIds=%s;", id, name,
                             description, list);
    }

    /**
     * @param id The ID of the race
     * @return A string representation of the race instance
     * @throws IDNotRecognisedException If no race exists with the requested ID
     */
    public static String toString(int id) throws IDNotRecognisedException {
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
     * @throws IDNotRecognisedException If no race exists with the requested ID
     */
    public static String getRaceName(int id) throws IDNotRecognisedException {
        return getRace(id).raceName;
    }

    /**
     * @return The string raceDescription for the race instance
     */
    public String getRaceDescription() { return this.raceDescription; }

    /**
     * @param id The ID of the race
     * @return The string raceDescription for the race with the associated id
     * @throws IDNotRecognisedException If no race exists with the requested ID
     */
    public static String getRaceDescription(int id) throws
                                            IDNotRecognisedException {
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
     * @throws IDNotRecognisedException If no race exists with the requested ID
     */
    public static void setRaceName(int id, String name) throws
                                   IDNotRecognisedException {
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
     * @throws IDNotRecognisedException If no race exists with the requested ID
     */
    public static void setRaceDescription(int id, String description) throws
                                          IDNotRecognisedException {
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
                               LocalDateTime startTime, StageType type) throws
                               IllegalNameException, InvalidNameException,
                               InvalidLengthException {
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
     * @throws IDNotRecognisedException If no race exists with the requested ID
     */
    public static void addStageToRace(int id, String name, String description,
                                      double length, LocalDateTime startTime,
                                      StageType type) throws
                                      IDNotRecognisedException,
                                      IllegalNameException, InvalidNameException,
                                      InvalidLengthException  {
        getRace(id).addStageToRace(name, description, length, startTime, type);
    }

    /**
     * Removes a stageId from the array of stageIds for a race instance.
     * 
     * @param stageId The ID of the stage to be removed
     */
    private void removeStageFromRace(int stageId) {
        int index = -1;
        for(int i=0;i<this.stageIds.size();i++) {
            int sId = this.stageIds.get(i);
            if(sId>stageId) {
                this.stageIds.set(i,--sId);
            } else if (sId==stageId) {
                index = i;
            }
        }
        this.stageIds.remove(index);
    }

    /**
     * Removes a stageId from the array of stageIds for a race instance.
     * 
     * @param id The ID of the race instance
     * @param stageId The ID of the stage to be removed
     * @throws IDNotRecognisedException If no race exists with the requested ID
     */
    /*
    public static void removeStageFromRace(int id, int stageId) throws
                                           IDNotRecognisedException {
        int index = -1;
        Race r = getRace(id);
        for(int i=0;i<r.stageIds.size();i++) {
            int sId = r.stageIds.get(i);
            if(sId>stageId) {
                r.stageIds.set(i,--sId);
            } else if (sId==stageId) {
                index = i;
            }
        }
        r.stageIds.remove(index);
    }
    */

    /**
     * Removes a stageId from the array of stageIds for all race instances,
     * as well as from the static array of all stages in the Stage class.
     * 
     * @param stageId The ID of the stage to be removed
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static void removeStage(int stageId) throws
                                   IDNotRecognisedException {
        for (Race race : allRaces) {
            race.removeStageFromRace(stageId);
        }
        Stage.removeStage(stageId);
    }
}
