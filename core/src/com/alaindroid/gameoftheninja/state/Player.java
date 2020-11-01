package com.alaindroid.gameoftheninja.state;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Setter
@Getter
@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Player {
    @EqualsAndHashCode.Include
    private final String id = UUID.randomUUID().toString();
    private final Color color;

    private int turnLeft;
    private int maxTurns;

    enum Color {
        BLACK, BRONZE, GOLD, SILVER;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}
