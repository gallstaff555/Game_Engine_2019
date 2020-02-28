package com.david.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ForegroundObject extends Actor {

    Sprite sprite;

    public ForegroundObject(String textureFile, int theX, int theY) {
        sprite = new Sprite(new Texture(Gdx.files.internal(textureFile)));
        sprite.setPosition(theX, theY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
