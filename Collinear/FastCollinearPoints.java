/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FastCollinearPoints {
    static private final boolean debug = false;
    static private final int minLineLen = 3;   // 4 - 1 for the starting point itself
    private ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException(
                    "Input to BruteCollinearPoints constructor was null!");
        }

        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new java.lang.IllegalArgumentException("One of the input points was null!");
            }
            for (int j = i - 1; j >= 0; j--) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new java.lang.IllegalArgumentException(
                            "A repeated point was passed to BruteCollinearPoints!");
                }
            }
        }

        if (points.length <= minLineLen) {
            segments = new ArrayList<LineSegment>();
        } else {
            findCollinear(points);
        }
    }

    private boolean shouldAddLine(Point source, List<Point> pointSet) {
        if (debug) StdOut.println("    inspecting set " + pointSet.toString());

        // Takes a list of points with equal slope and checks if the source point is the smallest
        Point smallest = pointSet.get(0);
        for (int i = 1; i < pointSet.size(); i++) {
            if (pointSet.get(i).compareTo(smallest) < 0) {
                smallest = pointSet.get(i);
            }
        }

        return source.compareTo(smallest) <= 0;
    }

    private int argmax(List<Point> pointSet) {
        Point max = pointSet.get(0);
        int maxIdx = 0;
        for (int i = 1; i < pointSet.size(); i++) {
            if (pointSet.get(i).compareTo(max) > 0) {
                max = pointSet.get(i);
                maxIdx = i;
            }
        }
        return maxIdx;
    }

    private void findCollinear(Point[] points) {
        segments = new ArrayList<LineSegment>();

        // Go to the last minus the minimum line length
        for (int p = 0; p < points.length; p++) {
            // Create list of following points
            ArrayList<Point> pointsList = new ArrayList<Point>();
            for (int q = 0; q < points.length; q++) {
                if (q != p) {
                    pointsList.add(points[q]);
                }
            }

            // Sort the list by slope, with p as the calling point
            Collections.sort(pointsList, points[p].slopeOrder());

            // Print sorted slopes
            if (debug)
                StdOut.println(
                        "source = " + points[p].toString() + "; Sorted list: " + pointsList
                                .toString());

            // Search through the sorted set
            double startSlope = points[p].slopeTo(pointsList.get(0));
            int startIdx = 0;
            int endIdx = 0;
            for (int idx = 0; idx < pointsList.size(); idx++) {
                double curSlope = points[p].slopeTo(pointsList.get(idx));
                if (startSlope == curSlope
                        && idx < pointsList.size() - 1) { // To make sure we capture the last point
                    endIdx++;
                }
                else {
                    // wtf
                    if (startSlope == curSlope && idx == pointsList.size() - 1) {
                        endIdx++;
                    }
                    if (debug)
                        StdOut.println("startIdx: " + startIdx + " endIdx: " + endIdx);
                    // Check if this is longer than the minimum required
                    if (endIdx - startIdx >= minLineLen && shouldAddLine(points[p], pointsList
                            .subList(startIdx, endIdx))) {
                        // Find the maximum
                        int maxIdx = argmax(pointsList.subList(startIdx, endIdx));
                        // We know the source point, p, is the minimum, so just add this line segment to the list of segments
                        segments.add(new LineSegment(points[p], pointsList.subList(startIdx, endIdx)
                                                                          .get(maxIdx)));
                        // Print
                        if (debug)
                            StdOut.println(
                                    "    Found line between " + points[p] + ", set " + pointsList
                                            .subList(startIdx, endIdx) + " (maxIdx = " + maxIdx
                                            + ")");
                    }
                    // Update the start and end points regardless of whether we added the line segment or not
                    startSlope = curSlope;
                    startIdx = idx;
                    endIdx = idx + 1;
                }
            }
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] segmentsArr = new LineSegment[segments.size()];
        segments.toArray(segmentsArr);

        return segmentsArr;
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
