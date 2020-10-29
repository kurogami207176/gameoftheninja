package com.alaindroid.gameoftheninja.state;

import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.units.LandUnit;
import com.alaindroid.gameoftheninja.units.Unit;
import com.alaindroid.gameoftheninja.units.UnitType;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Setter
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {
    @EqualsAndHashCode.Include
    private final String id = UUID.randomUUID().toString();
    private final Color color;

    private int turnLeft;
    private int maxTurns;

    private Set<Coordinate> seenCoordinates = new HashSet<>();
    private Set<UnitMemory> seenUnit = new HashSet<>();

    enum Color {
        GREEN, RED
    }

    @Getter
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @Accessors(fluent = true)
    public static class UnitMemory {
        private static int counter = 0;
        @EqualsAndHashCode.Include
        private final int id = ++counter;
        private final UnitType unitType;
        private final Coordinate coordinate;
        private final Player player;

        public UnitMemory(Unit unit) {
            this.unitType = unit.unitType();
            this.coordinate = unit.coordinate();
            this.player = unit.player();
        }
        public Unit generateUnit() {
            Unit unit;
            if (Arrays.asList(LandUnit.LAND_UNITS).contains(unitType)) {
                unit = new Unit(UnitType.LAND_UNKNOWN);
            } else {
                unit = new Unit(UnitType.SHIP_UNKNOWN);
            }
            unit.setNextDestination(this.coordinate);
            unit.player(this.player);
            return  unit;
        }
    }
}
