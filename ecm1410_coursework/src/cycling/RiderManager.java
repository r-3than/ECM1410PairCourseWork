package cycling;

import java.io.Serializable;
import java.util.ArrayList;

/** 
 * RiderManager handles Teams and Riders and their respective interactions.
 * 
 * 
 * @author Ethan Ray
 * @version 1.0
 * 
 */

public class RiderManager implements Serializable{
    public static ArrayList<Rider> allRiders = new ArrayList<>();
    public static ArrayList<Team> allTeams = new ArrayList<>();
    

    /**
     * @param teamID int - A team Id that the rider will belong too. If the ID doesn't exist IDNotRecognisedException is thrown.
     * @param name String - A name for the rider, Has to be non-null or IllegalArgumentException is thrown.
     * @param yearOfBirth int - A year that the rider was born in. Has to be above 1900 or IllegalArgumentException is thrown.
     * @return riderId of the rider created.
     * @throws IDNotRecognisedException teamId int - If the ID doesn't exist IDNotRecognisedException is thrown.
     * @throws IllegalArgumentException yearOfBirth int - A year that the rider was born in. Has to be above 1900 or IllegalArgumentException is thrown.
    */
    public int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException,IllegalArgumentException{
        int teamIndex = getIndexForTeamId(teamID);
        Rider newRider = new Rider(teamID,name,yearOfBirth);
        allRiders.add(newRider);
        Team ridersTeam = allTeams.get(teamIndex);
        ridersTeam.addRider(newRider);
        return newRider.getRiderId();
    }
    /**
     * @param riderId int - A riderId of a rider to be removed. If the ID doesn't exist IDNotRecognisedException is thrown.
     * @throws IDNotRecognisedException riderId int - If the ID doesn't exist IDNotRecognisedException is thrown.
     */
    public void removeRider(int riderId) throws IDNotRecognisedException
    {
        int riderIndex = getIndexForRiderId(riderId);
        int teamId = allRiders.get(riderIndex).getRiderTeamId();
        int teamIndex = getIndexForTeamId(teamId);
        Team riderTeam = allTeams.get(teamIndex);
        riderTeam.removeRiderId(riderId);
        allRiders.remove(riderIndex);
    }
    /**
     * @param riderId int - A riderId of a rider to be searced for. If the ID doesn't exist IDNotRecognisedException is thrown.
     * @throws IDNotRecognisedException riderId int - If the ID doesn't exist IDNotRecognisedException is thrown.
     * @return An int which is the index that maps to the riderId.
     */
    public int getIndexForRiderId(int riderId) throws IDNotRecognisedException{
        int index =-1;
        if (allRiders.size() == 0){
            throw new IDNotRecognisedException("No rider exists with that ID");
        }
        for (int i=0; i<allRiders.size();i++){
            if (allRiders.get(i).getRiderId()==riderId){
                index = i;
                break;
            }
        }
        if (index == -1){
            throw new IDNotRecognisedException("No rider exists with that ID");
        }
        return index;
    }
    /**
     * @param name String - A name for the team, , If the name is null, empty, has more than 30 characters, or has white spaces will throw InvaildNameException.
     * @param description String - A description for the team.
     * @throws IllegalNameException name String - Is a duplicate name of any other Team, IllegalNameException will be thrown.
     * @throws InvailNameException name String - If the name is null, empty, has more than 30 characters, or has white spaces will throw InvaildNameException.
     */
    public int createTeam(String name, String description) throws IllegalNameException, InvalidNameException{
        Team newTeam = new Team(name,description);
        allTeams.add(newTeam);
        return newTeam.getId();
    }
    /**
     * @param teamId int - A teamId of a rider to be removed. If the ID doesn't exist IDNotRecognisedException is thrown.
     * @throws IDNotRecognisedException riderId int - If the ID doesn't exist IDNotRecognisedException is thrown.
     */
    public void removeTeam(int teamId) throws IDNotRecognisedException{ // Delete team and all riders in that team
        int teamIndex = getIndexForTeamId(teamId);
        Team currentTeam = allTeams.get(teamIndex);
        for (Integer riderId : currentTeam.getRiderIds()) {
            removeRider(riderId);
        }
        allTeams.remove(teamIndex);

    }
    /**
     * @return All the teamId's that are currently in the system as an int[]
     */

    public int[] getTeams(){
        int [] allTeamIds = new int[allTeams.size()];
        for (int i=0; i<allTeams.size();i++){
            allTeamIds[i]=allTeams.get(i).getId();
        }
        return allTeamIds;
    }
    /**
     * @param teamId int - A teamId to get RidersId in that team. If the ID doesn't exist IDNotRecognisedException is thrown.
     * @throws IDNotRecognisedException teamId int - If the ID doesn't exist IDNotRecognisedException is thrown.
     * @return All the riderId's in a team as an int[]
     */
    public int[] getTeamRiders(int teamId) throws IDNotRecognisedException{
        Team currentTeam = getTeam(teamId);
        return currentTeam.getRiderIds();

    }
    /**
     * @return All team names in the system as an String[]
     */
    public String[] getTeamsNames(){
        String [] allTeamNames = new String[allTeams.size()];
        for (int i=0; i<allTeams.size();i++){
            allTeamNames[i] = allTeams.get(i).getTeamName();
        }
        return allTeamNames;
    }
    /**
     * @return All rider names in the system as an String[]
     */
    public String[] getRidersNames(){
        String [] allRiderNames = new String[allRiders.size()];
        for (int i=0; i<allRiders.size();i++){
            allRiderNames[i] = allRiders.get(i).getRiderName();
        }
        return allRiderNames;
    }
    /**
     * @param teamId int - A teamId of a team to search for its index. If the ID doesn't exist IDNotRecognisedException is thrown.
     * @throws IDNotRecognisedException teamId int - If the ID doesn't exist IDNotRecognisedException is thrown.
     * @return An int which is the index that maps to the teamId.
     */
    public int getIndexForTeamId(int teamId) throws IDNotRecognisedException{
        int index =-1;
        if (allTeams.size() == 0){
            throw new IDNotRecognisedException("No Team exists with that ID");
        }
        for (int i=0; i<allTeams.size();i++){
            if (allTeams.get(i).getId()==teamId){
                index = i;
                break;
            }
        }
        if (index == -1){
            throw new IDNotRecognisedException("No rider exists with that ID");
        }
        return index;
    }
    /**
     * @param teamId int - A teamId of a team to search for its object. If the ID doesn't exist IDNotRecognisedException is thrown.
     * @throws IDNotRecognisedException teamId int - If the ID doesn't exist IDNotRecognisedException is thrown.
     * @return A Team object with the teamId parsed.
     */
    public Team getTeam(int teamId) throws IDNotRecognisedException{
        int teamIndex = getIndexForTeamId(teamId);
        return allTeams.get(teamIndex);
    }
    /**
     * @param riderId int - A riderId of a team to search for its object. If the ID doesn't exist IDNotRecognisedException is thrown.
     * @throws IDNotRecognisedException riderId int - If the ID doesn't exist IDNotRecognisedException is thrown.
     * @return A Rider object with the riderId parsed.
     */
    public Rider getRider(int riderId) throws IDNotRecognisedException{
        int riderIndex = getIndexForRiderId(riderId);
        return allRiders.get(riderIndex);
    }
    /**
     * @param allTeams ArrayList<Team> - A list of all teams to be set.
     */
    public void setAllTeams(ArrayList<Team> allTeams){

        RiderManager.allTeams = allTeams;
        if (allTeams.size() != 0){
        Team lastTeam = allTeams.get(allTeams.size()-1);
        Team.teamTopId = lastTeam.getId()+1;
        }
    }
    /**
     * @param allRider ArrayList<Rider> - A list of all riders to be set.
     */
    public void setAllRiders(ArrayList<Rider> allRiders){
        RiderManager.allRiders = allRiders;
        if (allRiders.size() != 0){
            Rider lastRider = allRiders.get(allRiders.size()-1);
            Rider.ridersTopId = lastRider.getRiderId()+1;
        }
    }
    /**
     * @return The list of all rider Ids
     */
    public int [] getRiderIds(){
        int[] riderIdArray = new int[allRiders.size()];
        int count = 0;
        for (Rider rider : RiderManager.allRiders){
            riderIdArray[count] = rider.getRiderId();
            count++;

        }
        return riderIdArray;
    }

}
