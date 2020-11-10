package com.alaindroid.gameoftheninja.draw;

import com.alaindroid.gameoftheninja.units.Unit;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UnitDrawer {
    final Map<String, Texture> unitTypeTextureMap = new HashMap<>();

    public void create() {
    }

    public void dispose() {
        unitTypeTextureMap.values().stream().forEach(Texture::dispose);
    }

    public List<SpriteDraw> draw(SpriteBatch spriteBatch,
                     List<Unit> units) {
        return units.stream()
                .map( unit -> renderTexture(spriteBatch, unit) )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private SpriteDraw renderTexture(SpriteBatch spriteBatch, Unit unit) {
        Point2D p = unit.currentPoint();

        String key = textureKey.apply(unit);
        Texture texture = unitTypeTextureMap.computeIfAbsent(key, k -> {
            String path = "unit/" + k + ".png";
            System.out.println("path: " + path);
            return new Texture(path);
        });
        Sprite sprite = new Sprite(texture);
        float scale = 0.7f;
        sprite.setScale(scale);
        sprite.setX(p.x() - texture.getWidth() / 2);
        sprite.setY(p.y() - texture.getHeight() / 2);
        sprite.setRotation(unit.currentAngle());
        sprite.draw(spriteBatch);
        return new SpriteDraw(sprite, (int) p.y() - (unit.moving()? 100 : 0));
    }
    private Function<Unit, String> textureKey = unit -> (unit.player().color() + "/" + unit.unitType()).toLowerCase();
}
