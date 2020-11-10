package com.alaindroid.gameoftheninja.service.animation;

import com.alaindroid.gameoftheninja.draw.Point2D;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.grid.HexCell;
import com.alaindroid.gameoftheninja.state.GameSave;
import com.alaindroid.gameoftheninja.units.Unit;

import java.util.List;

public class AnimationProcessorService {
    private static final float popReductionSpeed = 200f;
    private static final float popHeight = 10f;

    private static final float wobbleSpeed = 250f;
    private static final float maxWobbleAngle = 15f;

    private static final float unitMoveSpeed = 200f;

    public void processAnimation(GameSave gameSave, float deltaTime) {
        processAnimation(gameSave.grid(), deltaTime);
        processAnimation(gameSave.units(), deltaTime);
    }

    private void processAnimation(Grid grid, float deltaTime) {
        grid.cells().values().forEach(h -> processAnimation(h, deltaTime));
    }

    private void processAnimation(HexCell hexCell, float deltaTime) {
        float nextPopHeight = hexCell.currentPopHeight();
        if (hexCell.popped() && hexCell.currentPopHeight() < popHeight) {
            nextPopHeight = Math.min(popHeight, hexCell.currentPopHeight() + popReductionSpeed * deltaTime);

        }
        else if (!hexCell.popped()) {
            if (hexCell.currentPopHeight() > 0) {
                nextPopHeight = Math.max(0, hexCell.currentPopHeight() - popReductionSpeed * deltaTime);
            }
            else if (hexCell.currentPopHeight() < 0) {
                nextPopHeight = Math.min(0, hexCell.currentPopHeight() + popReductionSpeed * deltaTime);
            }
        }
        hexCell.currentPopHeight(nextPopHeight);
    }

    private void processAnimation(List<Unit> unitList, float deltaTime) {
        unitList.forEach(unit -> processAnimation(unit, deltaTime));
    }

    private void processAnimation(Unit unit, float deltaTime) {
        processWobble(unit, deltaTime);
        processUnitMove(unit, deltaTime);
    }

    private void processUnitMove(Unit unit, float deltaTime) {
        Point2D current = unit.currentPoint();
        if (unit.targetPoints() == null || unit.targetPoints().isEmpty()) {
            return;
        }
        Point2D target = unit.targetPoints().get(0);
        float dy = target.y() - current.y();
        float dx = target.x() - current.x();
        if (Math.abs(dy) < 5f && Math.abs(dx) < 5f) {
            unit.currentPoint(target);
            unit.targetPoints().remove(0);
            if (unit.targetPoints().isEmpty()) {
                unit.moving(false);
            }
            return;
        }
        else {
            double adj = Math.atan2(dy, dx);
            float deltaX = (float) Math.cos(adj) * deltaTime * unitMoveSpeed;
            float deltaY = (float) Math.sin(adj) * deltaTime * unitMoveSpeed;
            float newX = current.x() + deltaX;
            float newY = current.y() + deltaY;
            unit.currentPoint(new Point2D(newX, newY));
        }

    }

    private void processWobble(Unit unit, float deltaTime) {
        float newWobbleAngle;
        if (unit.wobble() && Math.abs(unit.currentAngle()) < maxWobbleAngle) {
            if (unit.currentWobbleDirectionLeft()) {
                newWobbleAngle = Math.max(-maxWobbleAngle, unit.currentAngle() - deltaTime*wobbleSpeed);
            }
            else {
                newWobbleAngle = Math.min(maxWobbleAngle, unit.currentAngle() + deltaTime*wobbleSpeed);
            }
        }
        else {
            newWobbleAngle = 0;
        }
        if (Math.abs(newWobbleAngle) == maxWobbleAngle) {
            unit.currentWobbleDirectionLeft(!unit.currentWobbleDirectionLeft());
        }
        unit.currentAngle(newWobbleAngle);
    }
}
