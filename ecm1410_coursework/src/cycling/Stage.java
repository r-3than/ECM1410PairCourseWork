package cycling;

import java.util.ArrayList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** 
 * Stage encapsulates race stages, each of which has a number of associated
 * Segments.
 * 
 * @author Thomas Newbold
 * @version 1.0
 * 
 */
public class Stage {
    // Static class attributes
    private static int idMax = 0;
    private static ArrayList<Stage> allStages = new ArrayList<Stage>();

    /**
     * @param stageId The ID of the stage instance to fetch
     * @return The stage instance with the associated ID
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static Stage getStage(int stageId) throws IDNotRecognisedException {
        if(stageId<Stage.idMax && stageId >= 0) {
            return allStages.get(stageId);
        } else {
            throw new IDNotRecognisedException("stageId out of range");
        }
    }

    /**
     * @param stageId The ID of the stage instance to remove
     * @throws IDNotRecognisedException If no stage exists with the requested ID 
     */
    public static void removeStage(int stageId) throws IDNotRecognisedException {
        if(stageId<Stage.idMax && stageId >= 0) {
            for(int id : allStages.get(stageId).getSegments()) {
                Segment.removeSegment(id);
            }
            allStages.remove(stageId);
            Stage.idMax--;
            for(int i=stageId;i<allStages.size();i++) {
                getStage(i).stageId--;
            }
        } else {
            throw new IDNotRecognisedException("stageId out of range");
        }
    }

    // Instance attributes
    private int stageId;
    private StageState stageState;
    private String stageName;
    private String stageDescription;
    private double stageLength;
    private LocalDateTime stageStartTime;
    private StageType stageType;
    private ArrayList<Integer> segmentIds;

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
     * Stage constructor; creates a new stage and adds to allStages array.
     * 
     * @param name The name of the new stage
     * @param description The description of the new stage
     * @param length The total length of the new stage
     * @param startTime The start time for the new stage
     * @param type The type of the new stage
     * @throws IllegalNameException If name already exists in the system
     * @throws InvalidNameException If name is empty/null, contains whitespace,
     *                              or is longer than 30 characters
     * @throws InvalidLengthException If the length is less than 5km
     */
    public Stage(String name, String description, double length,
                 LocalDateTime startTime, StageType type) throws
                 IllegalNameException, InvalidNameException,
                 InvalidLengthException {
        for(Stage stage : allStages) {
            if(stage.getStageName().equals(name)) {
                throw new IllegalNameException("name already exists");
            }
        }
        if(!validName(name)) {
            throw new InvalidNameException("invalid name");
        }
        if(length<5) {
            throw new InvalidLengthException("length less than 5km");
        }
        this.stageId = idMax++;
        this.stageState = StageState.BUILDING;
        this.stageName = name;
        this.stageDescription = description;
        this.stageLength = length;
        this.stageStartTime = startTime;
        this.stageType = type;
        this.segmentIds = new ArrayList<Integer>();
        Stage.allStages.add(this);
    }

    /**
     * @return A string representation of the stage instance
     */
    public String toString() {
        String id = Integer.toString(this.stageId);
        String state;
        switch (this.stageState) {
            case BUILDING:
                state = "In preperation";
                break;
            case WAITING:
                state = "Waiting for results";
                break;
            default:
                state = "null state";
        }
        String name = this.stageName;
        String description = this.stageDescription;
        String length = Double.toString(this.stageLength);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:hh dd-MM-yyyy");
        String startTime = this.stageStartTime.format(formatter);
        String list = this.segmentIds.toString();
        String type;
        switch (this.stageType) {
            case FLAT:
                type = "Flat";
                break;
            case MEDIUM_MOUNTAIN:
                type = "Medium Mountain";
                break;
            case HIGH_MOUNTAIN:
                type = "High Mountain";
                break;
            case TT:
                type = "Time Trial";
                break;
            default:
                type = "null type";
        }
        return String.format("Stage[%s](%s): %s (%s); %s; %skm; %s; SegmentIds=%s;",
                             id, state, name, type, description, length,
                             startTime, list);
    }

    /**
     * @param id The ID of the stage
     * @return A string representation of the stage instance
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static String toString(int id) throws IDNotRecognisedException {
        return getStage(id).toString();
    }

    /**
     * @return The integer stageId for the stage instance
     */
    public int getStageId() { return this.stageId; }

    /**
     * @return The state of the stage instance
     */
    public StageState getStageState() { return this.stageState; }

    /**
     * @param id The ID of the stage
     * @return The state of the stage instance
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static StageState getStageState(int id) throws
                                           IDNotRecognisedException {
        return getStage(id).getStageState();
    }
    /**
     * @return The string raceName for the stage instance
     */
    public String getStageName() { return this.stageName; }

    /**
     * @param id The ID of the stage
     * @return The string stageName for the stage with the associated id
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static String getStageName(int id) throws IDNotRecognisedException {
        return getStage(id).stageName;
    }

    /**
     * @return The string stageDescription for the stage instance
     */
    public String getStageDescription() { return this.stageDescription; }

    /**
     * @param id The ID of the stage
     * @return The string stageDescription for the stage with the associated id
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static String getStageDescription(int id) throws
                                             IDNotRecognisedException {
        return getStage(id).stageDescription;
    }

    /**
     * @return The length of the stage instance
     */
    public double getStageLength() { return this.stageLength; }

    /**
     * @param id The ID of the stage
     * @return The length of the stage instance
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static double getStageLength(int id) throws IDNotRecognisedException {
        return getStage(id).stageLength;
    }

    /**
     * @return The start time for the stage instance
     */
    public LocalDateTime getStageStartTime() { return this.stageStartTime; }

    /**
     * @param id The ID of the stage
     * @return The start time for the stage instance
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static LocalDateTime getStageStartTime(int id) throws
                                                  IDNotRecognisedException {
        return getStage(id).stageStartTime;
    }

    /**
     * @return The type of the stage instance
     */
    public StageType getStageType() { return this.stageType; }

    /**
     * @param id The ID of the stage
     * @return The type of the stage instance
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static StageType getStageType(int id) throws IDNotRecognisedException {
        return getStage(id).getStageType();
    }

    /**
     * @return An integer array of segment IDs for the stage instance
     */
    public int[] getSegments() {
        int length = this.segmentIds.size();
        int[] segmentIdsArray = new int[length];
        for(int i=0; i<length; i++) {
            segmentIdsArray[i] = this.segmentIds.get(i);
        }
        return segmentIdsArray;
    }

    /**
     * Updates the stage state from building to waiting for results.
     * 
     * @throws InvalidStageStateException If the stage is already waiting for results
     */
    public void updateStageState() throws InvalidStageStateException {
        if(this.stageState.equals(StageState.WAITING)) {
            throw new InvalidStageStateException("stage is already waiting for results");
        } else if(this.stageState.equals(StageState.BUILDING)) {
            this.stageState = StageState.WAITING;
        }
    }

    /**
     * Updates the stage state from building to waiting for results.
     * 
     * @param id The ID of the stage to be updated
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     * @throws InvalidStageStateException If the stage is already waiting for results
     */
    public static void updateStageState(int id) throws IDNotRecognisedException,
                                        InvalidStageStateException {
        getStage(id).updateStageState();
    }

    /**
     * @param name The new name for the stage instance
     */
    public void setStageName(String name) {
        this.stageName = name;
    }

    /**
     * @param id The ID of the stage to be updated
     * @param name The new name for the stage instance
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static void setStageName(int id, String name) throws
                                    IDNotRecognisedException {
        getStage(id).setStageName(name);
    }

    /**
     * @param description The new description for the stage instance
     */
    public void setStageDescription(String description) {
        this.stageDescription = description;
    }

    /**
     * @param id The ID of the stage to be updated
     * @param description The new description for the stage instance
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static void setStageDescription(int id, String description) throws
                                           IDNotRecognisedException {
        getStage(id).setStageDescription(description);
    }

    /**
     * @param length The new length for the stage instance
     */
    public void setStageLength(double length) {
        this.stageLength = length;
    }

    /**
     * @param id The ID of the stage to be updated
     * @param length The new length for the stage instance
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static void setStageLength(int id, double length) throws 
                                      IDNotRecognisedException {
        getStage(id).stageLength = length;
    }

    /**
     * @param startTime The new start time for the stage instance
     */
    public void setStageStartTime(LocalDateTime startTime) {
        this.stageStartTime = startTime;
    }
    
    /**
     * @param id The ID of the stage to be updated
     * @param startTime The new start time for the stage instance
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static void setStageStartTime(int id, LocalDateTime startTime)
                                         throws IDNotRecognisedException {
        getStage(id).stageStartTime = startTime;
    }

    /**
     * Creates a new stage and adds the ID to the stageIds array.
     * 
     * @param location The location of the new segment
     * @param type The type of the new segment
     * @param averageGradient The average gradient of the new segment
     * @param length The length (in km) of the new segment
     * @throws InvalidLocationException If the segment finishes outside of the
     *                                  bounds of the stage
     * @throws InvalidStageStateException If the segment state is waiting for
     *                                    results
     * @throws InvalidStageTypeException If the stage type is a time-trial
     *                                   (cannot contain segments)
     */
    public void addSegmentToStage(double location, SegmentType type, 
                                  double averageGradient, double length) throws
                                  InvalidLocationException,
                                  InvalidStageStateException,
                                  InvalidStageTypeException {
        if(location > this.getStageLength()) {
            throw new InvalidLocationException("segment finishes outside of stage bounds");
        }
        if(this.getStageState().equals(StageState.WAITING)) {
            throw new InvalidStageStateException("stage is waiting for results");
        }
        if(this.getStageType().equals(StageType.TT)) {
            throw new InvalidStageTypeException("time trial stages cannot contain segments");
        }
        Segment newSegment = new Segment(location, type, averageGradient, length);
        this.segmentIds.add(newSegment.getSegmentId());
    }

    /**
     * Creates a new stage and adds the ID to the stageIds array.
     * 
     * @param id The ID of the stage to which the segment will be added
     * @param location The location of the new segment
     * @param type The type of the new segment
     * @param averageGradient The average gradient of the new segment
     * @param length The length (in km) of the new segment
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     * @throws InvalidLocationException If the segment finishes outside of the
     *                                  bounds of the stage
     * @throws InvalidStageStateException If the segment state is waiting for
     *                                    results
     * @throws InvalidStageTypeException If the stage type is a time-trial
     *                                   (cannot contain segments)
     */
    public void addSegmentToStage(int id, double location, SegmentType type, 
                                  double averageGradient, double length) throws
                                  IDNotRecognisedException,
                                  InvalidLocationException,
                                  InvalidStageStateException,
                                  InvalidStageTypeException {
        getStage(id).addSegmentToStage(location, type, averageGradient, length);
    }

    /**
     * Removes a segmentId from the array of segmentIds for a stage instance.
     * 
     * @param segmentId The ID of the segment to be removed
     */
    public void removeSegmentFromStage(int segmentId) {
        int index = -1;
        for(int i=0;i<this.segmentIds.size();i++) {
            int sId = this.segmentIds.get(i);
            if(sId>segmentId) {
                this.segmentIds.set(i,--sId);
            } else if (sId==segmentId) {
                index = i;
            }
        }
        this.segmentIds.remove(index);
    }

    /**
     * Removes a segmentId from the array of segmentIds for a stage instance.
     * 
     * @param id The ID of the stage instance
     * @param segmentId The ID of the segment to be removed
     * @throws IDNotRecognisedException If no stage exists with the requested ID
     */
    public static void removeSegmentFromStage(int id, int segmentId) throws
                                              IDNotRecognisedException {
        int index = -1;
        Stage s = getStage(id);
        for(int i=0;i<s.segmentIds.size();i++) {
            int sId = s.segmentIds.get(i);
            if(sId>segmentId) {
                s.segmentIds.set(i,--sId);
            } else if (sId==segmentId) {
                index = i;
            }
        }
        s.segmentIds.remove(index);
    }

    /**
     * Removes a segmentId from the array of segmentIds for all stage instances,
     * as well as from the static array of all segments in the Segment class.
     * 
     * @param segmentId The ID of the segment to be removed
     * @throws IDNotRecognisedException If no segment exists with the requested ID
     */
    public static void removeSegment(int segmentId) throws
                                     IDNotRecognisedException {
        for (Stage stage : allStages) {
            stage.removeSegmentFromStage(segmentId);
        }
        Segment.removeSegment(segmentId);
    }
}
