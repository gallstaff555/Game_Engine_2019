package com.david.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import static com.david.game.utils.Constants.ANIMATION_FRAME_SPEED;
import static com.david.game.utils.Constants.FIRE_SIZE;
import static com.david.game.utils.Constants.PLAYER_SIZE;

public class ForegroundAnimation extends Actor {

    private Sprite sprite; // = new Sprite(new Texture(Gdx.files.internal("world_objects/objects.png")));
    private Animation<TextureRegion> objectAnimation;
    private Array<TextureAtlas.AtlasRegion> objectFrames;
    private TextureAtlas textureAtlas;
    private float elapsedTime = 0f;

    public ForegroundAnimation(String textureFile, String animation, int theX, int theY) {
        sprite = new Sprite(new Texture(Gdx.files.internal(textureFile)));
        this.setBounds(theX, theY, sprite.getWidth(), sprite.getHeight());
        textureAtlas = new TextureAtlas("world_objects/objects.txt");
        objectFrames = textureAtlas.findRegions(animation);
        objectAnimation = new Animation<TextureRegion>( 1/16f, objectFrames);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //sprite.draw(batch); //draws the sprite only, not the animation
        TextureRegion currentFrame = objectAnimation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame, getX(), getY(), FIRE_SIZE, FIRE_SIZE);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        objectAnimation = new Animation<TextureRegion>(ANIMATION_FRAME_SPEED, objectFrames);
        sprite.setPosition(this.getX(), this.getY());
        elapsedTime += Gdx.graphics.getDeltaTime();
    }

    public void objectDispose() {
        this.textureAtlas.dispose();
    }
}
