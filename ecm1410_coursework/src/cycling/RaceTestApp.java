package cycling;

import java.time.LocalDateTime;

public class RaceTestApp {
    public static void main(String[] args) throws IllegalNameException,
                            InvalidNameException, InvalidLengthException,
                            IDNotRecognisedException, InvalidStageStateException,
                            InvalidStageTypeException, InvalidLocationException {
        Race r1 = new Race("France", "Tour de France");
        r1.addStageToRace("Monaco", "Road Race", 50.1, LocalDateTime.of(2022, 1, 9, 13, 0), StageType.HIGH_MOUNTAIN);
        int[] stageIDs = r1.getStages();
        Stage s1 = Stage.getStage(stageIDs[0]);
        s1.addSegmentToStage(16.7, SegmentType.C1, 12.0, 16.7);
        int[] segmentIds = s1.getSegments();
        // instance states
        System.out.println(r1.toString());
        System.out.println(s1.toString());
        System.out.println(Segment.getSegment(segmentIds[0]));
    }
}
