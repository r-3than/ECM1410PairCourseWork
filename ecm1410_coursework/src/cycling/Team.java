package cycling;

import java.util.ArrayList;


public class Team {
    public static ArrayList<String> teamNames = new ArrayList<String>();
    public static int amtTeams = 0;

    private int teamID;
    private String name;
    private String description;
    private ArrayList<Integer> riderIds = new ArrayList<Integer>();

    public Team(String name, String description) throws IllegalNameException, InvalidNameException
    {
        if (name == "" || name.length()>30){
            throw new InvalidNameException("Team name cannot be empty or longer than 30 characters.");
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
    public void addRider(Rider rider){

        this.riderIds.add(rider.getRiderId());
    }
    public void removeRiderId(int riderId){
        for (int i =0;i<this.riderIds.size();i++){
            if (this.riderIds.get(i)==riderId){
                this.riderIds.remove(i);
                break;
            }
        }
    }

    public int getId(){
        return this.teamID;
    }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
}
