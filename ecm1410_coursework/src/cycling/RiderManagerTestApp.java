package cycling;
import java.util.Arrays;

public class RiderManagerTestApp {
    public static void main(String[] args) throws IllegalNameException,InvalidNameException,IDNotRecognisedException{
        RiderManager myRiderManager = new RiderManager();
        int testTeamId=myRiderManager.createTeam("EthansTeam", "Ethans Team for Racing!!!");
        System.out.println("Creating team EthansTeam");
        int testTeamId2=myRiderManager.createTeam("EthansTeam2", "Ethans Team2 for Racing!!!");
        System.out.println("Creating team EthansTeam2");
        myRiderManager.createRider(testTeamId, "Ethan", 2003);
        System.out.println("Adding Rider Ethan to EthansTeam (Born 2003)");
        myRiderManager.createRider(testTeamId, "Thomas", 2002);
        System.out.println("Adding Rider Thomas to EthansTeam2 (Born 2002)");
        myRiderManager.createRider(testTeamId2, "Jeff", 2002);
        System.out.println("Adding Rider Jeff to EthansTeam2 (Born 2002)");
        printInfo(myRiderManager,testTeamId);
        //int testTeamId2=myRiderManager.createTeam("EthansTeam", "Ethans Team for Racing!!!"); // Throws IllegalName Exception as expected
        System.out.println("Removing Rider with ID of 0 (Ethan)");
        myRiderManager.removeRider(0);
        printInfo(myRiderManager,testTeamId);
        myRiderManager.createRider(testTeamId, "Ethan", 2003);
        System.out.println("Adding Rider Ethan to EthansTeam (Born 2003)");
        //myRiderManager.createRider(testTeamId, "", 2003); // Throws Illegal Arg Exception as expected
        //myRiderManager.createRider(testTeamId, "OldEthan", 1800); // Throws Illegal Arg Expection as edxpected
        //myRiderManager.createRider(testTeamId, null, 2003); //Throws Illegal Arg Exception as expected
        printInfo(myRiderManager,testTeamId);
        System.out.println("Deleting Team 0 & Riders in that team");
        myRiderManager.removeTeam(testTeamId);
        printInfo(myRiderManager, testTeamId2);
        


    }
    public static void printInfo(RiderManager myRiderManager,int testTeamId) throws IDNotRecognisedException{
        System.out.println("-----------------------------------------------");
        System.out.println("RiderNames:"+Arrays.toString(myRiderManager.getRidersNames()));
        System.out.println("Id of Riders in teamId "+testTeamId+":"+Arrays.toString(myRiderManager.getTeamRiders(testTeamId)));
        System.out.println("TeamNames:"+Arrays.toString(myRiderManager.getTeamsNames()));
        System.out.println("Id of Teams:"+Arrays.toString(myRiderManager.getTeams()));
        System.out.println("-----------------------------------------------");
    }
}
