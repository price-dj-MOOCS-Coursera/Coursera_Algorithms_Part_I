import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
public class BruteCollinearPoints {
    private Point[] pts;
    private LineSegment[] ls;
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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
        // Sort the array according to Point compareTo()
        Arrays.sort(pts);
        
        
    }
    
    // the number of line segments
    public int numberOfSegments() {
        return ls.length;
    }

    // the line segments
    public LineSegment[] segments() {
        List<LineSegment> lst = new ArrayList<LineSegment>();
        
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double slopePQ = pts[i].slopeTo(pts[j]);
                for (int k = j + 1; k < pts.length; k++) {
                    double slopeQR = pts[j].slopeTo(pts[k]);
                    if (slopePQ == slopeQR) {
                        for (int l = k + 1; l < pts.length; l++) {
                            double slopeRS = pts[k].slopeTo(pts[l]);
                                if (slopeQR == slopeRS) {
                                    LineSegment ls1 = new LineSegment(pts[i], pts[l]);
                                    lst.add(ls1);
                                }
                        }
                    }
                }
            }
        }
        ls = lst.toArray(new LineSegment[lst.size()]);
        return ls;
    }
    
    
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }


}
