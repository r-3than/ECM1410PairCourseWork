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

    public static Result[] getResultsInStage(int stageId) {
        ArrayList<Result> stage = new ArrayList<Result>(allResults);
        stage.removeIf(r -> r.getStageId()!=stageId);
        Result[] resultsForStage = new Result[stage.size()];
        for(int i=0; i<stage.size(); i++) {
            resultsForStage[i] = stage.get(i);
        }
        return resultsForStage;
    }

    public static Result[] getResultsForRider(int riderId) {
        ArrayList<Result> rider = new ArrayList<Result>(allResults);
        rider.removeIf(r -> r.getRiderId()!=riderId);
        Result[] resultsForRider = new Result[rider.size()];
        for(int i=0; i<rider.size(); i++) {
            resultsForRider[i] = rider.get(i);
        }
        return resultsForRider;
    }

    private int stageId;
    private int riderId;
    private LocalTime[] checkpoints;

    public Result(int sId, int rId, LocalTime... check) {
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

    public static void removeResult(int sId, int rId) throws IDNotRecognisedException {
        for(Result r : allResults) {
            if(r.getRiderId()==rId && r.getStageId()==sId) {
                allResults.remove(r);
                break;
            }
        }
        throw new IDNotRecognisedException("results not found for rider in stage");
    }

    public int getStageId() { return this.stageId; }

    public int getRiderId() { return this.riderId; }

    public LocalTime[] getCheckpoints() {
        LocalTime[] out = new LocalTime[this.checkpoints.length-1];;
        for(int n=0;n<this.checkpoints.length-1; n++) {
            out[n] = getElapsed(checkpoints[n],checkpoints[n+1]);
        }
        return out;
    }

    public LocalTime getElasped() {
        return Result.getElapsed(this.checkpoints[0], this.checkpoints[-1]);
    }

    public static LocalTime getElapsed(LocalTime a, LocalTime b) {
        int hours = (int)a.until(b, ChronoUnit.HOURS);
        int minuites = (int)a.until(b, ChronoUnit.MINUTES);
        int seconds = (int)a.until(b, ChronoUnit.SECONDS);
        return LocalTime.of(hours, minuites, seconds);
    }
    
    public LocalTime[] adjustedCheckpoints() {
        LocalTime[] adjusted = this.getCheckpoints();
        for(int n=0; n<adjusted.length; n++) {
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
