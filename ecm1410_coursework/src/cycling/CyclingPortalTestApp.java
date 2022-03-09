package cycling;


import java.io.IOException;

public class CyclingPortalTestApp {
    public static void main(String[] args) throws IDNotRecognisedException,IllegalNameException,InvalidNameException,IOException,ClassNotFoundException{
        //CyclingPortal testCyclingPortal = new CyclingPortal();
        //int teamId = testCyclingPortal.createTeam("TESTTEAM", "SOME DESC");
        //testCyclingPortal.createRace("TESTRACE", "SOME RACE DESC");
        //testCyclingPortal.createRider(teamId, "SOMETEAM", 2001);
        //testCyclingPortal.saveCyclingPortal("testsave.data");
        CyclingPortal testCyclingPortal2 = new CyclingPortal();
        testCyclingPortal2.loadCyclingPortal("testsave.data");
        for (int teamID : testCyclingPortal2.riderManager.getTeams()){
            System.out.println(teamID);
        }
        for (int raceID : testCyclingPortal2.getRaceIds()){
            System.out.println(raceID);
            System.out.println(testCyclingPortal2.viewRaceDetails(raceID));
        }
        
    }
    
}
