package com.alaindroid.gameoftheninja.util;

import java.util.stream.IntStream;

public class MathUtil {
    public static int max(int... numbers) {
        return IntStream.of(numbers)
                .max()
                .orElse(0);
    }
}
