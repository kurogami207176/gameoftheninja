package com.alaindroid.gameoftheninja.modules;

import com.alaindroid.gameoftheninja.draw.BackgroundDrawer;
import com.alaindroid.gameoftheninja.draw.SpriteDrawer;
import com.alaindroid.gameoftheninja.service.DecisionService;
import com.alaindroid.gameoftheninja.service.GamespeedService;
import com.alaindroid.gameoftheninja.service.NavigationService;
import com.alaindroid.gameoftheninja.service.PlayerViewFilterService;
import com.alaindroid.gameoftheninja.service.animation.AnimationProcessorService;
import com.alaindroid.gameoftheninja.service.generator.BuildingGeneratorService;
import com.alaindroid.gameoftheninja.service.generator.GridGeneratorService;
import com.alaindroid.gameoftheninja.service.generator.UnitGenerator;
import com.alaindroid.gameoftheninja.state.MainGameState;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class StateModule {

    @Provides
    @Singleton
    public MainGameState mainGameState(SpriteDrawer spriteDrawer,
                                       BackgroundDrawer backgroundDrawer,
                                       GridGeneratorService gridGeneratorService,
                                       UnitGenerator unitGenerator,
                                       BuildingGeneratorService buildingGeneratorService,
                                       NavigationService navigationService,
                                       DecisionService decisionService,
                                       GamespeedService gamespeedService,
                                       AnimationProcessorService animationProcessorService,
                                       PlayerViewFilterService playerViewFilterService) {
        return new MainGameState(spriteDrawer, backgroundDrawer, gridGeneratorService, unitGenerator, buildingGeneratorService,
                navigationService, decisionService, gamespeedService, animationProcessorService, playerViewFilterService);
    }
}
