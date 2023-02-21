package com.league.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.league.game.Utils.InputHandler;
import com.league.game.heroes.Hero;
import com.league.game.Utils.StateManager;
import com.league.game.models.Nexus;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;
import java.util.Map;

public class LeagueOfMonsters extends ApplicationAdapter {

	private static final int SPRITE_COLS = 4, SPRITE_ROWS = 2;
	private Animation<TextureRegion> heroMovementAnimation;
	private Texture movementSpriteSheet;
	private SpriteBatch spriteBatch;
	private float animationDuration;
	private OrthographicCamera playerCamera;

	private TextureRegion currentFrame;


	ShapeRenderer shape;
	Hero hero;
	JSONParser parser;
	JSONObject gameState;

	private TextureRegion[] movementFrames;

	private Map<String, Hero> heroes;
	private Map<String, JSONObject> heroesState;
	private Nexus nexus;
	Socket socket;

	private static Texture backgroundTexture;
	private static Sprite backgroundSprite;

	@Override
	public void create () {
		nexus = Nexus.builder().xPos(2000L).yPos(-100).build();
		nexus.setNexusSprite("nexus.png");
		backgroundTexture = new Texture("background.png");
		backgroundSprite = new Sprite(backgroundTexture);
		gameState = new JSONObject();
		parser = new JSONParser();
		heroes = new HashMap<>();
		heroesState = new HashMap<>();
		spriteBatch = new SpriteBatch();
		hero = Hero.builder().xPos(50L).yPos(50L).size(50).build();
		animationDuration = 0f;
		gameState.put("xPos", 100L);
		gameState.put("yPos", 100L);
		playerCamera = new OrthographicCamera(1280, 720);
		playerCamera.position.set(50, 50, 0);
		playerCamera.update();
		movementSpriteSheet = new Texture(Gdx.files.internal("pumpkin_head_walk.png"));
		shape = new ShapeRenderer();
		TextureRegion[][] movementSpriteSheetSplits = TextureRegion.
				split(movementSpriteSheet, movementSpriteSheet.getWidth() / SPRITE_COLS
						, movementSpriteSheet.getHeight() / SPRITE_ROWS);

		movementFrames = new TextureRegion[SPRITE_COLS * SPRITE_ROWS];
		int frameCount = 0;
		for (int i = 0; i < SPRITE_ROWS; i++) {
			for (int j = 0; j < SPRITE_COLS; j++) {
				movementFrames[frameCount++] = movementSpriteSheetSplits[i][j];
			}
		}

		heroMovementAnimation = new Animation<TextureRegion>(0.15f, movementFrames);
		try {
			socket = IO.socket("http://localhost:3000");
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					hero = Hero.builder().xPos(50L).yPos(50L).size(50).heroId(socket.id()).build();
					heroes.put(socket.id(), hero);
					heroesState.put(socket.id(), new JSONObject());
					heroesState.get(socket.id()).put("xPos", 50L);
					heroesState.get(socket.id()).put("yPos", 50L);
					System.out.println("Connected to Game Server");

				}
			});
			socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					// TODO: on an event of a disconnect, we should send a signal to try to reconnect
					socket.emit("disconnect");
					System.out.println("Disconnected from Game Server");
				}
			});
			socket.on("updateState", new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					try {
						gameState = (JSONObject) parser.parse(String.valueOf(args[0]));
						JSONArray connectedPlayers = (JSONArray) gameState.get("connected");
						heroes = StateManager.replicateServerState(connectedPlayers);
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			});
			socket.open();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 0);
		animationDuration += Gdx.graphics.getDeltaTime();
		InputHandler.handleInput(socket);
		spriteBatch.setProjectionMatrix(playerCamera.combined);
		socket.emit("getState");
		spriteBatch.begin();
		backgroundSprite.draw(spriteBatch);
		nexus.draw(spriteBatch);
		for (Map.Entry<String, Hero> localHero : heroes.entrySet()) {
			if (localHero.getKey().equals(hero.getHeroId())) {
				playerCamera.position.set(localHero.getValue().getXPos(), 0, 0);
				playerCamera.update();
			}
			if (localHero.getValue().isMoving()) {
				currentFrame = heroMovementAnimation.getKeyFrame(animationDuration, true);
			} else {
				currentFrame = movementFrames[4];
			}
			localHero.getValue().draw(spriteBatch, currentFrame);
		}
		spriteBatch.end();
	}
	
	@Override
	public void dispose () {
		shape.dispose();
		spriteBatch.dispose();
		movementSpriteSheet.dispose();
	}
}
