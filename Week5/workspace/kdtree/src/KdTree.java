import java.util.ArrayList;

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
public class KdTree {
    
    private Node root;
    private int size;
    
    
    
    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean horizontal;
        
        
        
        public Node(Point2D p, RectHV rect, boolean horizontal) {
            this.p = p;
            this.rect = rect;
            this.horizontal = horizontal;
        }
     }
    
    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }
    
    // is the set empty?
    public boolean isEmpty(){
        return root == null;
    }
    
    // number of points in the set 
    public int size() {
        int res = size;
        return res;
    }
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new NullPointerException("null Point2D p to insert()");
        
        root = insert(root, null, p);
    }
    
    // private helper method
    private Node insert(Node x, Node parent, Point2D p) {
        if (x == null) {
            size++;
            
            if (parent == null) {
                return new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0), false);
            }
            
            return new Node(p, getRect(p, parent, !parent.horizontal), !parent.horizontal);
        }
        
        
        int cmp = compare(p, x.p, x.horizontal);
        
        if (cmp < 0)        x.lb = insert(x.lb, x, p);
        else if (cmp > 0)   x.rt = insert(x.rt, x, p);
        else                x.p = p;
        return x;
    }
    
    // helper method to compare L/R and B/T
    private int compare(Point2D p, Point2D q, boolean horizontal) {
        
        
        if (!horizontal) {
            if (p.x() < q.x()) return -1;
            if (p.x() > q.x()) return 1;
            if (p.y() < q.y()) return -1;
            if (p.y() > q.y()) return 1;
        }
        
        else {
            if (p.y() < q.y()) return -1;
            if (p.y() > q.y()) return 1;
            if (p.x() < q.x()) return -1;
            if (p.x() > q.x()) return 1;
        }
        return 0;
    }
    
    // get axis-aligned RectHV rectangle
    private RectHV getRect(Point2D p, Node parent, boolean horizontal) {
        
        int cmp = compare(p, parent.p, parent.horizontal);
        
        RectHV rangle = parent.rect;
        
        if (horizontal) {
            if (cmp < 0) {
                return new RectHV(rangle.xmin(), rangle.ymin(), parent.p.x(), rangle.ymax());
            }
            else if (cmp > 0) {
                return new RectHV(parent.p.x(), rangle.ymin(), rangle.xmax(), rangle.ymax());
            }
        }
        else {
            if (cmp < 0) {
                return new RectHV(rangle.xmin(), rangle.ymin(), rangle.xmax(), parent.p.y());
            }
            else if (cmp > 0) {
                return new RectHV(rangle.xmin(), parent.p.y(), rangle.xmax(), rangle.ymax());
            }
        }
        return null;
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("null Point2D p to contains()");
        
        return contains(root, p, false);
    }
    
    // private helper method for contains
    private boolean contains(Node x, Point2D p, boolean horizontal) {
        if (x == null) return false;
        
        if (x.p.equals(p)) return true;
        
        int cmp = compare(p, x.p, horizontal);
        
        if (cmp < 0)        return contains(x.lb, p, !x.horizontal);
        else if (cmp > 0)   return contains(x.rt, p, !x.horizontal);
        else                return true;
    }
    
    // draw all points to standard draw
    public void draw() {
        draw(root, false);
    }
    
    // private helper method to draw
    private void draw(Node x, boolean horizontal) {
        
        StdDraw.setPenRadius(0.001);
        // draw the first square in black
        // StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.square(0.5, 0.5, 0.5);
        
        if (!horizontal) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.p.x(), x.rect.ymin(), x.p.x(), x.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(x.rect.xmin(), x.p.y(), x.rect.xmax(), x.p.y());
        }
        
        
        // draw dot
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.005);
        x.p.draw();
        // draw subTree
        if (x.lb != null) draw(x.lb, !x.horizontal);
        if (x.rt != null) draw(x.rt, !x.horizontal);
        
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("null RectHV rect to range()");
        
        ArrayList<Point2D> rangeList = new ArrayList<Point2D>();
        range(root, rect, rangeList);
        ArrayList<Point2D> res = rangeList;
        return res;
    }
    
    private void range(Node x, RectHV rect, ArrayList<Point2D> rangeList) {
        if (x == null) return;
        if (rect.contains(x.p)) rangeList.add(x.p);
        if (x.lb != null && x.lb.rect.intersects(rect)) range(x.lb, rect, rangeList);
        if (x.rt != null && x.rt.rect.intersects(rect)) range(x.rt, rect, rangeList);
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("null Point2D p to nearest()");
        
        if (isEmpty()) return null;
        
        Point2D nearest = nearest(root, p, root.p, false);
        return nearest;
        
    }
    
    // private method for nearest
    private Point2D nearest(Node x, Point2D p, Point2D q, boolean horizontal) {
        Point2D champ = q;
        
        if (x == null) return champ;
        
        if (p.distanceSquaredTo(x.p) < p.distanceSquaredTo(champ)) {
            champ = x.p;
        }
        
        int cmp = compare(p, x.p, horizontal);
        
        if (cmp < 0) {
            champ = nearest(x.lb, p, champ, !x.horizontal);
            if (x.rt !=null && p.distanceSquaredTo(champ)
                    > x.rt.rect.distanceSquaredTo(p)) {
                champ = nearest(x.rt, p, champ, !x.horizontal);
            }
        }
        else if (cmp > 0) {
            champ = nearest(x.rt, p, champ, !x.horizontal);
            if (x.lb !=null && p.distanceSquaredTo(champ)
                    > x.lb.rect.distanceSquaredTo(p)) {
                champ = nearest(x.lb, p, champ, !x.horizontal);
            }
        }
        
        return champ;
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
