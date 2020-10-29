package com.alaindroid.gameoftheninja.state;

public interface GameState {

    void onCreate ();

    void onRender(float deltaTime);

    void onDispose ();
}
