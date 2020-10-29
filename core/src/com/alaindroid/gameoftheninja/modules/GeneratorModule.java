package com.alaindroid.gameoftheninja.modules;

import com.alaindroid.gameoftheninja.service.NavigationService;
import com.alaindroid.gameoftheninja.service.generator.BuildingGeneratorService;
import com.alaindroid.gameoftheninja.service.generator.GridGeneratorService;
import com.alaindroid.gameoftheninja.service.generator.UnitGenerator;
import com.alaindroid.gameoftheninja.service.grid.CellGeneratorService;
import com.alaindroid.gameoftheninja.service.grid.LandTileWeightService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class GeneratorModule {

    @Provides
    @Singleton
    public GridGeneratorService gridGeneratorService(CellGeneratorService cellGeneratorService) {
        return new GridGeneratorService(cellGeneratorService);
    }

    @Provides
    @Singleton
    public UnitGenerator unitGenerator(NavigationService navigationService) {
        return new UnitGenerator(navigationService);
    }

    @Provides
    @Singleton
    public BuildingGeneratorService buildingGeneratorService(NavigationService navigationService,
                                                             GridGeneratorService gridGeneratorService) {
        return new BuildingGeneratorService(gridGeneratorService, navigationService);
    }

    @Provides
    @Singleton
    public CellGeneratorService cellGeneratorService(LandTileWeightService landTileWeightService) {
        return new CellGeneratorService(landTileWeightService);
    }
    @Provides
    @Singleton
    public LandTileWeightService tileWeightService() {
        return new LandTileWeightService();
    }
}
