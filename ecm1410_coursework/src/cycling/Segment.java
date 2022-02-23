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
    private static int idMax = 0;
    private static ArrayList<Segment> allSegments = new ArrayList<Segment>();

    public static Segment getSegment(int segmentId) { return allSegments.get(segmentId); }

    private int segmentId;
    private double segmentLocation;
    private SegmentType segmentType;
    private double segmentAverageGradient;
    private double segmentLength;

    public Segment(double location, SegmentType type, double averageGradient, double length) {
        this.segmentId = idMax++;
        this.segmentLocation = location;
        this.segmentType = type;
        this.segmentAverageGradient = averageGradient;
        this.segmentLength = length;
        Segment.allSegments.add(this);
    }

    public String toString() {
        return Integer.toString(this.segmentId)+this.segmentLocation;
    }

    public int getSegmentId() { return this.segmentId; }
}
