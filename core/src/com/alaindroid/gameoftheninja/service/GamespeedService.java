package com.alaindroid.gameoftheninja.service;

public class GamespeedService {
    private float gameCycle = 0.5f;
    private float ticker = 0f;

    public boolean tick(float delta) {
        ticker = ticker + delta;
        if (ticker < gameCycle) {
            return false;
        }
        ticker = ticker - gameCycle;
        return true;
    }

    public float tickerPercent() {
        return ticker / gameCycle;
    }
}
