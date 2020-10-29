package com.alaindroid.gameoftheninja.service.grid;

import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.grid.HexCell;
import com.alaindroid.gameoftheninja.grid.TileType;
import com.alaindroid.gameoftheninja.util.RandomUtil;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class CellGeneratorService {
    private final LandTileWeightService landTileWeightService;

    public HexCell generate(Coordinate coordinate, Grid grid) {
        Map<Coordinate, HexCell> hexCellMap = grid.cells();
        Map<TileType, Long> neighborTiles = coordinate
                .generateNeighbors()
                .stream()
                .map(hexCellMap::get)
                .filter(Objects::nonNull)
                .map(HexCell::tileType)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                ;
        return new HexCell(weightedType(coordinate, grid.minRGB(), grid.maxRGB(), neighborTiles));
    }

    private Map<TileType, Integer> createWeighted(TreeMap<Integer, TileType> fullWeightedMap,
                                                      Coordinate coordinate,
                                                      Coordinate minRGB,
                                                      Coordinate maxRGB,
                                                      Map<TileType, Long> neighborTiles) {
        float polesDistance = maxRGB.b() - minRGB.b();
        float equatorToPoleDistance = polesDistance / 2;
        float equator = (maxRGB.b() - minRGB.b()) / 2;
        float distanceFromClosestPole = Math.min(Math.abs(maxRGB.b() - coordinate.b()), Math.abs(coordinate.b() - minRGB.b()));
        float distanceFromEquator = Math.abs(equatorToPoleDistance - coordinate.b());
        LandTileWeightService.TileWeightBundle.TileWeightBundleBuilder builderBundle = LandTileWeightService.TileWeightBundle.builder()
                .fullWeightedMap(fullWeightedMap)
                .coordinate(coordinate)
                .minRGB(minRGB)
                .maxRGB(maxRGB)
                .neighborTiles(neighborTiles)
                .polesDistance(polesDistance)
                .equator(equator)
                .equatorToPoleDistance(equatorToPoleDistance)
                .distanceFromClosestPole(distanceFromClosestPole)
                .distanceFromEquator(distanceFromEquator);
        Map<TileType, Integer> baseWeights = Stream.of(TileType.DIRT, TileType.GOLD, TileType.GRASS, TileType.LAVA,
                TileType.ROCK, TileType.SAND, TileType.SNOW, TileType.STONE, TileType.WATER)
                .collect(Collectors.toMap(
                        Function.identity(),
                        t -> landTileWeightService.getWeight(builderBundle.tileType(t).build()))
                );
        baseWeights = baseWeights.entrySet().stream()
                .filter(kv -> kv.getValue() > 0)
                .collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
        return baseWeights;
    }

    private TileType weightedType(Coordinate coordinate,
                                  Coordinate minRGB,
                                  Coordinate maxRGB,
                                  Map<TileType, Long> neighborTiles) {
        Map<TileType, Integer> baseWeights = createWeighted( new TreeMap<>(),
                coordinate,
                minRGB,
                maxRGB,
                neighborTiles);
        return RandomUtil.weightedRandom(baseWeights);
    }
}
