package com.alaindroid.gameoftheninja.service;

import com.alaindroid.gameoftheninja.units.Unit;
import com.alaindroid.gameoftheninja.units.UnitAttackDef;
import lombok.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FightResolverService {
    private UnitAttackDef unitAttackDef;
    public FightResolution findLoser(Unit attacker, Unit defender) {
        if (UnitAttackDef.attackable(attacker.unitType()).contains(defender.unitType())) {
            return new FightResolution(Collections.singletonList(attacker),
                    Collections.singletonList(defender));
        }
        else if (UnitAttackDef.attackable(defender.unitType()).contains(attacker.unitType())) {
            return new FightResolution(Collections.singletonList(defender),
                    Collections.singletonList(attacker));
        }
        else {
            return new FightResolution(Collections.EMPTY_LIST,
                    Arrays.asList(attacker, defender));
        }
    }

    @Value
    public static class FightResolution {
        private final List<Unit> victor;
        private final List<Unit> defeated;
    }
}
