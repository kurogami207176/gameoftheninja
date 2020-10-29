package com.alaindroid.gameoftheninja.modules;

import com.alaindroid.gameoftheninja.draw.*;
import com.alaindroid.gameoftheninja.service.animation.AnimationProcessorService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class DrawModule {

    @Provides
    @Singleton
    public HexGridDrawer hexDrawer() {
        return new HexGridDrawer();
    }

    @Provides
    @Singleton
    public UnitDrawer unitDrawer() {
        return new UnitDrawer();
    }

    @Provides
    @Singleton
    public BuildingDrawer buildingDrawer() {
        return new BuildingDrawer();
    }

    @Provides
    @Singleton
    public SpriteDrawer spriteDrawer(HexGridDrawer hexDrawer, UnitDrawer unitDrawer, BuildingDrawer buildingDrawer) {
        return new SpriteDrawer(hexDrawer, unitDrawer, buildingDrawer);
    }

    @Provides
    @Singleton
    public BackgroundDrawer backgroundDrawer() {
        return new BackgroundDrawer();
    }

    @Provides
    @Singleton
    public AnimationProcessorService animationProcessorService() {
        return new AnimationProcessorService();
    }
}
