package com.alaindroid.gameoftheninja.modules;

public class DaggerInjectorModule {
    public static MainComponent get() {
        return DaggerMainComponent.builder().build();
    }
}
