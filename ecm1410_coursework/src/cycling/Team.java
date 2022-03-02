package cycling;
import java.util.ArrayList;
/** 
 * Team Class holds the teamId,name,description and riderIds belonging to that team.
 * 
 * 
 * @author Ethan Ray
 * @version 1.0
 * 
 */




public class Team {
    public static ArrayList<String> teamNames = new ArrayList<String>();
    public static int amtTeams = 0;

    private int teamID;
    private String name;
    private String description;
    private ArrayList<Integer> riderIds = new ArrayList<Integer>();


    /**
     * @param name String - A name for the team, , If the name is null, empty, has more than 30 characters, or has white spaces will throw InvaildNameException
     * @param description String - A description for the team.
     */
    public Team(String name, String description) throws IllegalNameException, InvalidNameException
    {
        if (name == "" || name.length()>30 || name.contains(" ")){
            throw new InvalidNameException("Team name cannot be empty, longer than 30 characters , or has white spaces.");
        } 
        for (int i = 0;i<teamNames.size();i++){
            if (teamNames.get(i) == name){
                throw new IllegalNameException("That team name already exsists!");
            }
        }

        teamNames.add(name);
        this.teamID = amtTeams++;
        this.name = name;
        this.description = description;
    }
    /**
     * @param rider Rider - A rider to add to the team.
     */
    public void addRider(Rider rider){

        this.riderIds.add(rider.getRiderId());
    }
    /**
     * @param riderId int - A riderId to be removed from the team.
     */
    public void removeRiderId(int riderId){
        for (int i =0;i<this.riderIds.size();i++){
            if (this.riderIds.get(i)==riderId){
                this.riderIds.remove(i);
                break;
            }
        }
    }
    /**
     * @return An Array of integers - which are the riderIds in that team.
     */
    public int[] getRiderIds(){
        int [] currentRiderIds = new int[this.riderIds.size()];
        for (int i=0; i<this.riderIds.size();i++){
            currentRiderIds[i]=this.riderIds.get(i);
        }
        return currentRiderIds;
    }
    /**
     * @return A Integer - teamId of the team.
     */
    public int getId(){
        return this.teamID;
    }
    /**
     * @return A String - Name of the team.
     */
    public String getName(){
        return this.name;
    }
    /**
     * @return A String - The description of the team. 
     */
    public String getDescription(){
        return this.description;
    }
}
