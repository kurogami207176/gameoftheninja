package com.alaindroid.gameoftheninja.bldg;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Stream;

@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
public enum SettlementType {
    TENT(1, 1),
    SMALL_HAMLET(2, 2),
    LARGE_HAMLET(3, 3),
    SMALL_TOWN(5, 4),
    LARGE_TOWN(8, 5),
    SMALL_CITY(13,6),
    LARGE_CITY (21,7),
    CASTLE (34,8);

    private static TreeSet<SettlementType> sorted = Stream.of(SettlementType.values())
            .sorted(Comparator.comparing(SettlementType::minAge))
            .collect(() -> new TreeSet<>(),
                    (a, b) -> a.add(b),
                    (b, c) -> b.addAll(c));
    private final int minAge;
    private final int range;

    public static SettlementType getType(int age) {
        return sorted.stream()
                .filter(t -> age >= t.minAge)
                .max(Comparator.comparing(SettlementType::minAge))
                .orElse(TENT);
    }
}
