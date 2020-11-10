package com.alaindroid.gameoftheninja.animation;

import com.alaindroid.gameoftheninja.draw.Point2D;

public class SlideDestinationAnimation extends DestinationAnimation{
    private float moveScale;
    public SlideDestinationAnimation(Plottable plottable, Point2D destination,
                                     float scale, OnArrivalCallback callback) {
        super(plottable, destination, callback);
        this.moveScale = moveScale;
    }

    @Override
    public Point2D nextPoint(float deltaTime) {
        double adj = Math.atan2(deltaY(), deltaX());
        float deltaX = (float) Math.cos(adj) * deltaTime * moveScale;
        float deltaY = (float) Math.sin(adj) * deltaTime * moveScale;
        float newX = currentPoint().x() + deltaX;
        float newY = currentPoint().y() + deltaY;
        return new Point2D(newX, newY);
    }
}
