import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 * 
 */

/**
 * @author David Price
 *
 */
public class FastCollinearPoints {
    private Point[] pts;
    private Point[] sortedPts;
    private ArrayList<Point> starts;
    private ArrayList<Point> ends;
    private ArrayList<LineSegment> segs;
    private int n;
   
    
    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
     // check if Points array is null - if so throw NullPointerException
        if (points == null) {
            throw new NullPointerException("Points array is null");
        }
        // check if any element in Points array is null - if so throw NullPointerException
        for (Point pt : points) {
            if (pt == null) {
                throw new NullPointerException("At least one point is null");
            }
        }
        
        
        // check if any point is repeated - if so throw IllegalArgumentException
        if (containsDuplicates(points)) {
            throw new IllegalArgumentException("Points array contains at least one repeated point");
        }
        
        n = points.length;
        pts = new Point[n];
        System.arraycopy(points, 0, pts, 0, points.length);
        
        sortedPts = new Point[n];
        System.arraycopy(pts, 0, sortedPts, 0, sortedPts.length);
        
        segs = new ArrayList<LineSegment>();
        
        starts = new ArrayList<Point>();
        ends = new ArrayList<Point>();
        
        findLineSegments();
        
    }
    
    // the number of line segments
    public int numberOfSegments() {
        return segs.size();
    }
    
    // the line segments
    public LineSegment[] segments() {
        LineSegment[] ls = segs.toArray(new LineSegment[segs.size()]);
        
        return Arrays.copyOf(ls, ls.length);
    }
    
    // Private method to check for duplicate points
    private boolean containsDuplicates(Point[] x) {
        
        for (int j = 0; j < x.length; j++) {
            for (int k = 0; k < x.length; k++) {
                if (k != j && x[j].equals(x[k])) {
                    return true;
                }
            }
        }      
        return false;
    }
    
    // The actual method to find the ine segments
    private void findLineSegments() {
        
        for (int i = 0; i < n; i++) {
            Point p = pts[i];
            
            Arrays.sort(sortedPts,  p.slopeOrder());
            
            ArrayList<Point> colinearPts = new ArrayList<Point>();
            
            double slope = 0;
            double previousSlope = Double.NEGATIVE_INFINITY;    
            
            for (int j = 1; j < n; j++) {
                Point q = sortedPts[j];
                    
                    slope = p.slopeTo(q);
                      
                    if (slope == previousSlope) {
                        colinearPts.add(q);
                    }
                    
                    else {
                        if (colinearPts.size() >= 3) {
                            colinearPts.add(p);
                            makeColinearPts(colinearPts);
                        }
                        colinearPts.clear();
                        colinearPts.add(q);
                    }
                    previousSlope = slope;
            }
            if (colinearPts.size() >= 3) {
                colinearPts.add(p);
                makeColinearPts(colinearPts);
            }
        }
        
    }
    
    private void makeColinearPts(ArrayList<Point> colinearPts) {
        
        Collections.sort(colinearPts);
        Point a = colinearPts.get(0);
        Point b = colinearPts.get(colinearPts.size() - 1);
        LineSegment lineS = new LineSegment(a, b);
        
        if (!starts.contains(a) && !ends.contains(b)) {
            starts.add(a);
            ends.add(b);
            segs.add(lineS);
            
        }
        /*else if (starts.indexOf(a) != ends.indexOf(b)) {
            starts.add(a);
            ends.add(b);
            segs.add(lineS);
            
        }
        Collections.sort(starts);
        Collections.sort(ends);*/
    }
    
   

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
    
}
