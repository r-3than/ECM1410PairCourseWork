package cycling;

import java.util.ArrayList;

public class RiderManager {
    private static ArrayList<Rider> allRiders = new ArrayList<Rider>();
    
    int createRider(int teamID, String name, int yearOfBirth) throws IDNotRecognisedException,IllegalArgumentException{
        Rider newRider = new Rider(teamID,name,yearOfBirth);
        allRiders.add(newRider);
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
}
