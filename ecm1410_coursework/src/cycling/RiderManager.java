package cycling;

import java.util.ArrayList;

public class RiderManager {
    private static ArrayList<Rider> allRiders = new ArrayList<Rider>();
    private static ArrayList<Team> allTeams = new ArrayList<Team>();
    

    /**
     * @param teamID int - A team Id that the rider will belong too. If the ID doesn't exist IDNotRecognisedException is thrown.
     * @param name String - A name for the rider, Has to be non-null or IllegalArgumentException is thrown.
     * @param yearOfBirth int - A year that the rider was born in. Has to be above 1900 or IllegalArgumentException is thrown.
     * @return riderId of the rider created.
     */
    int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException,IllegalArgumentException{
        int teamIndex = getIndexForTeamId(teamID);
        Rider newRider = new Rider(teamID,name,yearOfBirth);
        allRiders.add(newRider);
        Team ridersTeam = allTeams.get(teamIndex);
        ridersTeam.addRider(newRider);
        return newRider.getRiderId();
    }
    /**
     * @param riderId int - A riderId of a rider to be removed. If the ID doesn't exist IDNotRecognisedException is thrown.
     */
    void removeRider(int riderId) throws IDNotRecognisedException
    {
        int riderIndex = getIndexForRiderId(riderId);
        int teamId = allRiders.get(riderIndex).getRiderTeamId();
        int teamIndex = getIndexForTeamId(teamId);
        Team riderTeam = allTeams.get(teamIndex);
        riderTeam.removeRiderId(riderId);
        allRiders.remove(riderIndex);
    }
    int getIndexForRiderId(int riderId) throws IDNotRecognisedException{
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

    int createTeam(String name, String description) throws IllegalNameException, InvalidNameException{
        Team newTeam = new Team(name,description);
        allTeams.add(newTeam);
        return newTeam.getId();
    }

    void removeTeam(int teamId) throws IDNotRecognisedException{ // Delete team and all riders in that team
        int teamIndex = getIndexForTeamId(teamId);
        Team currentTeam = allTeams.get(teamIndex);
        for (Integer riderId : currentTeam.getRiderIds()) {
            removeRider(riderId);
        }
        allTeams.remove(teamIndex);

    }

    int[] getTeams(){
        int [] allTeamIds = new int[allTeams.size()];
        for (int i=0; i<allTeams.size();i++){
            allTeamIds[i]=allTeams.get(i).getId();
        }
        return allTeamIds;
    }
    int[] getTeamRiders(int teamId) throws IDNotRecognisedException{
        int teamIndex = getIndexForTeamId(teamId);
        Team currentTeam = allTeams.get(teamIndex);
        return currentTeam.getRiderIds();

    }
    String[] getTeamsNames(){
        String [] allTeamNames = new String[allTeams.size()];
        for (int i=0; i<allTeams.size();i++){
            allTeamNames[i] = allTeams.get(i).getTeamName();
        }
        return allTeamNames;
    }
    String[] getRidersNames(){
        String [] allRiderNames = new String[allRiders.size()];
        for (int i=0; i<allRiders.size();i++){
            allRiderNames[i] = allRiders.get(i).getRiderName();
        }
        return allRiderNames;
    }
    int getIndexForTeamId(int teamId) throws IDNotRecognisedException{
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
    Team getTeam(int teamId) throws IDNotRecognisedException{
        int teamIndex = getIndexForTeamId(teamId);
        return allTeams.get(teamIndex);
    }
    Rider getRider(int riderId) throws IDNotRecognisedException{
        int riderIndex = getIndexForRiderId(riderId);
        return allRiders.get(riderIndex);
    }


}
