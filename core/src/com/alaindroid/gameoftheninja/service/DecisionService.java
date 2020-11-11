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
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class DecisionService {
    private final NavigationService navigationService;
    private final PathFinderService pathFinderService;
    private final GridGeneratorService gridGeneratorService;
    private final FightResolverService fightResolverService;

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
        popPossibles(selected, gameSave.grid(), gameSave.units());
        decisionState = DecisionState.DECISION;
        return selected;
    }

    public boolean decide(Player player, Unit unit, Grid grid, Coordinate nextCoordinate, List<Unit> allUnits) {
        Set<Coordinate> navigable = navigationService.navigable(unit, grid, allUnits);
        if (!navigable.contains(nextCoordinate)) {
            reset();
            return false;
        }
        Coordinate[] nextCoords = pathFinderService.findPath(unit, nextCoordinate, grid, allUnits).toArray(new Coordinate[0]);
        if (nextCoords.length <= 0) {
            System.err.println("Couldn't find path");
            decisionState = DecisionState.SELECTION;
            return false;
        }
        Optional<Unit> unitInNextCoord = allUnits.stream()
                .filter(u -> u.coordinate().equals(nextCoordinate))
                .findFirst();
        unit.moving(true);
        unit.setNextDestination(nextCoords);
        if (unitInNextCoord.isPresent()) {
            unit.onArrival(Optional.of(() -> {
                FightResolverService.FightResolution resolution = fightResolverService.findLoser(unit, unitInNextCoord.get());
                resolution.getDefeated().forEach(defeated -> {
                    defeated.moving();
                    defeated.setNextDestination(grid.hell());
                    defeated.spin();
                });

            }));
        }
        decisionState = DecisionState.SELECTION;
        return true;
    }

    private void popPossibles(Unit unit, Grid grid, List<Unit> unitList) {
        navigationService.navigable(unit, grid, unitList)
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
