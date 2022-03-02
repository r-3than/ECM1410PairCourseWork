package cycling;

import java.util.ArrayList;

public class RiderManager {
    private static ArrayList<Rider> allRiders = new ArrayList<Rider>();
    private static ArrayList<Team> allTeams = new ArrayList<Team>();
    
    int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException,IllegalArgumentException{
        int teamIndex = getIndexForTeamId(teamID);

        Rider newRider = new Rider(teamID,name,yearOfBirth);
        allRiders.add(newRider);
        Team ridersTeam = allTeams.get(teamIndex)
        ridersTeam.addRider(newRider);
        return newRider.getRiderId();
    }
    void removeRider(int riderId) throws IDNotRecognisedException
    {
        int riderIndex = getIndexForRiderId(riderId);
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

    void removeTeam(int teamId) throws IDNotRecognisedException{
        int teamIndex = getIndexForTeamId(teamId);
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


}
