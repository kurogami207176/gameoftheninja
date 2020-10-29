package com.alaindroid.gameoftheninja.grid;

import com.alaindroid.gameoftheninja.draw.Point2D;
import com.alaindroid.gameoftheninja.util.Constants;
import com.alaindroid.gameoftheninja.util.CoordinateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class Coordinate {
    private int r, g, b;

    @EqualsAndHashCode.Exclude
    private List<Point2D> point;

    public Coordinate(int r, int g, int b, float s) {
        this.r = r;
        this.g = g;
        this.b = b;
        point = CoordinateUtil.toPoint(r,g, b, s);
    }

    public Set<Coordinate> generateNeighbors() {
        float s = Constants.HEX_SIDE_LENGTH;
        Set<Coordinate> coordinates = new HashSet<>();
        coordinates.add(coordOffset(1, 0, -1, s));
        coordinates.add(coordOffset( 1, -1, 0, s));
        coordinates.add(coordOffset( 0, 1, -1, s));
        coordinates.add(coordOffset( -1, 1, 0, s));
        coordinates.add(coordOffset( 0, -1, 1, s));
        coordinates.add(coordOffset( -1, 0, 1, s));
        return coordinates;
    }

    private Coordinate coordOffset(int r, int g, int b, float s) {
        return new Coordinate(this.r() + r,
                this.g() + g,
                this.b() + b,
                s);
    }

}
