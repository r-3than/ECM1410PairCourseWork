package cycling;

import java.time.LocalTime;

public class Result {
    private int stageId;
    private int riderId;
    private LocalTime[] checkpoints;

    public Result(int sId, int rId, LocalTime... check) throws
                  IDNotRecognisedException, InvalidStageStateException {
        if() {
            throw new IDNotRecognisedException();
        } else if(Stage.getStageState(sId).equals(StageState.BUILDING)) {
            throw new InvalidStageStateException("stage is not waiting for results");
        }
        this.stageId = sId;
        this.riderId = rId;
        this.checkpoints = check;
    }
}
