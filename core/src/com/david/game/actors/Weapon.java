package com.david.game.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;

import static com.david.game.utils.Constants.WEAPON_FRAME_SPEED;
import static com.david.game.utils.Constants.PLAYER_SIZE;

public class Weapon extends Actor {

    private Sprite sprite = new Sprite(new Texture(Gdx.files.internal("weapons/sword.png")));
    private Animation<TextureRegion> weaponAnimation;
    private Array<TextureAtlas.AtlasRegion> weaponFrames;
    private TextureRegion currentFrame;
    private TextureAtlas textureAtlas;
    private float elapsedTime = 0f;
    private boolean weaponAttacking = false;
    private boolean attackingAllowed = true;

    public Weapon() {
        this.setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        setTouchable(Touchable.enabled);
        textureAtlas = new TextureAtlas("weapons/sword.txt"); //"player_sprites.txt"
        weaponFrames = textureAtlas.findRegions("forwardSword"); //forwardIdle
        weaponAnimation = new Animation<TextureRegion>( 1/16f, weaponFrames);
        weaponAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        currentFrame = weaponAnimation.getKeyFrame(elapsedTime);
        if (weaponAttacking) {
            batch.draw(currentFrame, getX(), getY(), PLAYER_SIZE, PLAYER_SIZE);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        weaponAnimation = new Animation<TextureRegion>(WEAPON_FRAME_SPEED, weaponFrames);
        sprite.setPosition(this.getX(), this.getY());
        if (weaponAttacking) {
            elapsedTime += Gdx.graphics.getDeltaTime();
        }
        if (weaponAnimation.isAnimationFinished(elapsedTime)) {
            weaponAttacking = false;
            elapsedTime = 0f;
        }
    }

    //only one attack allowed per spacebar press
    //return true if this call leads to weapon attacking
    public boolean weaponAttack(char direction) {
        boolean isAttacking = false;
        if (!Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            attackingAllowed = true;
            if (direction == 'F') {
                weaponFrames = textureAtlas.findRegions("forwardSword");
            } else if (direction == 'L') {
                weaponFrames = textureAtlas.findRegions("leftSword");
            } else if (direction == 'R') {
                weaponFrames = textureAtlas.findRegions("rightSword");
            } else {
                weaponFrames = textureAtlas.findRegions("backSword");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && attackingAllowed) {
            attackingAllowed = false;
            weaponAttacking = true;
            isAttacking = true;
        }
        return isAttacking;
    }

    public boolean isWeaponAttacking() {
        return this.weaponAttacking;
        //return !this.attackingAllowed;
    }


}
