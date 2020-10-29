package com.alaindroid.gameoftheninja.service;

import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.units.Unit;
import com.alaindroid.gameoftheninja.util.CoordinateUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PathFinderService {

    private final NavigationService navigationService;

    public List<Coordinate> findPath(Unit unit, Coordinate end, Grid grid) {
        Coordinate current = unit.coordinate();
        List<Coordinate> pathTaken = new ArrayList<>();
        Set<Coordinate> blockSet = new HashSet<>();
        Set<Coordinate> considered = new HashSet<>();
        blockSet.add(current);
        while (!current.equals(end)) {
            Set<Coordinate> possibles = navigationService
                    .navigable(unit, current, grid, 1)
                    .stream()
                    .filter(c -> !pathTaken.contains(c))
                    .filter(c -> !blockSet.contains(c))
                    .collect(Collectors.toSet());
            Optional<Coordinate> coords = possibles.stream()
                    .map(n -> new CoordinateDistance(n, end))
                    .sorted(Comparator.comparing(CoordinateDistance::distance))
                    .findFirst()
                    .map(CoordinateDistance::coordinate);
            if (coords.isPresent()) {
                current = coords.get();
                pathTaken.add(current);
                possibles.remove(current);
                considered.addAll(possibles);
            }
            else {
                Coordinate blockThis = pathTaken.remove(pathTaken.size()-1);
                blockSet.add(blockThis);
                current = pathTaken.get(pathTaken.size() - 1);
            }
        }
        return pathTaken;
    }

    @Data
    @Accessors(fluent = true)
    private static class CoordinateDistance {
        private final Coordinate coordinate;
        private final float distance;
        public CoordinateDistance(Coordinate n, Coordinate end) {
            this.coordinate = n;
            this.distance = CoordinateUtil.distance(n, end); //new Vector3(n.r(), n.g(), n.b()).dst(new Vector3(end.r(), end.g(), end.b()));
        }
    }

}
