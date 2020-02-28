package com.david.game.world;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.david.game.actors.ForegroundAnimation;
import com.david.game.actors.ForegroundObject;

import java.util.ArrayList;

import static com.david.game.utils.Constants.COLLISION_LAYER_NAME;

public class GameLevel {

    //name, map, maprenderer, collection of actors, MapObjects: collisionObjects and warp objects

    private String name;
    private ArrayList<ForegroundObject> foregroundObjects;
    private ArrayList<ForegroundAnimation> foregroundAnimations;

    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledMapRenderer;

    MapLayer collisionLayer; // = map.getLayers().get(COLLISION_LAYER_NAME);
    MapObjects collisionObjects; // =  collisionLayer.getObjects();


    public GameLevel(String name, TiledMap map, OrthogonalTiledMapRenderer tiledMapRenderer) {
        this.name = name;
        this.map = map;
        this.tiledMapRenderer = tiledMapRenderer;

        collisionLayer = map.getLayers().get(COLLISION_LAYER_NAME);
        collisionObjects = collisionLayer.getObjects();
    }

    public void addForegroundObject(ForegroundObject object) {
        this.foregroundObjects.add(object);
    }

    public void addForegroundAnimation(ForegroundAnimation object) {
        this.foregroundAnimations.add(object);
    }

    public void removeAllObjectsAndAnimations() {
        this.foregroundObjects.clear();
        this.foregroundAnimations.clear();
    }

    public TiledMap getMap() {
        return this.map;
    }

    public TiledMapRenderer getMapRenderer() {
        return this.tiledMapRenderer;
    }

    public MapObjects getCollisionObjects() {
        return this.collisionObjects;
    }

    public MapLayer getCollisionLayer() {
        return this.collisionLayer;
    }

    public String getName() {
        return this.name;
    }
}
