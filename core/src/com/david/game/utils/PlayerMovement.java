package com.david.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.david.game.actors.Player;

import java.util.Map;

import static com.david.game.utils.Constants.MOVEMENT_SPEED;

public class PlayerMovement {

    private Player player;

    public PlayerMovement(Player player) {
        this.player = player;
    }

    //TODO dont search for collisions in opposite direction of movement
    //TODO implement quad tree search
    //TODO fix collision detection bug
    public void updateMovemement(boolean collisionDetected, Map<String, Boolean> collisionDirections) {
        boolean leftPressed = false;
        boolean upPressed = false;
        boolean downPressed = false;
        boolean rightPressed = false;
        boolean no_input = true; //used for animation


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            leftPressed = true;
            no_input = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rightPressed = true;
            no_input = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            upPressed = true;
            no_input = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            downPressed = true;
            no_input = false;
        }

        if (no_input) {
            if (player.getPlayerDirection() == 'F') {
                player.updateCharacterFrames("forwardIdle");
            } else if (player.getPlayerDirection() == 'L') {
                player.updateCharacterFrames("leftIdle");
            } else if (player.getPlayerDirection() == 'R') {
                player.updateCharacterFrames("rightIdle");
            } else {  //up
                player.updateCharacterFrames("backIdle");
            }
        }

        boolean upLeftPressed = upPressed && leftPressed;
        boolean downLeftPressed = downPressed && leftPressed;
        boolean upRightPressed = upPressed && rightPressed;
        boolean downRightPressed = downPressed && rightPressed;

        if (leftPressed) {
            player.setPlayerDirection('L');
            boolean leftBlocked =  collisionDetected && ( collisionDirections.get("West") );
            player.updateCharacterFrames("leftRun"); //"leftRun
            if (upLeftPressed) {
                if (leftBlocked) {
                    player.playerPosition(player.getX(), player.getY() + MOVEMENT_SPEED);
                } else {
                    if (  !collisionDirections.get("North")) {
                        player.playerPosition(player.getX() - MOVEMENT_SPEED, player.getY() + MOVEMENT_SPEED);
                    } else {
                        player.playerPosition(player.getX() - MOVEMENT_SPEED, player.getY());
                    }
                }
            } else if (downLeftPressed) {
                if (leftBlocked) {
                    player.playerPosition(player.getX(), player.getY() - MOVEMENT_SPEED);
                } else {
                    if ( !collisionDirections.get("South") ) {
                        player.playerPosition(player.getX() - MOVEMENT_SPEED, player.getY() - MOVEMENT_SPEED);
                    } else {
                        player.playerPosition(player.getX() - MOVEMENT_SPEED, player.getY());
                    }
                }
            } else if (!leftBlocked){
                player.playerPosition(player.getX() - MOVEMENT_SPEED, player.getY());
            }
        } else if (rightPressed) {
            player.setPlayerDirection('R');
            boolean rightBlocked = collisionDetected && ( collisionDirections.get("East"));
            player.updateCharacterFrames("rightRun"); //"rightRun"
            if (upRightPressed) {
                if (rightBlocked) {
                    player.playerPosition(player.getX(), player.getY() + MOVEMENT_SPEED);
                } else {
                    if ( !collisionDirections.get("North")) {
                        player.playerPosition(player.getX() + MOVEMENT_SPEED, player.getY() + MOVEMENT_SPEED);
                    } else {
                        player.playerPosition(player.getX() + MOVEMENT_SPEED, player.getY());
                    }
                }
            } else if (downRightPressed) {
                if (rightBlocked) {
                    player.playerPosition(player.getX(), player.getY() - MOVEMENT_SPEED);
                } else {
                    if ( !collisionDirections.get("South")) {
                        player.playerPosition(player.getX() + MOVEMENT_SPEED, player.getY() - MOVEMENT_SPEED);
                    } else {
                        player.playerPosition(player.getX() + MOVEMENT_SPEED, player.getY());
                    }
                }
            } else if (!rightBlocked) {
                player.playerPosition(player.getX() + MOVEMENT_SPEED, player.getY()); //don't update position
            }

        } else if (downPressed) {
            player.setPlayerDirection('F');
            boolean downBlocked = collisionDetected && ( collisionDirections.get("South") );
            player.updateCharacterFrames("forwardRun");
            if (!downBlocked) {
                player.playerPosition(player.getX(), player.getY() - MOVEMENT_SPEED);
            }
        } else if (upPressed) {
            player.setPlayerDirection('U');
            boolean upBlocked = collisionDetected && ( collisionDirections.get("North"));
            player.updateCharacterFrames("backRun");
            if (!upBlocked) {
                player.playerPosition(player.getX(), player.getY() + MOVEMENT_SPEED);
            }
        }
    }
}

