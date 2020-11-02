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
        List<Unit> gens =  new ArrayList<>();
        gens.addAll(unit(1, UnitType.FLAG));
        gens.addAll(unit(2, UnitType.SPY));
        gens.addAll(unit(6, UnitType.PRIV));
        gens.addAll(unit(1, UnitType.SGT));
        gens.addAll(unit(1, UnitType.LT1ST));
        gens.addAll(unit(1, UnitType.LT2ND));
        gens.addAll(unit(1, UnitType.CPT));
        gens.addAll(unit(1, UnitType.MAJ));
        gens.addAll(unit(1, UnitType.LTCOL));
        gens.addAll(unit(1, UnitType.COL));
        gens.addAll(unit(1, UnitType.GEN1));
        gens.addAll(unit(1, UnitType.GEN2));
        gens.addAll(unit(1, UnitType.GEN3));
        gens.addAll(unit(1, UnitType.GEN4));
        gens.addAll(unit(1, UnitType.GEN5));
        for(Unit unit: gens) {
            unit.player(player);
        }
        return gens;
    }

    private List<Unit> unit(int count, UnitType type) {
        return IntStream.range(0, count)
                .mapToObj(c -> new Unit(type))
                .collect(Collectors.toList());
    }

    private void randomCoordinate(Unit unit, Grid grid, Set<Coordinate> settlementRange, List<Unit> unitsSoFar) {
        settlementRange.stream()
                .filter(CoordinateUtil.navigable(unit, grid))
                .filter(coordinate -> unitsSoFar.stream().map(Unit::coordinate).filter(Objects::nonNull).noneMatch(coordinate::equals))
                .findAny()
                .ifPresent(c -> unit.setNextDestination(c) );
    }


}
