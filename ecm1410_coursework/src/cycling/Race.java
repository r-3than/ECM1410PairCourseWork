package cycling;

import java.util.ArrayList;

/** 
 * Race encapsulates tour races, each of which has a number of associated
 * Stages.
 * 
 * @author Thomas Newbold
 * @version 1.0
 * 
 */
public class Race {
    private static int idMax = 0;
    private static ArrayList<Race> allRaces = new ArrayList<Race>();

    public static Race getRace(int raceId) { return allRaces.get(raceId); }

    private int raceId;
    private String raceName;
    private String raceDescription;
    private ArrayList<Integer> stageIds;

    public Race(String name, String description) {
        this.raceId = idMax++;
        this.raceName = name;
        this.raceDescription = description;
        this.stageIds = new ArrayList<Integer>();
        Race.allRaces.add(this);
    }

    public String toString() {
        return Integer.toString(this.raceId)+this.raceName+this.raceDescription;
    }

    public int[] getStages() {
        int length = this.stageIds.size();
        int[] stageIdsArray = new int[length];
        for(int i=0; i<length; i++) {
            stageIdsArray[i] = this.stageIds.get(i);
        }
        return stageIdsArray;
    }
}
