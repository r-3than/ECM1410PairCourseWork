package cycling;
import java.util.Arrays;

public class RiderManagerTestApp {
    public static void main(String[] args) throws IllegalNameException,InvalidNameException,IDNotRecognisedException{
        RiderManager myRiderManager = new RiderManager();
        int testTeamId=myRiderManager.createTeam("EthansTeam", "Ethans Team for Racing!!!");
        int testTeamId2=myRiderManager.createTeam("EthansTeam2", "Ethans Team2 for Racing!!!");
        myRiderManager.createRider(testTeamId, "Ethan", 2003);
        myRiderManager.createRider(testTeamId, "Thomas", 2002);
        printInfo(myRiderManager,testTeamId);
        //int testTeamId2=myRiderManager.createTeam("EthansTeam", "Ethans Team for Racing!!!"); // Throws IllegalName Exception as expected
        myRiderManager.removeRider(0);
        printInfo(myRiderManager,testTeamId);
        myRiderManager.createRider(testTeamId, "Ethan", 2003);
        //myRiderManager.createRider(testTeamId, "", 2003); // Throws Illegal Arg Exception as expected
        //myRiderManager.createRider(testTeamId, "OldEthan", 1800); // Throws Illegal Arg Expection as edxpected
        //myRiderManager.createRider(testTeamId, null, 2003); //Throws Illegal Arg Exception as expected
        printInfo(myRiderManager,testTeamId);
        System.out.println(Arrays.toString(myRiderManager.getTeams()));
        


    }
    public static void printInfo(RiderManager myRiderManager,int testTeamId) throws IDNotRecognisedException{
        System.out.println("-----------------------------------------------");
        System.out.println("RiderNames:"+Arrays.toString(myRiderManager.getRidersNames()));
        System.out.println("Id of Riders in teamId "+testTeamId+":"+Arrays.toString(myRiderManager.getTeamRiders(testTeamId)));
        System.out.println("TeamNames:"+Arrays.toString(myRiderManager.getTeamsNames()));
        System.out.println("Id of Teams:"+Arrays.toString(myRiderManager.getTeams()));
    }
}
