package com.alaindroid.gameoftheninja.modules;

import com.alaindroid.gameoftheninja.service.*;
import com.alaindroid.gameoftheninja.service.generator.GridGeneratorService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ServiceModule {
    @Provides
    @Singleton
    public NavigationService navigationService() {
        return new NavigationService();
    }
    @Provides
    @Singleton
    public FightResolverService fightResolverService() {
        return new FightResolverService();
    }

    @Provides
    @Singleton
    public DecisionService decisionService(NavigationService navigationService, PathFinderService pathFinderService,
                                           GridGeneratorService gridGeneratorService, FightResolverService fightResolverService) {
        return new DecisionService(navigationService, pathFinderService, gridGeneratorService, fightResolverService);
    }

    @Provides
    @Singleton
    public PathFinderService pathFinderService(NavigationService navigationService) {
        return new PathFinderService(navigationService);
    }

    @Provides
    @Singleton
    public GamespeedService gamespeedService() {
        return new GamespeedService();
    }

}
