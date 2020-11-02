package com.alaindroid.gameoftheninja.state;

import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.units.Unit;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Data
@Accessors(fluent = true)
public class GameSave {
    private final Grid grid;
    private final List<Unit> units;

    private final List<Player> players;

    private Player currentPlayer;
    private MainPlayStateEnum currentState = MainPlayStateEnum.PLACEMENT;

    public Unit findWobblingUnit() {
        return units.stream().filter(Unit::wobble).findFirst().orElse(null);
    }

    public void postDecisionReset() {
        grid.unpopAll();
        units.forEach(unit -> {
            unit.wobble(false);
        });
    }

    public void reset() {
        postDecisionReset();
    }

    public void nextPlayer() {
        int indexOf = players.indexOf(currentPlayer);
        int next = (indexOf + 1) % players.size();
        currentPlayer = players.get(next);
    }
}
