package com.alaindroid.gameoftheninja.draw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BackgroundDrawer {

    Texture backgroundTexture;
    Sprite backgroundSprite;

    public static final double SQRT_THREE = Math.sqrt(3);

    public void create() {
        backgroundTexture = new Texture("background/starfield.png");
        backgroundSprite =new Sprite(backgroundTexture);
    }

    public void dispose() {
        backgroundTexture.dispose();
    }

    public void draw(SpriteBatch spriteBatch, float width, float height) {
        backgroundSprite.setSize(width, height);
        backgroundSprite.draw(spriteBatch);
//        spriteBatch.draw(backgroundTexture, 0 , 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
