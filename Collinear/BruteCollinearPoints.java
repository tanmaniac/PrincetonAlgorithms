/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;

public class BruteCollinearPoints {
    private Point[] points;
    private ArrayList<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.IllegalArgumentException(
                    "Input to BruteCollinearPoints constructor was null!");
        }

        this.points = points;

        for (int i = 0; i < this.points.length; i++) {
            if (this.points[i] == null) {
                throw new java.lang.IllegalArgumentException("One of the input points was null!");
            }
            for (int j = i - 1; j >= 0; j--) {
                if (this.points[i].compareTo(this.points[j]) == 0) {
                    throw new java.lang.IllegalArgumentException(
                            "A repeated point was passed to BruteCollinearPoints!");
                }
            }
        }

        findSegments();
    }

    private void findSegments() {
        segments = new ArrayList<LineSegment>();
        // Ew
        for (int p = 0; p < points.length; p++) {
            for (int q = p + 1; q < points.length; q++) {
                for (int r = q + 1; r < points.length; r++) {
                    for (int s = r + 1; s < points.length; s++) {
                        double pqSlope = points[p].slopeTo(points[q]);
                        if (pqSlope == points[p].slopeTo(points[r]) && pqSlope == points[p]
                                .slopeTo(points[s])) {
                            // Find minimum and maximum
                            Point[] segment = new Point[] {
                                    points[p], points[q], points[r], points[s]
                            };
                            Point minimum = segment[0];
                            Point maximum = segment[0];
                            for (Point point : segment) {
                                if (point.compareTo(minimum) < 0) {
                                    minimum = point;
                                }
                                else if (point.compareTo(maximum) > 0) {
                                    maximum = point;
                                }
                            }

                            // System.out.println(
                            //         "Found segment with points " + segment[0].toString() + ", "
                            //                 + segment[1].toString() + ", " + segment[2].toString()
                            //                 + ", " + segment[3].toString());
                            segments.add(new LineSegment(minimum, maximum));
                        }
                    }
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

}
