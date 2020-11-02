package com.alaindroid.gameoftheninja.service;

import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.service.generator.GridGeneratorService;
import com.alaindroid.gameoftheninja.state.GameSave;
import com.alaindroid.gameoftheninja.state.Player;
import com.alaindroid.gameoftheninja.units.Unit;
import com.alaindroid.gameoftheninja.util.Constants;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class DecisionService {
    private final NavigationService navigationService;
    private final PathFinderService pathFinderService;
    private final GridGeneratorService gridGeneratorService;

    private DecisionState decisionState = DecisionState.SELECTION;

    public void reset() {
        decisionState = DecisionState.SELECTION;
    }

    public Unit select(GameSave gameSave, Unit selected) {
        Player player = gameSave.currentPlayer();
        if (!player.equals(selected.player())) {
            decisionState = DecisionState.SELECTION;
            return selected;
        }
        selected.wobble(true);
        popPossibles(selected, gameSave.grid());
        decisionState = DecisionState.DECISION;
        return selected;
    }

    public boolean decide(Player player, Unit unit, Grid grid, Coordinate nextCoordinate, List<Unit> allUnits) {
        Set<Coordinate> navigable = navigationService.navigable(unit, grid);
        if (!navigable.contains(nextCoordinate)) {
            reset();
            return false;
        }
        Coordinate[] nextCoords = pathFinderService.findPath(unit, nextCoordinate, grid).toArray(new Coordinate[0]);
        if (nextCoords.length > 0) {
            unit.moving(true);
            unit.setNextDestination(nextCoords);
            decisionState = DecisionState.SELECTION;
            return true;
        } else {
            System.err.println("Couldn't find path");
            decisionState = DecisionState.SELECTION;
            return false;
        }
    }

    private void popPossibles(Unit unit, Grid grid) {
        navigationService.navigable(unit, grid)
                .stream()
                .map(grid::cell)
                .forEach(h -> h.popped(true));
    }

    public boolean isWaitingForSelection() {
        return decisionState == DecisionState.SELECTION;
    }
    public boolean isWaitingForDecision() {
        return decisionState == DecisionState.DECISION;
    }

    enum DecisionState {
        SELECTION, DECISION
    }
}
