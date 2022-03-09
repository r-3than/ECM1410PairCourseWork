package cycling;

import java.util.ArrayList;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Result encapsulates rider results per stage, and handles time adjustments and
 * rankings (scoring is done externally based on points distributions defined in
 * Cycling Portal)
 * 
 * @author Thomas Newbold
 * @version 1.0
 */
public class Result {
    private static ArrayList<Result> allResults = new ArrayList<Result>();

    private int stageId;
    private int riderId;
    private LocalTime[] checkpoints;

    public Result(int sId, int rId, LocalTime... check) {
        /*
        if(Stage.getStageState(sId).equals(StageState.BUILDING)) {
            throw new InvalidStageStateException("stage is not waiting for results");
        }
        */
        this.stageId = sId;
        this.riderId = rId;
        this.checkpoints = check;
    }

    public static Result getResult(int sId, int rId) throws IDNotRecognisedException {
        for(Result r : allResults) {
            if(r.getRiderId()==rId && r.getStageId()==sId) {
                return r;
            }
        }
        throw new IDNotRecognisedException("results not found for rider in stage");
    }

    public int getStageId() { return this.stageId; }

    public int getRiderId() { return this.riderId; }

    public LocalTime[] getCheckpoints() {
        return this.checkpoints;
    }

    public LocalTime[] adjustedCheckpoints() {
        LocalTime[] adjusted = this.checkpoints;
        for(int n=0; n<this.checkpoints.length; n++) {
            adjusted[n] = adjustedCheckpoint(n);            
        }
        return adjusted;
    }

    public LocalTime adjustedCheckpoint(int n) {
        for(int i=0; i<allResults.size(); i++) {
            Result r = allResults.get(i);
            if(r.getRiderId()==this.getRiderId() && r.getStageId()==this.getStageId()) {
                continue;
            }
            LocalTime selfTime = this.getCheckpoints()[n];
            LocalTime rTime = r.getCheckpoints()[n];
            if(selfTime.until(rTime, ChronoUnit.SECONDS)<1) {
                return r.adjustedCheckpoint(n);
            } else {
                return selfTime;
            }
        }
        return null;
    }
}
