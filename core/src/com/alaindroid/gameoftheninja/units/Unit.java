package com.alaindroid.gameoftheninja.units;

import com.alaindroid.gameoftheninja.draw.Point2D;
import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.state.Player;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ToString
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Unit {
    @EqualsAndHashCode.Include
    private final String id = UUID.randomUUID().toString();
    private final UnitType unitType;
    private Coordinate coordinate;
    private float maxHealth = 100;
    private float currHealth = 100;

    @Setter
    private float currentWobbleAngle = 0;
    @Setter
    private boolean currentWobbleDirectionLeft = false;
    @Setter
    private Point2D currentPoint;
    @Setter
    private List<Point2D> targetPoints;
    @Setter
    private Player player;

    @Setter
    private boolean wobble = false;
    @Setter
    private boolean moving = false;

    public void setNextDestination(Coordinate... nextCoordinate) {
        if (coordinate != null) {
            this.currentPoint = this.coordinate.point().get(0);
        }
        else {
            this.coordinate = nextCoordinate[nextCoordinate.length - 1];
        }
        this.targetPoints = Stream.of(nextCoordinate)
                .map(Coordinate::point)
                .map(p -> p.get(0))
                .collect(Collectors.toList());
        this.coordinate = nextCoordinate[nextCoordinate.length - 1];
    }

    public Point2D currentPoint() {
        return currentPoint == null
                ? coordinate.point().get(0)
                : currentPoint;
    }
}
