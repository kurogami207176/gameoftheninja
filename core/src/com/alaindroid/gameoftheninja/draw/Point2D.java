package com.alaindroid.gameoftheninja.draw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@AllArgsConstructor
public class Point2D {
    private float x, y;
}
