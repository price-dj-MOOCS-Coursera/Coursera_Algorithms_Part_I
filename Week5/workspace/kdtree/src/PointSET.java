import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

/**
 * 
 */

/**
 * @author David Price
 *
 */
public class PointSET {
    private Set<Point2D> pointSet;
    
    
    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
        
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("null Point2D p to insert()");
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("null Point2D p to contains()");
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        if (isEmpty()) return;
        
        for (Point2D p : pointSet) {
            StdDraw.point(p.x(), p.y());
        }
    }

    // all points that are inside the rectangle 
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("null RectHV rect to range()");
        Set<Point2D> rangeSet = new TreeSet<Point2D>();
        
        if (isEmpty()) return rangeSet;
        
        for (Point2D p : pointSet) {
            if (rect.contains(p)) {
                rangeSet.add(p);
            }
        }
        return rangeSet;
    }

    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("null Point2D p to nearest()");
        
        double nearestDist = Double.POSITIVE_INFINITY;
        Point2D nearest = null;;
        
        if (isEmpty()) return nearest;
        
        for (Point2D px : pointSet) {
            double dist = p.distanceSquaredTo(px);
            if (dist < nearestDist) {
                nearestDist = dist;
                nearest = px;
            }
            
        }
        Point2D res = nearest;
        return res;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
