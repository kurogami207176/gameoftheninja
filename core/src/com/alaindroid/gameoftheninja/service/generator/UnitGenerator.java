package com.alaindroid.gameoftheninja.service.generator;

import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.service.NavigationService;
import com.alaindroid.gameoftheninja.state.Player;
import com.alaindroid.gameoftheninja.units.Unit;
import com.alaindroid.gameoftheninja.units.UnitType;
import com.alaindroid.gameoftheninja.util.CoordinateUtil;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class UnitGenerator {

    private final NavigationService navigationService;

    public List<Unit> generateUnitsForPlayers(Collection<Player> players, Map<Player, Set<Coordinate>> settlementRange,
                                              int cnt, Grid grid) {
        List<Unit> units = players.stream()
                .map(player -> generate(player, cnt))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("Generated units: " + units.size());
        List<Unit> removable = new ArrayList<>();
        for(Unit unit: units) {
            randomCoordinate(unit, grid, settlementRange.get(unit.player()), units);
            if (unit.coordinate() == null) {
                removable.add(unit);
                continue;
            }
        }
        units.removeAll(removable);
        System.out.println("Filtered units: " + units.size());
        units.forEach(unit -> {
            System.out.println(unit);
        });
        return units;
    }

    public List<Unit> generate(Player player, int  cnt) {
        List<Unit> gens =  IntStream.range(0, cnt)
                        .mapToObj(i -> UnitType.random())
                        .map(unitType -> new Unit(unitType))
                        .collect(Collectors.toList());
        for(Unit unit: gens) {
            unit.player(player);
        }
        return gens;
    }

    private void randomCoordinate(Unit unit, Grid grid, Set<Coordinate> settlementRange, List<Unit> unitsSoFar) {
        settlementRange.stream()
                .filter(CoordinateUtil.navigable(unit, grid))
                .filter(coordinate -> unitsSoFar.stream().map(Unit::coordinate).filter(Objects::nonNull).noneMatch(coordinate::equals))
                .findAny()
                .ifPresent(c -> unit.setNextDestination(c) );
    }


}
