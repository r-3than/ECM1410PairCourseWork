package cycling;

import java.time.LocalDateTime;

public class RaceTestApp {
    public static void main(String[] args) throws IllegalNameException,
                            InvalidNameException, InvalidLengthException,
                            IDNotRecognisedException {
        Race r1 = new Race("France", "Tour de France");
        r1.addStageToRace("Monaco", "Road Race", 50.1, LocalDateTime.of(2022, 1, 9, 13, 0), StageType.FLAT);
        r1.toString();
        int[] stageIDs = r1.getStages();
        for(int id : stageIDs) {
            Stage.toString(id);
        }
    }
}
