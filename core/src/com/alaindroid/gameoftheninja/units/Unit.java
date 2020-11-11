package com.alaindroid.gameoftheninja.units;

import com.alaindroid.gameoftheninja.draw.Point2D;
import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.service.animation.RotationType;
import com.alaindroid.gameoftheninja.state.Player;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;
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
    @Getter
    private float currentAngle = 0;
    @Setter
    private boolean currentRotateDirectionLeft = false;
    @Setter
    private Point2D currentPoint;
    @Setter
    private List<Point2D> targetPoints;
    @Setter
    private Player player;

    @Setter
    private boolean moving = false;

    private RotationType rotationType;
    @Setter
    private Optional<Runnable> onArrival = Optional.empty();

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

    public boolean wobble() {
        return rotationType == RotationType.WOBBLE;
    }

    public Unit wobble(boolean wobble) {
        rotationType = wobble? RotationType.WOBBLE : RotationType.NONE;
        return this;
    }

    public boolean spin() {
        return rotationType == RotationType.WOBBLE;
    }

    public Unit spin(boolean spin) {
        rotationType = spin? RotationType.SPIN : RotationType.NONE;
        return this;
    }
}
