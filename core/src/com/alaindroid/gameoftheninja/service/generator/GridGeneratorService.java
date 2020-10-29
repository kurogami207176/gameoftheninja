package com.alaindroid.gameoftheninja.service.generator;

import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.grid.HexCell;
import com.alaindroid.gameoftheninja.service.grid.CellGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GridGeneratorService {
    private final CellGeneratorService cellGeneratorService;
    @SneakyThrows
    public Grid initGrid(int size, Coordinate minRGB, Coordinate maxRGB, float s) {
        Coordinate curr = new Coordinate(0,0,0, s);
        return initGrid(size, minRGB, maxRGB, s, curr);
    }

    @SneakyThrows
    public Grid initGrid(int size, Coordinate minRGB, Coordinate maxRGB, float s, Coordinate curr) {
        Grid grid = new Grid(minRGB, maxRGB);
        Set<Coordinate> coordinates = new HashSet<>();
        coordinates.add(curr);
        for(int i =0 ; i < size; i++) {
            coordinates.addAll(grow(coordinates, s));
        }
        coordinates.stream()
                .filter(grid::within)
                .forEach(c -> grid.cell(c, cellGeneratorService.generate(c, grid)));
//        gridPoint(grid, s);
        return grid;
    }

    public Grid growGrid(Grid currentGrid, Coordinate current, float s, int count) {
        Set<Coordinate> coordinates = new HashSet<>();
        coordinates.add(current);
        for(int i =0 ; i < count; i++) {
            coordinates.addAll(grow(coordinates, s));
        }
        coordinates.stream()
                .filter(currentGrid::within)
                .filter(c -> !currentGrid.cells().keySet().contains(c))
                .forEach(c -> {
                    HexCell hexCell = cellGeneratorService.generate(c, currentGrid);
                    hexCell.currentPopHeight(-200);
                    hexCell.popped(false);
                    currentGrid.cell(c, hexCell);
                });
        return currentGrid;
    }

    private Set<Coordinate> grow(Set<Coordinate> coordinates, float s) {
        return coordinates.stream()
                .map(Coordinate::generateNeighbors)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
