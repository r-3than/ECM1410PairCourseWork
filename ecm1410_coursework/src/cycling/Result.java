package cycling;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Result encapsulates rider results per stage, and handles time adjustments and
 * rankings (scoring is done externally based on points distributions defined in
 * Cycling Portal)
 * 
 * @author Thomas Newbold
 * @version 1.1
 */
public class Result implements Serializable {
    // Static class attributes
    public static ArrayList<Result> allResults = new ArrayList<Result>();

    /**
     * @param stageId The ID of the stage
     * @return An array of all results for a stage
     */
    public static Result[] getResultsInStage(int stageId) {
        ArrayList<Result> stage = new ArrayList<Result>();
        for(Result r : allResults) {
            stage.add(r);
        }
        stage.removeIf(r -> r.getStageId()!=stageId);
        Result[] resultsForStage = new Result[stage.size()];
        for(int i=0; i<stage.size(); i++) {
            resultsForStage[i] = stage.get(i);
        }
        return resultsForStage;
    }

    /**
     * @param riderId The ID of the driver
     * @return An array of all results for a driver
     */
    public static Result[] getResultsForRider(int riderId) {
        ArrayList<Result> rider = new ArrayList<Result>(allResults);
        rider.removeIf(r -> r.getRiderId()!=riderId);
        Result[] resultsForRider = new Result[rider.size()];
        for(int i=0; i<rider.size(); i++) {
            resultsForRider[i] = rider.get(i);
        }
        return resultsForRider;
    }

    // Instance attributes
    private int stageId;
    private int riderId;
    private LocalTime[] checkpoints;

    /**
     * Result constructor; creates a new result entry and adds to the
     * allResults array.
     * 
     * @param sId The ID of the stage the result refers to
     * @param rId The ID of the rider who achieved the result
     * @param check An array of times at which the rider reached each
     *              checkpoint (including start and finish)
     */
    public Result(int sId, int rId, LocalTime... check) {
        this.stageId = sId;
        this.riderId = rId;
        this.checkpoints = check;
        Result.allResults.add(this);
    }

    /**
     * @return A string representation of the Result instance
     */
    public String toString() {
        String sId = Integer.toString(this.stageId);
        String rId = Integer.toString(this.riderId);
        int l = this.getCheckpoints().length;
        String times[] = new String[l];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        for(int i=0; i<l; i++) {
            times[i] = this.getCheckpoints()[i].format(formatter);
        }
        return String.format("Stage[%s]-Rider[%s]: SplitTimes=%s; Total=%s",
                             sId, rId, Arrays.toString(times),
                             getTotalElasped().format(formatter));
    }

    /**
     * @param sId The ID of the stage of the result instance
     * @param rId The ID of the associated rider to the result instance
     * @return The Result instance
     * @throws IDNotRecognisedException If an instance for the rider/stage
     *                                  combination is not found in the
     *                                  allResults array
     */
    public static Result getResult(int sId, int rId) throws IDNotRecognisedException {
        for(Result r : allResults) {
            if(r.getRiderId()==rId && r.getStageId()==sId) {
                return r;
            }
        }
        throw new IDNotRecognisedException("results not found for rider in stage");
    }

    /**
     * @param sId The ID of the stage of the result instance to remove
     * @param rId The ID of the associated rider to the result instance to remove
     * @throws IDNotRecognisedException If an instance for the rider/stage
     *                                  combination is not found in the
     *                                  allResults array
     */
    public static void removeResult(int sId, int rId) throws IDNotRecognisedException {
        for(Result r : allResults) {
            if(r.getRiderId()==rId && r.getStageId()==sId) {
                allResults.remove(r);
                break;
            }
        }
        throw new IDNotRecognisedException("results not found for rider in stage");
    }

    /**
     * @return The stageId of the stage the result refers to
     */
    public int getStageId() { return this.stageId; }

    /**
     * @return The riderId of the rider associated with the result
     */
    public int getRiderId() { return this.riderId; }

    /**
     * @return An array of the split times between each checkpoint
     */
    public LocalTime[] getCheckpoints() {
        LocalTime[] out = new LocalTime[this.checkpoints.length-1];
        for(int n=0;n<this.checkpoints.length-1; n++) {
            out[n] = getElapsed(checkpoints[n],checkpoints[n+1]);
        }
        return out;
    }

    /**
     * @return The total time elapsed between the start and end checkpoints
     */
    public LocalTime getTotalElasped() {
        LocalTime[] times = this.checkpoints;
        return Result.getElapsed(times[0], times[times.length-1]);
    }

    /**
     * @param a Start time
     * @param b End time
     * @return The time difference between two times, a and b
     */
    public static LocalTime getElapsed(LocalTime a, LocalTime b) {
        int hours = (int)a.until(b, ChronoUnit.HOURS);
        int minuites = (int)a.until(b, ChronoUnit.MINUTES);
        int seconds = (int)a.until(b, ChronoUnit.SECONDS);
        double nanos = a.until(b, ChronoUnit.NANOS);
        nanos = nanos%Math.pow(10, 9);
        return LocalTime.of(hours%24, minuites%60, seconds%60, (int)nanos);
    }
    
    /**
     * @return An array of the checkpoint times, adjusted to a threshold of
     *         one second
     */
    public LocalTime[] adjustedCheckpoints() {
        LocalTime[] adjusted = this.getCheckpoints();
        for(int n=0; n<adjusted.length; n++) {
            adjusted[n] = adjustedCheckpoint(n);            
        }
        return adjusted;
    }

    /**
     * Recursive adjuster, used in {@link #adjustedCheckpoints()}.
     * 
     * @param n The index of the checkpoint to adjust
     * @return The adjusted time for checkpoint n
     */
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
