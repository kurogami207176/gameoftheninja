package com.alaindroid.gameoftheninja.draw;

import com.badlogic.gdx.graphics.g2d.Sprite;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class SpriteDraw {
    private Sprite sprite;
    private int drawOrder;
}
