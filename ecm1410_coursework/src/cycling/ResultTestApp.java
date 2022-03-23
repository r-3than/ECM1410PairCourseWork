package cycling;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
/** 
 * Testing for the Result class.
 * 
 * 
 * @author Thomas Newbold
 * @version 1.0
 * 
 */

public class ResultTestApp {
    public static void main(String[] args) throws IllegalNameException, InvalidNameException,
                            IDNotRecognisedException, InvalidLengthException, InvalidLocationException,
                            InvalidStageStateException, InvalidStageTypeException, DuplicatedResultException,
                            InvalidCheckpointsException {
        CyclingPortal portal = new CyclingPortal();
        // creating races, adding stages/segments
        int race = portal.createRace("France", "Tour de France");
        int[] stages = new int[2];
        stages[0] = portal.addStageToRace(race, "Monaco", "Road Race", 10.0,
                                          LocalDateTime.of(2022, 1, 9, 13, 0),
                                          StageType.TT);
        stages[1] = portal.addStageToRace(race, "Nice", "Mountain Road", 25.0,
                                          LocalDateTime.of(2022, 1, 11, 9, 0),
                                          StageType.HIGH_MOUNTAIN);
        int[] segments = new int[4];
        segments[0] = portal.addIntermediateSprintToStage(stages[1], 5.0);
        segments[1] = portal.addCategorizedClimbToStage(stages[1], 15.0, SegmentType.C3, 4.5, 10.0);
        segments[2] = portal.addCategorizedClimbToStage(stages[1], 21.0, SegmentType.C1, 8.0, 6.0);
        segments[3] = portal.addCategorizedClimbToStage(stages[1], 25.0, SegmentType.C2, 10.0, 4.0);
        portal.concludeStagePreparation(stages[0]);
        portal.concludeStagePreparation(stages[1]);
        // creating teams and riders
        int[] teams = new int[2];
        teams[0] = portal.createTeam("EthansTeam", "Ethans Team for Racing!");
        teams[1] = portal.createTeam("ThomasTeam", "Thomas Team for Racing?");
        int[] riders = new int[4];
        riders[0] = portal.createRider(teams[0], "Ethan", 2003);
        riders[1] = portal.createRider(teams[1], "Thomas", 2003);
        riders[2] = portal.createRider(teams[0], "Jeff", 2002);
        riders[3] = portal.createRider(teams[1], "Bob", 2002);
        // registering times
        portal.registerRiderResultsInStage(stages[0], riders[0], new LocalTime[]{LocalTime.of(13,0,0),LocalTime.of(13,18,51)});
        portal.registerRiderResultsInStage(stages[0], riders[1], new LocalTime[]{LocalTime.of(13,0,0),LocalTime.of(13,19,8)});
        portal.registerRiderResultsInStage(stages[0], riders[2], new LocalTime[]{LocalTime.of(13,0,0),LocalTime.of(13,19,42)});
        portal.registerRiderResultsInStage(stages[0], riders[3], new LocalTime[]{LocalTime.of(13,0,0),LocalTime.of(13,19,11)});

        portal.registerRiderResultsInStage(stages[1], riders[0], new LocalTime[]{LocalTime.of(9,0,0),LocalTime.of(9,10,0), LocalTime.of(9,32,44),LocalTime.of(9,43,2), LocalTime.of(9,58,30),LocalTime.of(9,58,30)});
        portal.registerRiderResultsInStage(stages[1], riders[1], new LocalTime[]{LocalTime.of(9,0,0),LocalTime.of(9,9,59), LocalTime.of(9,32,33),LocalTime.of(9,43,21), LocalTime.of(9,59,20),LocalTime.of(9,59,20)});
        portal.registerRiderResultsInStage(stages[1], riders[2], new LocalTime[]{LocalTime.of(9,0,0),LocalTime.of(9,10,10), LocalTime.of(9,33,1),LocalTime.of(9,45,0), LocalTime.of(10,1,4),LocalTime.of(10,1,4)});
        portal.registerRiderResultsInStage(stages[1], riders[3], new LocalTime[]{LocalTime.of(9,0,0),LocalTime.of(9,10,11), LocalTime.of(9,32,58),LocalTime.of(9,44,40), LocalTime.of(10,0,11),LocalTime.of(10,0,11)});
        // fetching points
        //System.out.println(Result.getResult(0, 0).toString());
        Result[] stage1 = Result.getResultsInStage(stages[0]);
        Result[] stage2 = Result.getResultsInStage(stages[1]);
        //System.out.println(stage1.length);
        for(Result r : stage1) {
            System.out.println(r.toString());
        }
        System.out.println("");
        for(Result r : stage2) {
            System.out.println(r.toString());
        }
        System.out.println("\nStage 0:");
        System.out.println(Arrays.toString(portal.getRidersRankInStage(stages[0])));
        System.out.println(Arrays.toString(portal.getRidersPointsInStage(stages[0])));
        System.out.println(Arrays.toString(portal.getRidersMountainPointsInStage(stages[0])));
        System.out.println("Adjusted elapsed times in stage 0:");
        System.out.println(Arrays.toString(portal.getRankedAdjustedElapsedTimesInStage(stages[0])));
        System.out.println("Stage 1:");
        System.out.println(Arrays.toString(portal.getRidersRankInStage(stages[1])));
        System.out.println(Arrays.toString(portal.getRidersPointsInStage(stages[1])));
        System.out.println(Arrays.toString(portal.getRidersMountainPointsInStage(stages[1])));

        System.out.println("\nRace Classification:");
        System.out.println(Arrays.toString(portal.getRidersPointClassificationRank(race)));
        System.out.println(Arrays.toString(portal.getRidersPointsInRace(race)));
        System.out.println("Race Classification (Mountain):");
        System.out.println(Arrays.toString(portal.getRidersMountainPointClassificationRank(race)));
        System.out.println(Arrays.toString(portal.getRidersMountainPointsInRace(race)));
    }
}
