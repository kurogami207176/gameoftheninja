package com.alaindroid.gameoftheninja.animation;

import com.alaindroid.gameoftheninja.draw.Point2D;

public abstract class DestinationAnimation implements CustomAnimation {
    private Plottable plottable;
    private Point2D destination;
    private OnArrivalCallback callback;

    public abstract Point2D nextPoint(float delta);

    public DestinationAnimation(Plottable plottable, Point2D destination, OnArrivalCallback callback) {
        this.destination = destination;
        this.callback = callback;
        this.plottable = plottable;
    }

    @Override
    public void tick(float delta) {
        Point2D nextPoint = nextPoint(delta);
        currentPoint(nextPoint);
        float dy = destination.y() - plottable.currentPoint().y();
        float dx = destination.x() - plottable.currentPoint().x();
        if (Math.abs(dy) < 5f && Math.abs(dx) < 5f) {
            plottable.currentPoint().x(destination.x());
            plottable.currentPoint().y(destination.y());
            callback.onArrival();
        }
    }

    protected Point2D currentPoint() {
        return plottable.currentPoint();
    }

    protected void currentPoint(Point2D nextPoint) {
        plottable.currentPoint(nextPoint);
    }

    protected float deltaX() {
        return destination.x() - plottable.currentPoint().x();
    }

    protected float deltaY() {
        return destination.y() - plottable.currentPoint().y();
    }

    interface OnArrivalCallback {
        void onArrival();
    }
}
