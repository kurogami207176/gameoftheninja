package com.alaindroid.gameoftheninja.util;

import com.alaindroid.gameoftheninja.draw.Point2D;
import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.units.Unit;
import com.badlogic.gdx.math.Vector3;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class CoordinateUtil {

    public static List<Point2D> toPoint(double r, double g, double b, float s) {
        double y = 2.65d/2 * s * b;
        double x = Math.sqrt(2.91d) * s * ( b/2 + r);
        double x2 = - Math.sqrt(2.91d) * s * ( b/2 + g );

        return Arrays.asList(
                new Point2D((float) x, (float) y),
                new Point2D( (float) x2, (float) y)
        );
    }

    public static float euclideanDistance(Coordinate a, Coordinate b) {
        return new Vector3(a.r(), a.g(), a.b()).dst(new Vector3(b.r(), b.g(), b.b()));
    }

    public static int distance(Coordinate a, Coordinate b) {
        return IntStream.of(Math.abs(a.r() - b.r()),
                Math.abs(a.b() - b.b()),
                Math.abs(a.g() - b.g()))
                .max()
                .getAsInt();
    }

    public static Predicate<Coordinate> navigable(Unit unit, Grid grid) {
        return coordinate -> grid.cell(coordinate) != null;
    }



}
