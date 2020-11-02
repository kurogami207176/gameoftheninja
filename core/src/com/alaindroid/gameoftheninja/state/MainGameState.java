package com.alaindroid.gameoftheninja.state;

import com.alaindroid.gameoftheninja.draw.BackgroundDrawer;
import com.alaindroid.gameoftheninja.draw.Point2D;
import com.alaindroid.gameoftheninja.draw.SpriteDrawer;
import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.inputs.GameControllerListener;
import com.alaindroid.gameoftheninja.service.DecisionService;
import com.alaindroid.gameoftheninja.service.GamespeedService;
import com.alaindroid.gameoftheninja.service.NavigationService;
import com.alaindroid.gameoftheninja.service.animation.AnimationProcessorService;
import com.alaindroid.gameoftheninja.service.generator.GridGeneratorService;
import com.alaindroid.gameoftheninja.service.generator.UnitGenerator;
import com.alaindroid.gameoftheninja.units.Unit;
import com.alaindroid.gameoftheninja.util.Constants;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class MainGameState implements GameState {
    final SpriteDrawer spriteDrawer;
    final BackgroundDrawer backgroundDrawer;
    final GridGeneratorService gridGeneratorService;
    final UnitGenerator unitGenerator;
    final NavigationService navigationService;
    final DecisionService decisionService;
    final GamespeedService gamespeedService;
    final AnimationProcessorService animationProcessorService;

    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;
    SpriteBatch bgSpriteBatch;
    SpriteBatch spriteBatch;
    GameSave gameSave;
    float width;
    float height;
    Player player1;
    Player player2;

    @Override
    public void onCreate() {
        shapeRenderer = new ShapeRenderer();
        spriteBatch = new SpriteBatch();
        bgSpriteBatch = new SpriteBatch();
        width = isAndroid()
                ? Gdx.graphics.getWidth() / Gdx.graphics.getDensity()
                : 1280;
        height = isAndroid()
                ? Gdx.graphics.getHeight() / Gdx.graphics.getDensity()
                : 960;
        camera = new OrthographicCamera(width, height);
        Coordinate minRGB = new Coordinate(-50, -50, -50, Constants.HEX_SIDE_LENGTH);
        Coordinate maxRGB = new Coordinate(50, 50, 50, Constants.HEX_SIDE_LENGTH);
        Grid grid = gridGeneratorService.initGrid(9, minRGB, maxRGB, Constants.HEX_SIDE_LENGTH);
        Point2D center = grid.centerPoint();
        camera.translate(center.x(), center.y());

        List<Player> players = new ArrayList<>();
        player1 = new Player(Player.Color.GOLD);
        player2 = new Player(Player.Color.SILVER);
        players.add(player1);
        players.add(player2);

        Map<Player, Set<Coordinate>> starterRange = new HashMap<>();
        starterRange.put(player1, navigationService.starter(grid, 0));
        starterRange.put(player2, navigationService.starter(grid, 1));
        List<Unit> units = unitGenerator.generateUnitsForPlayers(players, starterRange, 3, grid);

        gameSave = new GameSave(grid, units, players);
        gameSave.currentPlayer(player1);
        backgroundDrawer.create();
        spriteDrawer.create();

//        Gdx.input.setInputProcessor(new GameStateInputProcessor(camera, gameSave, decisionService, navigationService));
        Gdx.input.setInputProcessor(new GestureDetector(new GameControllerListener(camera, gameSave, decisionService, navigationService)));
    }

    @Override
    public void onRender(float deltaTime) {
        if (gamespeedService.tick(deltaTime)) {
            // TODO: Do something?
        }
        animationProcessorService.processAnimation(gameSave, deltaTime);
        bgSpriteBatch.begin();
        backgroundDrawer.draw(bgSpriteBatch, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bgSpriteBatch.end();

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        spriteDrawer.draw(spriteBatch, gameSave);
//        spriteDrawer.draw(spriteBatch, playerViewFilterService.filterGameSave(gameSave));

        spriteBatch.end();
    }

    @Override
    public void onDispose() {
        spriteDrawer.dispose();
        backgroundDrawer.dispose();
    }

    private boolean isAndroid() {
        return Gdx.app.getType().equals(Application.ApplicationType.Android);
    }

}
