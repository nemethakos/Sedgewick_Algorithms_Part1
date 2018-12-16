/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KdTree {

    private Node root;
    private int size;

    public static void main(String[] args) {

        KdTree tree = new KdTree();

        tree.insert(new Point2D(0.9, 0.6));
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        tree.draw();
        System.out.println(tree.size());
        System.out.println(tree.contains(new Point2D(0, 0)));
        System.out.println(tree.contains(new Point2D(0.9, 0.6)));

    }

    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("argument should be non null");
        }
        Node node = root;
        while (node != null) {
            if (node.xAxis) {
                // compare by X axis
                int compareResult = compareX(point, node.point);
                if (compareResult > 0) {
                    node = node.right;
                }
                else if (compareResult <= 0) {
                    if (point.equals(node.point)) {
                        return true;
                    }
                    node = node.left;
                }
            }
            else {
                // compare by Y axis
                int compareResult = compareY(point, node.point);
                if (compareResult > 0) {
                    node = node.right;
                }
                else if (compareResult <= 0) {
                    if (point.equals(node.point)) {
                        return true;
                    }
                    node = node.left;
                }
            }
        } // end of while
        return false;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    /**
     * Returns +1 if point1.x > point2.x Returns 0 if point1.x == point2.x Returns -1 if point1.x <
     * point2.x
     *
     * @param point1
     * @param point2
     * @return
     */
    private int compareX(Point2D point1, Point2D point2) {
        return Double.compare(point1.x(), point2.x());
    }

    private int compareY(Point2D point1, Point2D point2) {
        return Double.compare(point1.y(), point2.y());
    }

    /**
     * Search and insert. The algorithms for search and insert are similar to those for BSTs, but at
     * the root we use the x-coordinate (if the point to be inserted has a smaller x-coordinate than
     * the point at the root, go left; otherwise go right); then at the next level, we use the
     * y-coordinate (if the point to be inserted has a smaller y-coordinate than the point in the
     * node, go left; otherwise go right); then at the next level the x-coordinate, and so forth.
     *
     * @param point
     */
    public void insert(Point2D point) {

        if (point == null) {
            throw new IllegalArgumentException("argument should be non null");
        }


        if (root == null) {
            // no nodes in tree yet
            root = new Node(point, true, new RectHV(0, 0, 1, 1));
        }
        else {
            // there are nodes in tree
            Node node = root;
            Node found = null;
            Node parent = null;
            boolean left = false;
            while (node != null) {
                if (node.xAxis) {
                    // compare by X axis
                    int compareResult = compareX(point, node.point);
                    if (compareResult > 0) {
                        parent = node;
                        node = node.right;
                        left = false;
                    }
                    else if (compareResult < 0) {
                        parent = node;
                        node = node.left;
                        left = true;
                    }
                    else {
                        if (point.equals(node.point)) {
                            return;
                        }
                        else {
                            parent = node;
                            node = node.left;
                            left = true;
                        }
                    }

                }
                else {
                    // compare by Y axis
                    int compareResult = compareY(point, node.point);
                    if (compareResult > 0) {
                        parent = node;
                        node = node.right;
                        left = false;
                    }
                    else if (compareResult < 0) {
                        parent = node;
                        node = node.left;
                        left = true;
                    }
                    else {
                        if (point.equals(node.point)) {
                            return;
                        }
                        else {
                            parent = node;
                            node = node.left;
                            left = true;
                        }
                    }

                }
            } // end of while
            // not found
            RectHV bounds = null;
            if (parent.xAxis) {
                // parent is x axis, node is y axis
                if (left) {
                    bounds = new RectHV(parent.bounds.xmin(), parent.bounds.ymin(),
                                        parent.point.x(), parent.bounds.ymax());
                }
                else {
                    bounds = new RectHV(parent.point.x(), parent.bounds.ymin(),
                                        parent.bounds.xmax(), parent.bounds.ymax());
                }
                // insert as vertical
                node = new Node(point, false, bounds);
            }
            else {
                // parent is y axis, node is x axis
                if (left) {
                    bounds = new RectHV(parent.bounds.xmin(), parent.bounds.ymin(),
                                        parent.bounds.xmax(), parent.point.y());
                }
                else {
                    bounds = new RectHV(parent.bounds.xmin(), parent.point.y(),
                                        parent.bounds.xmax(), parent.bounds.ymax());

                }
                // insert as horizontal
                node = new Node(point, true, bounds);
            }
            if (left) {
                parent.left = node;
            }
            else {
                parent.right = node;
            }
        }
        size++;
    }

    /**
     * Grader output: do not compute the distance between the query point and the point in a node if
     * the closest point discovered so far is closer than the distance between the query point and
     * the rectangle corresponding to the node
     * <p>
     * Nearest-neighbor search. To find a closest point to a given query point, start at the root
     * and recursively search in both subtrees using the following pruning rule: if the closest
     * point discovered so far is closer than the distance between the query point and the rectangle
     * corresponding to a node, there is no need to explore that node (or its subtrees). That is,
     * search a node only only if it might contain a point that is closer than the best one found so
     * far. The effectiveness of the pruning rule depends on quickly finding a nearby point. To do
     * this, organize the recursive method so that when there are two possible subtrees to go down,
     * you always choose the subtree that is on the same side of the splitting line as the query
     * point as the first subtree to explore—the closest point found while exploring the first
     * subtree may enable pruning of the second subtree.
     *
     * @param query
     * @return
     */
    public Point2D nearest(Point2D query) {
        if (query == null) {
            throw new IllegalArgumentException("argument should be non null");
        }
        if (root == null) {
            return null;
        }
        return nearest(query, root, root, root.point.distanceSquaredTo(query)).nearest.point;
    }

    private SearchResult nearest(Point2D query, Node node, Node nearest,
                                 double nearestDistanceSquared) {
        if (node == null) {
            return null;
        }

        double distance = node.point.distanceSquaredTo(query);
        if (nearestDistanceSquared > distance) {
            nearestDistanceSquared = distance;
            nearest = node;
        }

        double leftDistance = Double.POSITIVE_INFINITY;
        if (node.left != null) {
            leftDistance = node.left.bounds.distanceSquaredTo(query);
        }
        double rightDistance = Double.POSITIVE_INFINITY;
        if (node.right != null) {
            rightDistance = node.right.bounds.distanceSquaredTo(query);
        }

        ArrayList<Node> searchNodes = new ArrayList<>();


        if (nearestDistanceSquared > leftDistance) {
            // explore left
            searchNodes.add(node.left);
        }

        if (nearestDistanceSquared > rightDistance) {
            // explore right
            searchNodes.add(node.right);
        }

        if (node.right != null && node.right.bounds.contains(query) && searchNodes.size() > 1) {
            Collections.swap(searchNodes, 0, 1);
        }

        for (Node searchNode : searchNodes) {
            SearchResult searchResult = nearest(query, searchNode, nearest, nearestDistanceSquared);
            if (searchResult.nearestDistanceSquared < nearestDistanceSquared) {
                nearest = searchResult.nearest;
                nearestDistanceSquared = searchResult.nearestDistanceSquared;
            }
        }

        return new SearchResult(nearest, nearestDistanceSquared);
    }

    /**
     * Range search. To find all points contained in a given query rectangle, start at the root and
     * recursively search for points in both subtrees using the following pruning rule: if the query
     * rectangle does not intersect the rectangle corresponding to a node, there is no need to
     * explore that node (or its subtrees). A subtree is searched only if it might contain a point
     * contained in the query rectangle.
     *
     * @param rect the search {@link RectHV}
     * @return the {@link Iterable} of found {@link Point2D}s
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("argument should be non null");
        }
        ArrayList<Point2D> found = new ArrayList<>();
        range(rect, root, found);
        return found;
    }

    private void range(RectHV rect, Node node, List<Point2D> found) {
        if (node == null) {
            return;
        }
        if (!node.bounds.intersects(rect)) {
            return;
        }
        if (rect.contains(node.point)) {
            found.add(node.point);
        }
        range(rect, node.left, found);
        range(rect, node.right, found);
    }

    /**
     * Draw. A 2d-tree divides the unit square in a simple way: all the points to the left of the
     * root go in the left subtree; all those to the right go in the right subtree; and so forth,
     * recursively. Your draw() method should draw all of the points to standard draw in black and
     * the subdivisions in red (for vertical splits) and blue (for horizontal splits). This method
     * need not be efficient—it is primarily for debugging.
     */
    public void draw() {
        StdDraw.setPenRadius(0.001);
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) {
            return;
        }
        // draw left tree
        draw(node.left);
        // draw node
        if (node.xAxis) {
            StdDraw.setPenColor(Color.RED);
            StdDraw.line(node.point.x(), node.bounds.ymin(), node.point.x(), node.bounds.ymax());
        }
        else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(node.bounds.xmin(), node.point.y(), node.bounds.xmax(), node.point.y());
        }
        StdDraw.setPenColor(Color.BLACK);
        StdDraw.filledCircle(node.point.x(), node.point.y(), 0.002);
        // draw right tree
        draw(node.right);
    }

    private static class Node {
        private Point2D point;
        private boolean xAxis;
        private Node left;
        private Node right;
        private RectHV bounds;

        public Node(Point2D point, boolean xAxis, RectHV bounds) {
            this.point = point;
            this.xAxis = xAxis;
            this.bounds = bounds;
        }
    }

    private static class SearchResult {
        Node nearest;
        double nearestDistanceSquared;

        public SearchResult(Node nearest, double nearestDistanceSquared) {
            this.nearest = nearest;
            this.nearestDistanceSquared = nearestDistanceSquared;
        }
    }
}
