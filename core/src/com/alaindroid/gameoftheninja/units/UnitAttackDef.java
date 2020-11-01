package com.alaindroid.gameoftheninja.units;

import java.util.*;

import static com.alaindroid.gameoftheninja.units.UnitType.*;

public class UnitAttackDef {

    public static Set<UnitType> attackable(UnitType unitType) {
        switch (unitType) {
            case FLAG:
                return set(FLAG);
            case SPY:
                return set(FLAG, SGT, LT2ND, LT1ST, CPT, MAJ, LTCOL, COL, GEN1, GEN2, GEN3, GEN4, GEN5);
            case PRIV:
                return set(FLAG, SPY);
            case SGT:
                return set(FLAG, PRIV);
            case LT2ND:
                return set(FLAG, PRIV, SGT);
            case LT1ST:
                return set(FLAG, PRIV, SGT, LT2ND);
            case CPT:
                return set(FLAG, PRIV, SGT, LT2ND, LT1ST);
            case MAJ:
                return set(FLAG, PRIV, SGT, LT2ND, LT1ST, CPT);
            case LTCOL:
                return set(FLAG, PRIV, SGT, LT2ND, LT1ST, CPT, MAJ);
            case COL:
                return set(FLAG, PRIV, SGT, LT2ND, LT1ST, CPT, MAJ, LTCOL);
            case GEN1:
                return set(FLAG, PRIV, SGT, LT2ND, LT1ST, CPT, MAJ, LTCOL, COL);
            case GEN2:
                return set(FLAG, PRIV, SGT, LT2ND, LT1ST, CPT, MAJ, LTCOL, COL, GEN1);
            case GEN3:
                return set(FLAG, PRIV, SGT, LT2ND, LT1ST, CPT, MAJ, LTCOL, COL, GEN1, GEN2);
            case GEN4:
                return set(FLAG, PRIV, SGT, LT2ND, LT1ST, CPT, MAJ, LTCOL, COL, GEN1, GEN2, GEN3);
            case GEN5:
                return set(FLAG, PRIV, SGT, LT2ND, LT1ST, CPT, MAJ, LTCOL, COL, GEN1, GEN2, GEN3, GEN4);
        }
        return Collections.EMPTY_SET;
    }

    private static Set<UnitType> set(UnitType... type) {
        return new HashSet<>(Arrays.asList(type));
    }
}
