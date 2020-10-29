package com.alaindroid.gameoftheninja.util;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class RandomUtil {
    private static Random random = new Random();

    public static int nextInt() {
        return random.nextInt();
    }

    public static int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public static <T> T weightedRandom(Map<T, Integer> baseWeights) {

        TreeMap<Integer, T> fullWeightedMap = new TreeMap<>();
        int totalWeight = 0;
        for(T tileType: baseWeights.keySet()) {
            totalWeight = totalWeight + baseWeights.get(tileType);
            fullWeightedMap.put(totalWeight, tileType);
        }

        int randWeight = RandomUtil.nextInt(totalWeight);
        return fullWeightedMap.get(fullWeightedMap.ceilingKey(randWeight));
    }

}
