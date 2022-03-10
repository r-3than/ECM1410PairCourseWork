package cycling;

import java.time.LocalDateTime;

public class ResultTestApp {
    public static void main(String[] args) throws IllegalNameException, InvalidNameException,
                            IDNotRecognisedException, InvalidLengthException, InvalidLocationException,
                            InvalidStageStateException, InvalidStageTypeException {
        RiderManager riderManager = new RiderManager();
        int testTeamId = riderManager.createTeam("EthansTeam", "Ethans Team for Racing!!!");
        int testTeamId2 = riderManager.createTeam("EthansTeam2", "Ethans Team2 for Racing!!!");
        riderManager.createRider(testTeamId, "Ethan", 2003);
        riderManager.createRider(testTeamId, "Thomas", 2002);
        riderManager.createRider(testTeamId2, "Jeff", 2002);

        Race r1 = new Race("France", "Tour de France");
        r1.addStageToRace("Monaco", "Road Race", 10.0, LocalDateTime.of(2022, 1, 9, 13, 0), StageType.TT);
        r1.addStageToRace("Nice", "Mountain Road", 25.0, LocalDateTime.of(2022, 1, 11, 9, 0), StageType.HIGH_MOUNTAIN);
        Stage s1 = Stage.getStage(r1.getStages()[1]);
        s1.addSegmentToStage(5.0, SegmentType.SPRINT, 1.0, 5.0);
        s1.addSegmentToStage(15.0, SegmentType.C3, 4.5, 10.0);
        s1.addSegmentToStage(21.0, SegmentType.C1, 8.0, 6.0);
        s1.addSegmentToStage(25.0, SegmentType.C2, 10.0, 4.0);
    }
}
