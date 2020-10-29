package com.alaindroid.gameoftheninja.units;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum UnitType {
    LAND_ATTACK(0, 5,1),
    LAND_SPEED( 0, 1, 2),
    LAND_LARGE(0, 3, 1),
    SHIP_ATTACK(1, 5, 2),
    SHIP_SPEED(1, 1, 3),
    SHIP_LARGE(3, 1, 2),
    LAND_UNKNOWN(0, 0,0),
    SHIP_UNKNOWN(0, 0, 0)
    ;

    private int capacity;
    private int strength;
    private int range;
}
