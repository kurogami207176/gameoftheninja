package com.alaindroid.gameoftheninja.service;

import com.alaindroid.gameoftheninja.bldg.Settlement;
import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.grid.HexCell;
import com.alaindroid.gameoftheninja.grid.TileType;
import com.alaindroid.gameoftheninja.state.GameSave;
import com.alaindroid.gameoftheninja.state.Player;
import com.alaindroid.gameoftheninja.units.Unit;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PlayerViewFilterService {
    private final NavigationService navigationService;

    public GameSave filterGameSave(GameSave fullGameSave) {
        Player player = fullGameSave.currentPlayer();
        Set<Coordinate> playerVisibleCoords = fullGameSave.units().stream()
                .filter(u -> u.player().equals(player))
                .map(u -> navigationService.visible(u.coordinate(), fullGameSave.grid(), u.unitType().range()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        return new GameSave(
                filterGrid(player, playerVisibleCoords, fullGameSave.grid()),
                filterUnits(player, playerVisibleCoords, fullGameSave.units()),
                filterSettlement(player, playerVisibleCoords, fullGameSave.settlements()),
                fullGameSave.players()
        );
    }

    public Grid filterGrid(Player player, Set<Coordinate> playerVisibleCoords, Grid fullGrid) {
        Grid grid = new Grid(fullGrid.minRGB(), fullGrid.maxRGB());

        playerVisibleCoords.stream()
                .map(c -> navigationService.visible(c, fullGrid, 1))
                .flatMap(Set::stream)
                .forEach(c -> {
                    HexCell hexCell = new HexCell(TileType.UNKNOWN);
                    HexCell exCell = fullGrid.cells().get(c);
                    if (exCell != null) {
                        hexCell.currentPopHeight(exCell.currentPopHeight());
                    }
                    grid.cells().put(c, hexCell);
                });
        // TODO: Seen but currently hidden... fog of war?
        fullGrid.cells().keySet()
                .stream()
                .filter(c -> player.seenCoordinates().contains(c) || playerVisibleCoords.contains(c))
                .forEach(c -> grid.cells().put(c, fullGrid.cell(c)));
        return grid;
    }

    public List<Unit> filterUnits(Player player, Set<Coordinate> playerVisibleCoords, List<Unit> allUnits) {
        List<Unit> visibleUnits = new ArrayList<>();
        visibleUnits.addAll(allUnits.stream()
                .filter(u -> u.player().equals(player) || playerVisibleCoords.contains(u.coordinate()))
                .collect(Collectors.toList()));
        visibleUnits.addAll(player.seenUnit().stream().map(Player.UnitMemory::generateUnit).collect(Collectors.toList()));
        return visibleUnits;
    }

    public List<Settlement> filterSettlement(Player player, Set<Coordinate> playerVisibleCoords, List<Settlement> allUnits) {
        List<Settlement> visibleUnits = new ArrayList<>();
        visibleUnits.addAll(allUnits.stream()
                .filter(u -> u.player().equals(player) || playerVisibleCoords.contains(u.coordinate()))
                .collect(Collectors.toList()));
//        visibleUnits.addAll(player.seenUnit().stream().map(Player.UnitMemory::generateUnit).collect(Collectors.toList()));
        return visibleUnits;
    }
}
