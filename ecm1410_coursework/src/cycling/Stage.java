package cycling;

import java.time.LocalDateTime;
import java.util.ArrayList;

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

    private int stageId;
    private int stageState; // use an enum for this?
    private String stageName;
    private String stageDescription;
    private double stageLength;
    private LocalDateTime stageStartTime;
    private StageType stageType;
    private ArrayList<Integer> segmentIds;

    public Stage(String name, String description, double length, LocalDateTime startTime, StageType type) {
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

    public String toString() {
        return Integer.toString(this.stageId)+this.stageName+this.stageDescription;
    }

    public int getStageId() { return this.stageId; }

    public int[] getSegments() {
        int length = this.segmentIds.size();
        int[] segmentIdsArray = new int[length];
        for(int i=0; i<length; i++) {
            segmentIdsArray[i] = this.segmentIds.get(i);
        }
        return segmentIdsArray;
    }
}
