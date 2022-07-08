import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
// import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
// import java.util.Map;

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
    private LineSegment[] ls;
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
        
        pts = points;
        n = pts.length;
        // Sort the array according to Point compareTo()
        // Arrays.sort(pts);
        
    }
    
    // the number of line segments
    public int numberOfSegments() {
        return ls.length;
    }
    
    // the line segments
    public LineSegment[] segments() {
        List<LineSegment> lst = new ArrayList<LineSegment>();
        
        
        for (int p = 0; p < n; p++) {
            // Sort pts according to slope order with respect to point p
            Arrays.sort(pts, pts[p].slopeOrder());
            // Make new list for colinear points
            List<Point> colinearPts = new ArrayList<Point>();
            innerloop:
            for (int q = 1; q < n - 1; q++) {
                
                    if (colinearPts.isEmpty()) { colinearPts.add(pts[q]); }
                    
                    if (pts[p].slopeTo(pts[q]) == pts[p].slopeTo(pts[q + 1])) {
                        colinearPts.add(pts[q + 1]);
                    }
                    
                    if (pts[p].slopeTo(pts[q]) != pts[p].slopeTo(pts[q + 1])) {
                        break innerloop;
                    }
                    
                    if (colinearPts.size() >= 3) {
                        colinearPts.add(pts[p]);
                        Collections.sort(colinearPts);
                        Point a = colinearPts.get(0);
                        Point b = colinearPts.get(colinearPts.size() - 1);
                        LineSegment lineS = new LineSegment(a, b);
                        lst.add(lineS);
                    }
            }
            
            
        }
           
        ls = lst.toArray(new LineSegment[lst.size()]);
        return ls;
    }
    
    // Private method to check for duplicate points using a Set
    private boolean containsDuplicates(Point[] x) {
        return new HashSet<Point>(Arrays.asList(x)).size() != x.length;
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
