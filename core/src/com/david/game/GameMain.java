package com.david.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.david.game.actors.ForegroundAnimation;
import com.david.game.actors.ForegroundObject;
import com.david.game.actors.Player;
import com.david.game.utils.PlayerCollision;
import com.david.game.utils.PlayerMovement;
import com.david.game.world.GameLevel;

import java.util.HashMap;
import java.util.Map;

import static com.david.game.utils.Constants.CAMERA_HEIGHT;
import static com.david.game.utils.Constants.CAMERA_WIDTH;
import static com.david.game.utils.Constants.COLLISION_LAYER_NAME;
import static com.david.game.utils.Constants.TILE_HALF_SCALE;
import static com.david.game.utils.Constants.TILE_SCALE;
import static com.david.game.utils.Constants.WARP_LAYER_NAME;

public class GameMain extends Game {

	private OrthographicCamera camera;
	private ExtendViewport viewport;
	private TiledMap map;
	private OrthogonalTiledMapRenderer tiledMapRenderer;

	private Stage stage;
	private Player player;
	private PlayerMovement playerMovement;
	private PlayerCollision playerCollision;

	private GameLevel currentLevel;
	private MapObjects collisionObjects;
	private MapObjects warpObjects;

	private Map<String, Boolean> collisionDirections;

	private float horizontalTiles;
	private float verticalTiles;
	private float cameraHalfWidth;
	private float cameraHalfHeight;

	@Override
	public void create () {
		//view
		camera = new OrthographicCamera();
		viewport = new ExtendViewport(CAMERA_WIDTH, CAMERA_HEIGHT, camera);

		//starting map
		map = new TmxMapLoader().load("map/forest_01.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map);

		//create level
		currentLevel = new GameLevel("Starting level", map, tiledMapRenderer);

		//Collision directions array
		this.createCollisionMap();

		//map and camera
		//TODO update these values when loading a new map
		/*MapProperties mapProperties = map.getProperties();
		horizontalTiles = mapProperties.get("width", Integer.class);
		verticalTiles = mapProperties.get("height", Integer.class);
		cameraHalfWidth = CAMERA_WIDTH / 2;
		cameraHalfHeight = CAMERA_HEIGHT / 2;

		//collision layer
		MapLayer collisionLayer = map.getLayers().get(COLLISION_LAYER_NAME);
		collisionObjects = collisionLayer.getObjects();

		//warp layer
		MapLayer warpLayer = map.getLayers().get(WARP_LAYER_NAME);
		warpObjects = warpLayer.getObjects(); */

		stage = new Stage(viewport);
		player = new Player();
		playerMovement = new PlayerMovement(player);
		//playerCollision = new PlayerCollision(player, collisionObjects);

		setUpLevelMapAndCamera(currentLevel);

		//Actors and stage


		stage.addActor(player);
		//addFixedObjectsToStage();

		player.playerPosition(horizontalTiles * TILE_HALF_SCALE + 16,
				verticalTiles * TILE_HALF_SCALE); // * 8 gives middle of map
		camera.position.set(player.getX(), player.getY(), 0);
		camera.update();
	}

	private void setUpLevelMapAndCamera(GameLevel theLevel) {


		MapProperties mapProperties = theLevel.getMap().getProperties();
		horizontalTiles = mapProperties.get("width", Integer.class);
		verticalTiles = mapProperties.get("height", Integer.class);
		cameraHalfWidth = CAMERA_WIDTH / 2;
		cameraHalfHeight = CAMERA_HEIGHT / 2;

		MapLayer collisionLayer = theLevel.getMap().getLayers().get(COLLISION_LAYER_NAME);
		collisionObjects = collisionLayer.getObjects();
		playerCollision = new PlayerCollision(player, collisionObjects);

		MapLayer warpLayer = theLevel.getMap().getLayers().get(WARP_LAYER_NAME);
		warpObjects = warpLayer.getObjects();


	}

	@Override
	public void render () {

		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		//CAMERA AND MAP
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		//movement and user input
		playerMovement.updateMovemement(playerCollision.updateCollisions(collisionDirections), collisionDirections);
		playerAttack();

		playerCollision.updateCollisions(collisionDirections);
		detectWarp();

		stage.act(Gdx.graphics.getDeltaTime()); //actor actions are updated when act() is called
		stage.draw();

		this.setCameraPosition();
		camera.update();
	}

	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose () {
		map.dispose();
		stage.dispose();
		player.playerDispose();
	}

	/**
	 * Camera follows player but will not leave borders of TiledMap
	 */
	private void setCameraPosition() {
		float maxCameraX = (horizontalTiles * TILE_SCALE) - cameraHalfWidth;
		float maxCameraY = (verticalTiles * TILE_SCALE - cameraHalfHeight);
		camera.position.set(Math.min(maxCameraX, Math.max(player.getX(), cameraHalfWidth)),
				Math.min(maxCameraY, Math.max(player.getY(), cameraHalfHeight)),
				0);
	}

	private void playerAttack() {
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			if (player.getPlayerDirection() == 'R') {
				player.attack("rightAttack");
			}
			if (player.getPlayerDirection() == 'L') {
				player.attack("leftAttack");
			}
		}
	}

	private void detectWarp() {
		for (RectangleMapObject rectangleObject : warpObjects.getByType(RectangleMapObject.class)) {

			Rectangle warpObject = rectangleObject.getRectangle();
			//warp player to new level
			if (Intersector.overlaps(warpObject, player.getPlayerRectangle())) {
				String objectName = "map/" + rectangleObject.getName() + ".tmx";
				System.out.println(objectName);
				this.loadNewLevel(objectName);
			}
		}
	}

	private void addFixedObjectsToStage() {
		//Foreground objects
		ForegroundObject pillar_1 = new ForegroundObject("world_objects/pillar_01.png", 48, 416);
		ForegroundObject pillar_2 = new ForegroundObject("world_objects/pillar_01.png", 128, 416);
		ForegroundObject pillar_3 = new ForegroundObject("world_objects/pillar_01.png", 208, 416);
		ForegroundObject pillar_4 = new ForegroundObject("world_objects/pillar_01.png", 288, 416);
		ForegroundObject pillar_5 = new ForegroundObject("world_objects/pillar_01.png", 368, 416);
		ForegroundObject pillar_6 = new ForegroundObject("world_objects/pillar_01.png", 448, 416);

		ForegroundAnimation fire_1 = new ForegroundAnimation("world_objects/objects.png", "fire", 160,144);

		stage.addActor(pillar_1);
		stage.addActor(pillar_2);
		stage.addActor(pillar_3);
		stage.addActor(pillar_4);
		stage.addActor(pillar_5);
		stage.addActor(pillar_6);
		stage.addActor(fire_1);
	}

	private void loadNewLevel(String mapFile) {
		//currentLevel.removeAllObjectsAndAnimations();
		map = new TmxMapLoader().load(mapFile);
		tiledMapRenderer = new OrthogonalTiledMapRenderer(map);
		currentLevel = new GameLevel(mapFile, map, tiledMapRenderer);
		setUpLevelMapAndCamera(currentLevel);
		this.setCameraPosition();
	}

	private void createCollisionMap() {
		collisionDirections = new HashMap<>();
		collisionDirections.put("North", false);
		collisionDirections.put("South", false);
		collisionDirections.put("East", false);
		collisionDirections.put("West", false);
	}

}


