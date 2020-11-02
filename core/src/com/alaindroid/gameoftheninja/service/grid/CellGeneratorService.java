package com.alaindroid.gameoftheninja.service.grid;

import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.grid.HexCell;
import com.alaindroid.gameoftheninja.grid.TileType;
import com.alaindroid.gameoftheninja.service.NavigationService;
import com.alaindroid.gameoftheninja.util.RandomUtil;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CellGeneratorService {
    private final NavigationService navigationService;

    public void modifyHex(Grid grid) {
        Map<Integer, TileType> tileTypeMap = new HashMap<>();
        tileTypeMap.put(0, TileType.DIRT);
        tileTypeMap.put(1, TileType.GRASS);
        tileTypeMap.put(2, TileType.WATER);
        tileTypeMap.put(3, TileType.SAND);
        tileTypeMap.put(4, TileType.ROCK);
        tileTypeMap.put(5, TileType.SNOW);
        tileTypeMap.forEach((n, tileType) ->
            navigationService.starter(grid, n)
                    .stream()
                    .forEach( c -> grid.cells().put(c, new HexCell(tileType)) )
        );
    }
}
