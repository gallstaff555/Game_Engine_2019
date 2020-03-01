package com.david.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

import static com.david.game.utils.Constants.ANIMATION_FRAME_SPEED;
import static com.david.game.utils.Constants.PLAYER_SIZE;


public class Player extends Actor {

    private char playerDirection;
    private Sprite sprite = new Sprite(new Texture(Gdx.files.internal("player/link_sprites.png"))); //"character_elf.png"
    private Animation<TextureRegion> characterAnimation;
    private Array<TextureAtlas.AtlasRegion> characterFrames;
    private TextureRegion currentFrame;
    private TextureAtlas textureAtlas;
    private float elapsedTime = 0f;
    private boolean isAttacking = false;

    public Player() {
        this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        setTouchable(Touchable.enabled);
        playerDirection = 'F';
        textureAtlas = new TextureAtlas("player/link_sprites.txt"); //

        //Running animation
        characterFrames = textureAtlas.findRegions("forwardIdle"); //default: forwardIdle
        characterAnimation = new Animation<TextureRegion>( 1/16f, characterFrames);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        currentFrame = characterAnimation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame, getX(), getY(), PLAYER_SIZE, PLAYER_SIZE);

    }

    @Override
    public void act(float delta) {
        super.act(delta);
        characterAnimation = new Animation<TextureRegion>(ANIMATION_FRAME_SPEED, characterFrames);
        sprite.setPosition(this.getX(), this.getY());
        elapsedTime += Gdx.graphics.getDeltaTime();
    }

    public char getPlayerDirection() {
        return this.playerDirection;
    }

    public void setPlayerDirection(char direction) {
        this.playerDirection = direction;
    }

    public void playerPosition(float xMovement, float yMovement) {
        this.setPosition(xMovement, yMovement);
    }

    public void updateAnimation(String region) {
        characterFrames = textureAtlas.findRegions(region);
    }

    public Rectangle getPlayerRectangle() {
        Rectangle rect = sprite.getBoundingRectangle();
        rect.setHeight(PLAYER_SIZE);
        rect.setWidth(PLAYER_SIZE);
        return rect;
    }

    public void setPlayerAttacking(boolean attacking) {
        this.isAttacking = attacking;
    }

    public boolean getPlayerAttacking() {
        return this.isAttacking;
    }

    public void playerDispose() {
        this.textureAtlas.dispose();
    }
}
