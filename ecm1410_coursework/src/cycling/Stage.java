package cycling;

import java.util.ArrayList;

import javax.swing.SwingConstants;

import java.time.LocalDate;
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
    private static int idMax = 0;
    private static ArrayList<Stage> allStages = new ArrayList<Stage>();

    public static Stage getStage(int stageId) { return allStages.get(stageId); }
    public static void removeStage(int stageId) {
        allStages.remove(stageId);
        Stage.idMax--;
        for(int i=stageId;i<allStages.size();i++) {
            getStage(i).stageId--;
        }
    }

    private int stageId;
    private int stageState; // use an enum for this?
    private String stageName;
    private String stageDescription;
    private double stageLength;
    private LocalDateTime stageStartTime;
    private StageType stageType;
    private ArrayList<Integer> segmentIds;

    public Stage(String name, String description, double length,
                 LocalDateTime startTime, StageType type) {
        this.stageId = idMax++;
        this.stageState = 0;
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
        return String.format("Stage[%s]: %s (%s); %s; %skm; %s; StageIds=%s;",
                             id, name, type, description, length, startTime, list);
    }

    /**
     * @param id The ID of the stage
     * @return A string representation of the stage instance
     */
    public static String toString(int id) {
        return getStage(id).toString();
    }

    /**
     * @return The integer stageId for the stage instance
     */
    public int getStageId() { return this.stageId; }

    /**
     * @return The string raceName for the stage instance
     */
    public String getStageName() { return this.stageName; }

    /**
     * @param id The ID of the stage
     * @return The string stageName for the stage with the associated id
     */
    public static String getStageName(int id) {
        return getStage(id).stageName;
    }

    /**
     * @return The string stageDescription for the stage instance
     */
    public String getStageDescription() { return this.stageDescription; }

    /**
     * @param id The ID of the stage
     * @return The string stageDescription for the stage with the associated id
     */
    public static String getStageDescription(int id) {
        return getStage(id).stageDescription;
    }

    /**
     * @return The length of the stage instance
     */
    public double getStageLength() { return this.stageLength; }

    /**
     * @param id The ID of the stage
     * @return The length of the stage instance
     */
    public static double getStageLength(int id) {
        return getStage(id).stageLength;
    }

    /**
     * @return The start time for the stage instance
     */
    public LocalDateTime getStageStartTime() { return this.stageStartTime; }

    /**
     * @param id The ID of the stage
     * @return The start time for the stage instance
     */
    public static LocalDateTime getStageStartTime(int id) {
        return getStage(id).stageStartTime;
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
     * @param name The new name for the stage instance
     */
    public void setStageName(String name) {
        this.stageName = name;
    }

    /**
     * @param id The ID of the stage to be updated
     * @param name The new name for the stage instance
     */
    public static void setStageName(int id, String name) {
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
     */
    public static void setStageDescription(int id, String description) {
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
     */
    public static void setStageLength(int id, double length) {
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
     */
    public static void setStageStartTime(int id, LocalDateTime startTime) {
        getStage(id).stageStartTime = startTime;
    }

    /**
     * Creates a new stage and adds the ID to the stageIds array.
     */
    public void addSegmentToStage() {} // similar to addStageToRace
}
