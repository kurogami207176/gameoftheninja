package com.alaindroid.gameoftheninja.units;

import com.alaindroid.gameoftheninja.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
@AllArgsConstructor
public enum UnitType {
    FLAG(1, 1),
    SPY(1, 1),
    PRIV(1, 1),
    SGT(1, 1),
    LT2ND(1, 1),
    LT1ST(1, 1),
    CPT(1, 1),
    MAJ(1, 1),
    LTCOL(1, 1),
    COL(1, 1),
    GEN1(1, 1),
    GEN2(1, 1),
    GEN3(1, 1),
    GEN4(1, 1),
    GEN5(1, 1);

    private int range;
    private int illusionRange;

    public static UnitType random() {
        return RandomUtil.random(values());
    }
}
