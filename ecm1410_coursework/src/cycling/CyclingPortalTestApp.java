package cycling;


import java.io.IOException;
import java.time.LocalDateTime;

public class CyclingPortalTestApp {
    public static void main(String[] args) throws IDNotRecognisedException,IllegalNameException,InvalidNameException,IOException,ClassNotFoundException{
        if (true){
        CyclingPortal testCyclingPortal = new CyclingPortal();
        int teamId = testCyclingPortal.createTeam("TESTTEAM", "SOME DESC");
        testCyclingPortal.createRace("TESTRACE", "SOME RACE DESC");
        int removeID = testCyclingPortal.createRace("REMOVEME", "SOME RACE DESC");
        testCyclingPortal.removeRaceById(removeID);
        testCyclingPortal.createRider(teamId, "somename", 2001);
        testCyclingPortal.saveCyclingPortal("testsave.data");
        testCyclingPortal.eraseCyclingPortal();
        
        }     
        CyclingPortal testCyclingPortal2 = new CyclingPortal();
        testCyclingPortal2.loadCyclingPortal("testsave.data");
        for (int teamID : testCyclingPortal2.riderManager.getTeams()){
            System.out.println(teamID);
        }
        for (int raceID : testCyclingPortal2.getRaceIds()){
            System.out.println(raceID);
            System.out.println(testCyclingPortal2.viewRaceDetails(raceID));
        }
        System.out.println("RIDER 0 ID : "+testCyclingPortal2.riderManager.getRider(0));
        int testriderId = testCyclingPortal2.riderManager.createRider(0, "testrider", 2000);
        System.out.println("new rider ID SHOULD BE 1 not 0 :"+testriderId);
        System.out.println("RIDER IDS");
        for (int riderId : testCyclingPortal2.riderManager.getTeamRiders(0)){
            System.out.println(riderId);
        }

        System.out.println("RACE REMOVEDID");
        for (int removedID : Race.removedIds){
            System.out.println(removedID);
        }
        
        testCyclingPortal2.eraseCyclingPortal();

        System.out.println("------------------------------- ERASED");

        for (int teamID : testCyclingPortal2.riderManager.getTeams()){
            System.out.println(teamID);
        }
        for (int raceID : testCyclingPortal2.getRaceIds()){
            System.out.println(raceID);
            System.out.println(testCyclingPortal2.viewRaceDetails(raceID));
        }

        System.out.println("RACE REMOVEDID");
        for (int removedID : Race.removedIds){
            System.out.println(removedID);
        }
        //testCyclingPortal2.riderManager.getRider(100); errors correctly
        int racetest1 = testCyclingPortal2.createRace("RACETEST1", "The coolest race ever!");
        try {
            testCyclingPortal2.addStageToRace(racetest1, "Stage1", "DESC TEST", 10.0, LocalDateTime.now(), StageType.FLAT);
        } catch (InvalidLengthException e) {
            e.printStackTrace();
        }
        //System.out.println(testCyclingPortal2.getRidersGeneralClassificationRank(0));


        
    }
    
}
