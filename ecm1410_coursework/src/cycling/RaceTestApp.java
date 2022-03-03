package cycling;

public class RaceTestApp {
    public static void main(String[] args) throws IllegalNameException,
                            InvalidNameException, IDNotRecognisedException {
        Race r1 = new Race("France", "Monaco Race");
        r1.toString();
        int[] stageIDs = r1.getStages();
        for(int id : stageIDs) {
            Stage.toString(id);
        }
    }
}
