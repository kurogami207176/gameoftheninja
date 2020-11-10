package com.alaindroid.gameoftheninja.animation;

import com.alaindroid.gameoftheninja.draw.Point2D;

public interface Plottable {
    Point2D currentPoint();
    Plottable currentPoint(Point2D currentPoint);
}
