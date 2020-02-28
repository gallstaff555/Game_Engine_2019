package com.david.game.utils;

import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.david.game.actors.Player;

import java.util.Map;

import static com.david.game.utils.Constants.PLAYER_SIZE;

public class PlayerCollision {

    private Player player;
    private MapObjects collisionObjects;

    public PlayerCollision(Player player, MapObjects collisionObjects) {
        this.player = player;
        this.collisionObjects = collisionObjects;
    }

    public boolean updateCollisions(Map<String, Boolean> collisionDirections) {
        boolean collisionDetected = false;

        collisionDirections.put("West", false);
        collisionDirections.put("East", false);
        collisionDirections.put("North", false);
        collisionDirections.put("South", false);

        for (RectangleMapObject rectangleObject : collisionObjects.getByType(RectangleMapObject.class)) {
            Rectangle collidingObject = rectangleObject.getRectangle();
            if (Intersector.overlaps(collidingObject, player.getPlayerRectangle())) {
                // collision happened
                collisionDetected = true;
                if (collisionWest(collidingObject)) {
                    collisionDirections.put("West", true);
                    System.out.println("collision West");
                } if (collisionEast(collidingObject)) { //} else if (collisionEast(collidingObject)) {
                    collisionDirections.put("East", true);
                    System.out.println("collision East");
                } if (collisionNorth(collidingObject)) {
                    collisionDirections.put("North", true);
                    System.out.println("collision North");
                } if (collisionSouth(collidingObject)) {
                    collisionDirections.put("South", true);
                    System.out.println("collision South");
                }
            }
        }
        return collisionDetected;
    }

    /*
     *Return true if left part of Player collides with object.
     */
    private boolean collisionWest(Rectangle collidingObject) {
        boolean collisionOnPlayerLeft = false;
        if (player.getX() > (collidingObject.getX() + collidingObject.getWidth() - 2)) {
            collisionOnPlayerLeft = true;
        }
        return collisionOnPlayerLeft;
    }

    /*
     * Return true if right part of Player collides with object.
     */
    private boolean collisionEast(Rectangle collidingObject) {
        boolean collisionOnPlayerRight = false;
        if ( (player.getX() + PLAYER_SIZE) < (collidingObject.getX() + 2) ) {
            collisionOnPlayerRight = true;
        }
        return collisionOnPlayerRight;
    }

    /*
     * Return true if top part of Player collides with object.
     */
    private boolean collisionNorth(Rectangle collidingObject) {
        boolean collisionOnPlayerUp = false;
        if ( (player.getY() + PLAYER_SIZE) < (collidingObject.getY() + 2) ) {
            collisionOnPlayerUp = true;
        }
        return collisionOnPlayerUp;
    }

    /*
     * Return true is bottom part of Player collides with object.
     */
    private boolean collisionSouth(Rectangle collidingObject) {
        boolean collisionOnPlayerUp = false;
        if (player.getY() > (collidingObject.getY() + collidingObject.getHeight() - 2) ) {
            collisionOnPlayerUp = true;
        }
        return collisionOnPlayerUp;
    }

}
