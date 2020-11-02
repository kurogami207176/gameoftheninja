package com.alaindroid.gameoftheninja.service;

import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.state.Player;
import com.alaindroid.gameoftheninja.units.Unit;
import com.alaindroid.gameoftheninja.util.CoordinateUtil;
import com.alaindroid.gameoftheninja.util.MathUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class NavigationService {
    public Set<Coordinate> navigable(Unit unit, Grid grid) {
        return navigable(unit, grid, unit.unitType().range());
    }

    public Set<Coordinate> navigable(Unit unit, Grid grid, int range) {
        return navigable(unit, unit.coordinate(), grid, range);
    }

    public Set<Coordinate> navigable(Unit unit, Coordinate coordinate, Grid grid, int range) {
        Set<Coordinate> navigable = new HashSet<>();
        navigable.add(coordinate);
        for (int i = 0; i < range; i++) {
            navigable.addAll(navigable.stream()
                    .map(Coordinate::generateNeighbors)
                    .flatMap(Set::stream)
                    .filter(CoordinateUtil.navigable(unit, grid))
                    .collect(Collectors.toSet())
            );
        }
        navigable.remove(coordinate);
        return navigable;
    }

    public Set<Coordinate> starter(Grid grid, int playerNumber) {
        Function<Coordinate, Integer> maxer;
        switch (playerNumber) {
            case 0:
                maxer = c -> c.r();
                break;
            case 1:
                maxer = c -> c.g();
                break;
            case 2:
                maxer = c -> c.b();
                break;
            case 3:
                maxer = c -> -c.r();
                break;
            case 4:
                maxer = c -> -c.g();
                break;
            case 5:
                maxer = c -> -c.b();
                break;
            default:
                throw new RuntimeException("Valid player number is 0-2");
        }
        Set<Coordinate> gridCoords = grid.cells().keySet();
        int maxNumber = gridCoords.stream()
                .map(maxer)
                .max(Integer::compareTo)
                .orElse(0);

        Set<Coordinate> navigable = new HashSet<>(
                gridCoords.stream()
                .filter(c -> maxer.apply(c) == maxNumber)
                .filter(c -> Math.abs(c.r()) >= 1 && Math.abs(c.g()) >= 1 && Math.abs(c.b()) >= 1)
                .collect(Collectors.toSet()));
        Set<Coordinate> newCoords;
        do {
            newCoords = navigable.stream()
                    .map(Coordinate::generateNeighbors)
                    .flatMap(Set::stream)
                    .filter(c -> gridCoords.contains(c))
                    .filter(c -> !navigable.contains(c))
                    .filter(c -> Math.abs(c.r()) >= 1 && Math.abs(c.g()) >= 1 && Math.abs(c.b()) >= 1)
                    .filter(c -> navigable.stream().filter(n -> n.isNeighbor(c)).count() > 1)
                    .collect(Collectors.toSet());
            navigable.addAll(newCoords);
        } while (newCoords != null && !newCoords.isEmpty());
        return navigable;
    }

}
