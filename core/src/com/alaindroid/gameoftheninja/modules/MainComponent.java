package com.alaindroid.gameoftheninja.modules;

import com.alaindroid.gameoftheninja.ColoniserGame;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = { GeneratorModule.class, StateModule.class, DrawModule.class,
        ServiceModule.class})
public interface MainComponent {
    void inject(ColoniserGame mainGame);
}
