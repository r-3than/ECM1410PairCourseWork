package cycling;

import java.time.LocalDateTime;

public class RaceTestApp {
    public static void main(String[] args) throws IllegalNameException,
                            InvalidNameException, InvalidLengthException,
                            IDNotRecognisedException, InvalidStageStateException,
                            InvalidStageTypeException, InvalidLocationException {
        System.out.println("instanstiating...\n");               
        Race r1 = new Race("France", "Tour de France");
        r1.addStageToRace("Monaco", "Road Race", 10.0, LocalDateTime.of(2022, 1, 9, 13, 0), StageType.TT);
        r1.addStageToRace("Nice", "Mountain Road", 25.0, LocalDateTime.of(2022, 1, 11, 9, 0), StageType.HIGH_MOUNTAIN);
        int[] stageIDs = r1.getStages();
        Stage s1 = Stage.getStage(stageIDs[1]);
        s1.addSegmentToStage(5.0, SegmentType.SPRINT, 1.0, 5.0);
        s1.addSegmentToStage(15.0, SegmentType.C3, 4.5, 10.0);
        s1.addSegmentToStage(21.0, SegmentType.C1, 8.0, 6.0);
        s1.addSegmentToStage(25.0, SegmentType.C2, 10.0, 4.0);
        int[] segmentIds = s1.getSegments();
        // instance states
        System.out.println(r1.toString());
        System.out.println(Stage.getStage(stageIDs[0]).toString());
        System.out.println(s1.toString());
        for(int id : segmentIds) {
            System.out.println(Segment.getSegment(segmentIds[id]));
        }
        System.out.println("\nremoving instances...  [stage 0; segment 3]\n");
        Race.removeStage(0);
        s1 = Stage.getStage(r1.getStages()[0]);
        Stage.removeSegment(3);
        segmentIds = s1.getSegments();
        // instance states
        System.out.println(r1.toString());
        System.out.println(s1.toString());
        for(int id : segmentIds) {
            System.out.println(Segment.getSegment(segmentIds[id]));
        }
    }
}
