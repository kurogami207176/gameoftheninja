package com.alaindroid.gameoftheninja.grid;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@RequiredArgsConstructor
public class HexCell {
    private final TileType tileType;

    private boolean popped = false;
    private float currentPopHeight = 0;
}
