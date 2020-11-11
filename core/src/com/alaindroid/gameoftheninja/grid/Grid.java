package com.alaindroid.gameoftheninja.grid;

import com.alaindroid.gameoftheninja.draw.Point2D;
import com.alaindroid.gameoftheninja.util.Constants;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(fluent = true)
@RequiredArgsConstructor
public class Grid {
    private Map<Coordinate, HexCell> cells = new HashMap<>();
    private final Coordinate minRGB;
    private final Coordinate maxRGB;

    private Coordinate hell = new Coordinate(99, 99, 99, Constants.HEX_SIDE_LENGTH);

    public HexCell cell(Coordinate coordinate) {
        return cells.get(coordinate);
    }

    public void cell(Coordinate coordinate, HexCell cell) {
        cells.put(coordinate,cell);
    }

    public void unpopAll() {
        cells.values().forEach(h -> h.popped(false));
    }

    public boolean within(Coordinate coordinate) {
        return minRGB.r() <= coordinate.r() && coordinate.r() <= maxRGB.r() &&
                minRGB.g() <= coordinate.g() && coordinate.g() <= maxRGB.g() &&
                minRGB.b() <= coordinate.b() && coordinate.b() <= maxRGB.b();
    }

    public Point2D centerPoint() {
        int total = cells.keySet().size();
        return cells.keySet().stream()
                .map(Coordinate::point)
                .map(p -> p.get(0))
                .reduce((p1, p2) -> new Point2D(p1.x() + p2.x(), p1.y() + p2.y()))
                .map(p -> new Point2D(p.x() / total, p.y() / total))
                .orElse(new Point2D(0, 0));
    }

}
