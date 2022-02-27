package cycling;

import java.util.ArrayList;
/** 
 * Race encapsulates tour races, each of which has a number of associated
 * Stages.
 * 
 * @author Ethan Ray
 * @version 1.0
 * 
 */


public class Rider {
    private int riderId;
    private int teamID;
    private String name;
    private int yearOfBirth;
    
    public Rider(int riderId,int teamID, String name, int yearOfBirth)throws IllegalArgumentException
    {
        this.riderId = riderId;
        this.teamID = teamID;
        if (name == ""){
            throw new IllegalArgumentException("Illegal name entered for rider");
        }
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

}
