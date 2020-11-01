package com.alaindroid.gameoftheninja;

import com.alaindroid.gameoftheninja.modules.DaggerInjectorModule;
import com.alaindroid.gameoftheninja.state.MainGameState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import javax.inject.Inject;

public class GameOfTheNinjaGame extends ApplicationAdapter {

	@Inject
	MainGameState mainGameState;

	@Override
	public void create () {
		DaggerInjectorModule.get().inject(this);

		mainGameState.onCreate();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mainGameState.onRender(Gdx.graphics.getDeltaTime());

	}
	
	@Override
	public void dispose () {
		mainGameState.onDispose();
	}
}
