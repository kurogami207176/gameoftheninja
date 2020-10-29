package com.alaindroid.gameoftheninja.service.grid;

import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.TileType;
import lombok.Builder;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.alaindroid.gameoftheninja.util.Constants.WATER_PECENTAGE;

public class LandTileWeightService {

    public static final float SNOW_ZONE = 0.07f;
    private static final int TOTAL_LAND_WEIGHT = 10000;
    private static final Map<TileType, Float> LAND_PROBABILITIES = new HashMap<>();
    static {
        LAND_PROBABILITIES.put(TileType.LAVA, 0.01f);  //  1
        LAND_PROBABILITIES.put(TileType.GOLD, 0.02f);  //  4
        LAND_PROBABILITIES.put(TileType.STONE, 0.01f); //  5
        LAND_PROBABILITIES.put(TileType.DIRT, 0.2f);   // 20
        LAND_PROBABILITIES.put(TileType.GRASS, 0.7f);  // 60
        LAND_PROBABILITIES.put(TileType.SAND, 0.01f);  // 10
    }

    public int getWeight(TileWeightBundle bundle) {

        switch (bundle.tileType) {

            case DIRT:
            case GOLD:
            case GRASS:
            case LAVA:
            case ROCK:
            case SAND:
            case STONE:
                return nonSnowLandWeight(bundle);
            case SNOW:
                return snowWeight(bundle);
            case WATER:
                return waterWeight(bundle);
        }
        return 0;
    }

    private int snowWeight(TileWeightBundle b)  {
        float snowProbability = getSnowProbability(b);
        return (int) (TOTAL_LAND_WEIGHT * snowProbability);
    }

    private int waterWeight(TileWeightBundle b) {
        return (int)((float)TOTAL_LAND_WEIGHT * WATER_PECENTAGE) ;
    }

    private int nonSnowLandWeight(TileWeightBundle b) {
        float nonSnowLandProbability = 1f - getSnowProbability(b);
        return (int) (TOTAL_LAND_WEIGHT * nonSnowLandProbability * LAND_PROBABILITIES.computeIfAbsent(b.tileType, w->0f));
    }

    private float getSnowProbability(TileWeightBundle b) {
        float snowProbability = ((b.distanceFromClosestPole / b.equatorToPoleDistance) / -SNOW_ZONE) + 1;
        return Math.max(0, Math.min(1, snowProbability));
    }

    @ToString
    @Builder
    @Accessors(fluent = true)
    public static class TileWeightBundle {
        private TileType tileType;
        private TreeMap<Integer, TileType> fullWeightedMap;
        private Coordinate coordinate;
        private Coordinate minRGB;
        private Coordinate maxRGB;
        private Map<TileType, Long> neighborTiles;
        private float polesDistance;
        private float equator;
        private float equatorToPoleDistance;
        private float distanceFromClosestPole;
        private float distanceFromEquator;
    }
}
