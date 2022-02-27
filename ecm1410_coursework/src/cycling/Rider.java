package cycling;

/** 
 * Rider Class holds the riders teamId,riderId,name and yearOfBirth
 * 
 * 
 * @author Ethan Ray
 * @version 1.0
 * 
 */


public class Rider {
    public static int ridersTopId;
    private int riderId;
    private int teamID;
    private String name;
    private int yearOfBirth;
    

    /**
     * @param riderId int - A unique Id for each rider is given 
     * @param teamID int - A team Id that the rider will belong too
     * @param name String - A name for the rider, Has to be non-null or IllegalArgumentException is thrown.
     * @param yearOfBirth int - A year that the rider was born in. Has to be above 1900 or IllegalArgumentException is thrown.
     */
    public Rider(int teamID, String name, int yearOfBirth)throws IllegalArgumentException
    {
        this.riderId = ridersTopId++;
        this.teamID = teamID;
        if (name == ""){
            throw new IllegalArgumentException("Illegal name entered for rider");
        }
        this.name = name;
        if (yearOfBirth < 1900){
            throw new IllegalArgumentException("Illegal value for yearOfBirth given please enter a value above 1900.");
        }
        this.yearOfBirth = yearOfBirth;
    }
    /**
     * @return The RiderId of the rider.
     */
    public int getRiderId(){
        return this.riderId;
    }
    /**
     * @return The team Id that the rider belongs to/
     */
    public int getRiderTeamId(){
        return this.teamID;
    }
    /**
     * @return The rider's name.
     */
    public String getRiderName(){
        return this.name;
    }
    /**
     * @return The the year of birth of the rider.
     */
    public int getRiderYOB(){
        return this.yearOfBirth;
    }

}
