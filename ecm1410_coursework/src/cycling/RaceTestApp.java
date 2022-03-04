package cycling;

import java.time.LocalDateTime;

public class RaceTestApp {
    public static void main(String[] args) throws IllegalNameException,
                            InvalidNameException, InvalidLengthException,
                            IDNotRecognisedException, InvalidStageStateException,
                            InvalidStageTypeException, InvalidLocationException {
        Race r1 = new Race("France", "Tour de France");
        r1.addStageToRace("Monaco", "Road Race", 10.0, LocalDateTime.of(2022, 1, 9, 13, 0), StageType.TT);
        r1.addStageToRace("Nice", "Mountain Road", 25.0, LocalDateTime.of(2022, 1, 11, 9, 0), StageType.HIGH_MOUNTAIN);
        int[] stageIDs = r1.getStages();
        Stage s2 = Stage.getStage(stageIDs[1]);
        s2.addSegmentToStage(5.0, SegmentType.SPRINT, 1.0, 5.0);
        s2.addSegmentToStage(15.0, SegmentType.C3, 4.5, 10.0);
        s2.addSegmentToStage(21.0, SegmentType.C1, 8.0, 6.0);
        s2.addSegmentToStage(25.0, SegmentType.C2, 10.0, 4.0);
        int[] segmentIds = s2.getSegments();
        // instance states
        System.out.println(r1.toString());
        System.out.println(s2.toString());
        for(int id : segmentIds) {
            System.out.println(Segment.getSegment(segmentIds[id]));
        }
    }
}
