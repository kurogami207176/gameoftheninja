package com.alaindroid.gameoftheninja.service.generator;

import com.alaindroid.gameoftheninja.bldg.Settlement;
import com.alaindroid.gameoftheninja.bldg.SettlementType;
import com.alaindroid.gameoftheninja.grid.Coordinate;
import com.alaindroid.gameoftheninja.grid.Grid;
import com.alaindroid.gameoftheninja.service.NavigationService;
import com.alaindroid.gameoftheninja.state.Player;
import com.alaindroid.gameoftheninja.units.LandUnit;
import com.alaindroid.gameoftheninja.util.Constants;
import com.alaindroid.gameoftheninja.util.CoordinateUtil;
import com.alaindroid.gameoftheninja.util.RandomUtil;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class BuildingGeneratorService {

    private final GridGeneratorService gridGeneratorService;
    private final NavigationService navigationService;

    public List<Settlement> generateStart(Collection<Player> players, Grid grid) {
        List<Settlement> castles = generateTypeForPlayer(players, SettlementType.CASTLE, grid);
        Map<Player, Set<Coordinate>> settlementRange = findCastleRange(players, castles, grid);
        List<Settlement> accessoryBldgs = generateForPlayers(players, settlementRange, 2,grid);
        List<Settlement> settlements = new ArrayList<>();
        settlements.addAll(castles);
        settlements.addAll(accessoryBldgs);
        return settlements;
    }

    public List<Settlement> generateTypeForPlayer(Collection<Player> players, SettlementType type, Grid grid) {
        Set<Coordinate> coordinates = new HashSet<>();
        List<Settlement> settlements = players.stream()
                .map(player -> generate(player, randomCoordinates(grid, 1, coordinates, (int)(type.range() * 1.5f)), type))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("Generated Settlement Type: " + type);
        settlements.forEach(System.out::println);
        for(Settlement settlement: settlements) {
            gridGeneratorService.growGrid(grid, settlement.coordinate(), Constants.HEX_SIDE_LENGTH, type.range());
            Set<Coordinate> visible = navigationService.visible(settlement.coordinate(), grid, settlement.type().range());
            visible.forEach(c -> {
                settlement.player().seenCoordinates().add(c);
            });
        }
        return settlements;
    }

    public List<Settlement> generateForPlayers(Collection<Player> players, Map<Player, Set<Coordinate>> settlementRange,
                                               int towns, Grid grid) {
        Set<Coordinate> coordinates = new HashSet<>();
        List<Settlement> settlements = players.stream()
                .map(player -> generate(player, randomCoordinates(grid, towns, settlementRange.get(player), coordinates)))
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("Generated Settlement: ");
        settlements.forEach(System.out::println);
        for(Settlement settlement: settlements) {
            Set<Coordinate> visible = navigationService.visible(settlement.coordinate(), grid, settlement.type().range());
            visible.forEach(settlement.player().seenCoordinates()::add);
        }
        return settlements;
    }

    public Map findCastleRange(Collection<Player> players, List<Settlement> settlements, Grid grid) {
        return players.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        p -> {
                            Settlement settlement = settlements.stream()
                                    .filter(s -> s.type() == SettlementType.CASTLE)
                                    .filter(s -> s.player() == p)
                                    .findFirst()
                                    .orElse(null);
                            return navigationService.visible(settlement.coordinate(), grid, settlement.type().range());
                        }
                ));

    }

    private Set<Coordinate> randomCoordinates(Grid grid, int count, Set<Coordinate> toUse, Set<Coordinate> coordinatesSoFar) {
        return randomCoordinates(grid, count, toUse, coordinatesSoFar, 1);
    }

    private Set<Coordinate> randomCoordinates(Grid grid, int count, Set<Coordinate> coordinatesSoFar, int minimumDistance) {
        return randomCoordinates(grid, count, grid.cells().keySet(), coordinatesSoFar, minimumDistance);
    }

    private Set<Coordinate> randomCoordinates(Grid grid, int count, Set<Coordinate> toUse, Set<Coordinate> coordinatesSoFar, int minimumDistance) {
        Set<Coordinate> rands = new HashSet<>();
        while (rands.size() < count) {
            List<Coordinate> avail = toUse.stream()
                    .filter(c -> LandUnit.isLandType(grid.cell(c).tileType()) )
                    .filter(c -> coordinatesSoFar.stream()
                            .allMatch(a -> CoordinateUtil.distance(c, a) >= minimumDistance))
                    .collect(Collectors.toList());
            if (avail.size() <= 0) {
                rands.clear();
                continue;
            }
            Coordinate c = avail.get(RandomUtil.nextInt(avail.size()));
            rands.add(c);
            coordinatesSoFar.add(c);
            avail.remove(c);
        }
        return rands;

    }

    private List<Settlement> generate(Player player, Set<Coordinate> coordinates) {
        return generate(player, coordinates,
                SettlementType.getType(RandomUtil.nextInt(SettlementType.LARGE_CITY.minAge())));
    }

    private List<Settlement> generate(Player player, Set<Coordinate> coordinates, SettlementType settlementType) {
        List<Settlement> settlements = new ArrayList<>();
        for (Coordinate coordinate: coordinates) {
            settlements.add(new Settlement(settlementType.minAge(), coordinate, player));
        }
        return settlements;
    }

}
