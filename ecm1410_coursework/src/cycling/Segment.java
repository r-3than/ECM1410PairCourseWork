package cycling;

import java.util.ArrayList;

/** 
 * Segment encapsulates race segments
 * 
 * @author Thomas Newbold
 * @version 1.0
 * 
 */
public class Segment {
    // Static class attributes
    private static int idMax = 0;
    private static ArrayList<Segment> allSegments = new ArrayList<Segment>();

    /**
     * @param segmentId The ID of the segment instance to fetch
     * @return The segment instance with the associated ID
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID
     */
    public static Segment getSegment(int segmentId) throws
                                     IDNotRecognisedException {
        if(segmentId<Segment.idMax && segmentId >= 0) {
            return allSegments.get(segmentId);
        } else {
            throw new IDNotRecognisedException("segmentId out of range");
        }
    }

    /**
     * @param segmentId The ID of the segment instance to remove
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID 
     */
    public static void removeSegment(int segmentId) throws
                                     IDNotRecognisedException {
        if(segmentId<Segment.idMax && segmentId >= 0) {
            allSegments.remove(segmentId);
            Segment.idMax--;
            for(int i=segmentId;i<allSegments.size();i++) {
                getSegment(i).segmentId--; 
            }
        } else {
            throw new IDNotRecognisedException("segmentId out of range");
        }
    }

    // Instance attributes
    private int segmentId;
    private double segmentLocation;
    private SegmentType segmentType;
    private double segmentAverageGradient;
    private double segmentLength;

    /**
     * Segment constructor; creates a new segment and adds to allSegment array.
     * 
     * @param location The location of the finish of the new segment in the stage
     * @param type The type of the new segment
     * @param averageGradient The average gradient of the new segment
     * @param length The length of the new segment
     */
    public Segment(double location, SegmentType type, double averageGradient,
                   double length) {
        this.segmentId = idMax++;
        this.segmentLocation = location;
        this.segmentType = type;
        this.segmentAverageGradient = averageGradient;
        this.segmentLength = length;
        Segment.allSegments.add(this);
    }

    /**
     * @return A string representation of the segment instance
     */
    public String toString() {
        String id = Integer.toString(this.segmentId);
        String location = Double.toString(this.segmentLocation);
        String type;
        switch (this.segmentType) {
            case SPRINT:
                type = "Sprint";
                break;
            case C4:
                type = "Category 4 Climb";
                break;
            case C3:
                type = "Category 3 Climb";
                break;
            case C2:
                type = "Category 2 Climb";
                break;
            case C1:
                type = "Category 1 Climb";
                break;
            case HC:
                type = "Hors Categorie";
                break;
            default:
                type = "null category";
        }
        String averageGrad = Double.toString(this.segmentAverageGradient);
        String length = Double.toString(this.segmentLength);
        return String.format("Segment[%s]: %s; %skm; Location=%s; Gradient=%s;",
                             id, type, length, location, averageGrad);
    }

    /**
     * @param id The ID of the segment
     * @return A string representation of the segment instance
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID
     */
    public static String toString(int id) throws IDNotRecognisedException {
        return getSegment(id).toString();
    }

    /**
     * @return The integer segmentId for the segment instance
     */
    public int getSegmentId() { return this.segmentId; }

    /**
     * @return The integer representing the location of the segment instance
     */
    public double getSegmentLocation() { return this.segmentLocation; }

    /**
     * @param id The ID of the segment
     * @return The integer representing the location of the segment instance
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID
     */
    public static double getSegmentLocation(int id) throws
                                            IDNotRecognisedException {
        return getSegment(id).segmentLocation;
    }

    /**
     * @return The type of the segment instance
     */
    public SegmentType getSegmentType() { return this.segmentType; }

    /**
     * @param id The ID of the segment
     * @return The type of the segment instance
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID
     */
    public static SegmentType getSegmentType(int id) throws
                                             IDNotRecognisedException {
        return getSegment(id).segmentType;
    }

    /**
     * @return The average gradient of the segment instance
     */
    public double getSegmentAverageGradient() {
        return this.segmentAverageGradient;
    }

    /**
     * @param id The ID of the segment
     * @return The average gradient of the segment instance
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID
     */
    public static double getSegmentAverageGradient(int id) throws
                                                   IDNotRecognisedException {
        return getSegment(id).segmentAverageGradient;
    }

    /**
     * @return The length of the segment instance
     */
    public double getSegmentLength() { return this.segmentLength; }

    /**
     * @param id The ID of the segment
     * @return The length of the segment instance
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID
     */
    public static double getSegmentLength(int id) throws IDNotRecognisedException {
        return getSegment(id).segmentLength;
    }

    /**
     * @param location The new location for the segment instance
     */
    public void setSegmentLocation(double location) {
        this.segmentLocation = location;
    }

    /**
     * @param id The ID of the segment to be updated
     * @param location The new location for the segment instance
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID
     */
    public static void setSegmentLocation(int id, double location) throws
                                          IDNotRecognisedException {
        getSegment(id).setSegmentLocation(location);
    }

    /**
     * @param type The new type for the segment instance
     */
    public void setSegmentType(SegmentType type) {
        this.segmentType = type;
    }

    /**
     * @param id The ID of the segment to be updated
     * @param type The new type for the segment instance
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID
     */
    public static void setSegmentType(int id, SegmentType type) throws
                                      IDNotRecognisedException {
        getSegment(id).setSegmentType(type);
    }

    /**
     * @param averageGradient The new average gradient for the segment instance
     */
    public void setSegmentAverageGradient(double averageGradient) {
        this.segmentAverageGradient = averageGradient;
    }

    /**
     * @param id The ID of the segment to be updated
     * @param averageGradient The new average gradient for the segment instance
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID
     */
    public static void setSegmentAverageGradient(int id, double averageGradient)
                                                 throws IDNotRecognisedException {
        getSegment(id).setSegmentAverageGradient(averageGradient);
    }

    /**
     * @param length The new length for the segment instance
     */
    public void setSegmentLength(double length) {
        this.segmentLength = length;
    }

    /**
     * @param id The ID of the segment to be updated
     * @param length The new length for the segment instance
     * @throws IDNotRecognisedException If no segment exists with the requested
     *                                  ID
     */
    public static void setSegmentLength(int id, double length) throws
                                        IDNotRecognisedException {
        getSegment(id).setSegmentLength(length);
    }
}
