package com.alaindroid.gameoftheninja.inputs;

import com.alaindroid.gameoftheninja.draw.Point2D;
import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.HexCell;
import com.alaindroid.gameoftheninja.service.DecisionService;
import com.alaindroid.gameoftheninja.service.NavigationService;
import com.alaindroid.gameoftheninja.state.GameSave;
import com.alaindroid.gameoftheninja.units.Unit;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class GameControllerListener implements GestureDetector.GestureListener {
    private final OrthographicCamera camera;
    private final GameSave gameSave;
    private final DecisionService decisionService;
    private final NavigationService navigationService;

    private int tileCount = 0;
    private float minX = 0;
    private float minY = 0;
    private float maxX = 0;
    private float maxY = 0;
    private float xOffset = 0;
    private float yOffset = 0;
    private Function<Float, Float> boundScale = f -> f;

    private float maxZoom = 4f;
    private float minZoom = 0.4f;

    public GameControllerListener(OrthographicCamera camera, GameSave gameSave,
                                   DecisionService decisionService, NavigationService navigationService) {
        this.camera = camera;
        this.gameSave = gameSave;
        this.decisionService = decisionService;
        this.navigationService = navigationService;
        setBounds(gameSave);
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        handleXY(x, y);
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        setBounds(gameSave);
        xOffset = xOffset + deltaX;
        yOffset = yOffset + deltaY;
        if (xOffset > maxX) {
            float deltaDelta = xOffset - maxX;
            deltaX = deltaX - deltaDelta;
            xOffset = xOffset - deltaDelta;
        }
        else if (xOffset < minX){
            float deltaDelta = Math.abs(xOffset - minX);
            deltaX = deltaX + deltaDelta;
            xOffset = xOffset + deltaDelta;
        }
        if (yOffset > maxY) {
            float deltaDelta = yOffset - maxY;
            deltaY = deltaY - deltaDelta;
            yOffset = yOffset - deltaDelta;
        }
        else if (yOffset < minY) {
            float deltaDelta = Math.abs(yOffset - minY);
            deltaY = deltaY + deltaDelta;
            yOffset = yOffset + deltaDelta;
        }

        camera.translate(-deltaX, deltaY);
        camera.update();

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        System.out.println("initialDistance: " + initialDistance);
        System.out.println("distance: " + distance);
        System.out.println("zoom: " + camera.zoom);
        float diff = distance - initialDistance;
        float zoomLevel = camera.zoom - diff * 0.0005f;
        camera.zoom = Math.min(maxZoom, Math.max(minZoom, zoomLevel));
        camera.update();
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

    private void setBounds(GameSave gameSave) {
        Set<Coordinate> coordinateSet = gameSave.grid().cells().keySet();
        if(coordinateSet.size() != tileCount) {
            List<Point2D> points = coordinateSet
                    .stream()
                    .map(Coordinate::point)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            Supplier thrower = () -> new RuntimeException("Failures!");
            minX = points.stream().map(Point2D::x).min(Float::compareTo).map(boundScale).orElseThrow(thrower);
            minY = points.stream().map(Point2D::y).min(Float::compareTo).map(boundScale).orElseThrow(thrower);
            maxX = points.stream().map(Point2D::x).max(Float::compareTo).map(boundScale).orElseThrow(thrower);
            maxY = points.stream().map(Point2D::y).max(Float::compareTo).map(boundScale).orElseThrow(thrower);
            tileCount = gameSave.grid().cells().keySet().size();
        }
    }

    private boolean handleXY(float screenX, float screenY) {
        Optional<Coordinate> coordinateOptional = findCoordinates(screenX, screenY);
        if(!coordinateOptional.isPresent()) {
            gameSave.reset();
            return true;
        }
        Coordinate coordinate = coordinateOptional.get();
        System.out.println("Clicked on hex: "
                + Optional.ofNullable(gameSave.grid().cell(coordinate)).map(HexCell::tileType).orElse(null)
                + " at " + coordinate);
        if (decisionService.isWaitingForSelection()) {
            Optional<Unit> unitOptional = findUnit(coordinate);
            if(!unitOptional.isPresent()) {
                gameSave.reset();
                return true;
            }
            decisionService.select(gameSave, unitOptional.get());
        } else if (decisionService.isWaitingForDecision()) {
            Unit wobblingUnit = gameSave.findWobblingUnit();
            boolean decided = decisionService.decide(gameSave.currentPlayer(), wobblingUnit, gameSave.grid(), coordinate, gameSave.units());
            if (decided) {
                gameSave.nextPlayer();
            }
            gameSave.postDecisionReset();
        }

        return false;
    }

    private Optional<Coordinate> findCoordinates(float screenX, float screenY) {
        Vector3 gameWorldVector = camera.unproject(new Vector3(screenX, screenY, 0));
        return gameSave.grid().cells().keySet().stream()
                .parallel()
                .map(c -> new Distance(c, gameWorldVector))
                .sorted(Comparator.comparing(Distance::distance))
                .findFirst()
                .map(Distance::coordinate);
    }

    private Optional<Unit> findUnit(Coordinate coordinate) {
        return gameSave.units().stream().filter(u -> u.coordinate().equals(coordinate)).findFirst();
    }

    @Data
    @Accessors(fluent = true)
    private static class Distance {
        private final Coordinate coordinate;
        private final float distance;
        public Distance(Coordinate coordinate, Vector3 vector) {
            this.coordinate = coordinate;
            Point2D point2D = coordinate.point().get(0);
            this.distance = vector.dst(new Vector3(point2D.x(), point2D.y(), vector.z));
        }
    }

}
