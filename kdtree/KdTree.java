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

    /**
     * Dot radius for visualizing the tree
     */
    private static final double DOT_RADIUS = 0.002;

    /**
     * Line width for visualizing the tree
     */
    private static final double LINE_WIDTH = 0.001;

    /**
     * Root of the {@link KdTree}
     */
    private Node root;

    /**
     * Number of {@link Node}s in the {@link KdTree}
     */
    private int size;

    public static void main(String[] args) {
        //
    }

    /**
     * Returns true if the {@link KdTree} contains the <code>point</code>
     *
     * @param point the point to find
     * @return true if the {@link KdTree} contains the <code>point</code>
     */
    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("argument should be non null");
        }
        Node node = root;
        while (node != null) {
            int compareResult = compare(point, node.point, node.xAxis);
            if (compareResult > 0) {
                node = node.right;
            }
            else {
                if (point.equals(node.point)) {
                    return true;
                }
                node = node.left;
            }
        }
        return false;
    }

    /**
     * Compares two {@link Point2D}s by X or by Y coordinates
     *
     * @param point1     the first {@link Point2D}
     * @param point2     the second {@link Point2D}
     * @param compareByX if true, compare by X coordinate, if false compare by Y coordinate
     * @return -1 if the first is less, 0 if equal, 1 if greater than the second point
     */
    private int compare(Point2D point1, Point2D point2, boolean compareByX) {
        if (compareByX) {
            return Double.compare(point1.x(), point2.x());
        }
        else {
            return Double.compare(point1.y(), point2.y());
        }

    }

    /**
     * Returns true if the {@link KdTree} is empty
     *
     * @return true if the {@link KdTree} is empty
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the number of {@link Point2D}s in the {@link KdTree}
     *
     * @return the number of {@link Point2D}s in the {@link KdTree}
     */
    public int size() {
        return size;
    }

    /**
     * Search and insert. The algorithms for search and insert are similar to those for BSTs, but at
     * the root we use the x-coordinate (if the point to be inserted has a smaller x-coordinate than
     * the point at the root, go left; otherwise go right); then at the next level, we use the
     * y-coordinate (if the point to be inserted has a smaller y-coordinate than the point in the
     * node, go left; otherwise go right); then at the next level the x-coordinate, and so forth.
     *
     * @param point the point to insert
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
            Node parent = null;
            boolean left = false;
            while (node != null) {
                int compareResult = compare(point, node.point, node.xAxis);
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
                        // the point to insert is already in the tree, do not add
                        return;
                    }
                    else {
                        parent = node;
                        node = node.left;
                        left = true;
                    }
                }

            } // end of while

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
     * @param query the query point
     * @return the nearest point to the query point or null if not found (only if the {@link KdTree}
     * is empty
     */
    public Point2D nearest(Point2D query) {
        if (query == null) {
            throw new IllegalArgumentException("argument should be non null");
        }
        if (root == null) {
            return null;
        }

        return nearest(query, root, null, Double.POSITIVE_INFINITY).nearest.point;
    }


    /**
     * Find the nearest point at <code>node</code>
     *
     * @param query                  the query {@link Point2D}
     * @param node                   the current {@link Node}
     * @param nearest                the current nearest {@link Node}
     * @param nearestDistanceSquared the current nearest distance squared
     * @return the {@link SearchResult} containing the nearest {@link Point2D} and the nearest
     * distance squared
     */
    private SearchResult nearest(Point2D query, Node node, Node nearest,
                                 double nearestDistanceSquared) {

        // calculate the distance from the query point to the current node
        double distance = node.point.distanceSquaredTo(query);
        // update the nearest point and distance if the current one is closer
        if (nearestDistanceSquared > distance) {
            nearestDistanceSquared = distance;
            nearest = node;
        }

        ArrayList<Node> searchNodes = new ArrayList<>();

        if (node.left != null) {
            searchNodes.add(node.left);
        }

        if (node.right != null) {
            searchNodes.add(node.right);
        }

        // "go down on the subtree which is on the same side as the query point"
        // the default search order is the left and the right subtree. When the right
        // subtree is on the same side of the node than the query point, switch
        // the search order to the right and left subtree.
        if (searchNodes.size() > 1 &&
                (node.xAxis && query.x() > node.point.x() ||
                        !node.xAxis && query.y() > node.point.y())) {

            Collections.swap(searchNodes, 0, 1);
        }

        for (Node searchNode : searchNodes) {

            // only check the subtree if the bounding rectangle is closer than the current nearest distance
            if (searchNode.bounds.distanceSquaredTo(query) < nearestDistanceSquared) {
                // get the distance to the searchNode
                SearchResult searchResult = nearest(query, searchNode, nearest,
                                                    nearestDistanceSquared);
                // if the new distance is closer than the previous one, update the nearest distance and
                // node
                if (searchResult.nearestDistanceSquared < nearestDistanceSquared) {
                    nearest = searchResult.nearest;
                    nearestDistanceSquared = searchResult.nearestDistanceSquared;
                }
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

    /**
     * Recursive range search
     *
     * @param rect  the search {@link RectHV}
     * @param node  the current {@link Node}
     * @param found list of found {@link Node}s
     */
    private void range(RectHV rect, Node node, List<Point2D> found) {
        if (node == null) {
            return;
        }
        // if the current node's bounds does not intersects with the search rectangle,
        // abandon searching the subtree
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
        StdDraw.setPenRadius(LINE_WIDTH);
        draw(root);
    }

    /**
     * Recursively draws the subtree
     *
     * @param node the node to draw
     */
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
        StdDraw.filledCircle(node.point.x(), node.point.y(), DOT_RADIUS);

        draw(node.right);
    }

    /**
     * Node for the {@link KdTree}
     */
    private static class Node {
        private final Point2D point;
        private final boolean xAxis;
        private final RectHV bounds;
        private Node left;
        private Node right;

        public Node(Point2D point, boolean xAxis, RectHV bounds) {
            this.point = point;
            this.xAxis = xAxis;
            this.bounds = bounds;
        }
    }

    /**
     * Contains the result of the nearest operation
     */
    private static class SearchResult {
        Node nearest;
        double nearestDistanceSquared;

        public SearchResult(Node nearest, double nearestDistanceSquared) {
            this.nearest = nearest;
            this.nearestDistanceSquared = nearestDistanceSquared;
        }
    }
}
