package com.league.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.league.game.heroes.Hero;
import com.league.game.heroes.Utils.StateManager;
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
	Socket socket;

	private static Texture backgroundTexture;
	private static Sprite backgroundSprite;

	@Override
	public void create () {
		backgroundTexture = new Texture("background.png");
		backgroundSprite = new Sprite(backgroundTexture);
		gameState = new JSONObject();
		parser = new JSONParser();
		heroes = new HashMap<>();
		heroesState = new HashMap<>();
		spriteBatch = new SpriteBatch();
		animationDuration = 0f;
		gameState.put("xPos", 100L);
		gameState.put("yPos", 100L);
		playerCamera = new OrthographicCamera(1280, 720);
		playerCamera.position.set(50, 50, 0);
		playerCamera.update();
		movementSpriteSheet = new Texture(Gdx.files.internal("pumpkin_head_walk.png"));
		shape = new ShapeRenderer();
		hero = Hero.builder().xPos(50).yPos(50).size(50).build();

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

		heroMovementAnimation = new Animation<TextureRegion>(0.025f, movementFrames);
		try {
			socket = IO.socket("http://localhost:3000");
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					hero = Hero.builder().xPos(50L).yPos(50L).size(50).build();
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
						StateManager.updateStateOfAllHeroes(connectedPlayers, heroesState);
						for (Map.Entry<String, JSONObject> hero: heroesState.entrySet()) {
							String playerId = hero.getKey();
							if (heroes.containsKey(playerId)) {
								heroes.get(playerId).update(hero.getValue());
							} else {
								heroes.put(playerId, Hero.builder().xPos(50L).yPos(50L).size(50).build());
							}
						}
						currentFrame = heroMovementAnimation.getKeyFrame(animationDuration, true);
						System.out.println(connectedPlayers);
						System.out.println("-----------------------------");
						System.out.println(heroesState);
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
		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			if (socket != null) {
				socket.emit("command", "left");
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			if (socket != null) {
				socket.emit("command", "right");
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			if (socket != null) {
				socket.emit("command", "up");
			}
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			if (socket != null) {
				socket.emit("command", "down");
			}
		}
		System.out.println(playerCamera.position);
		playerCamera.position.set(hero.getXPos(), 0, 0);
		playerCamera.update();
		spriteBatch.setProjectionMatrix(playerCamera.combined);
		spriteBatch.begin();
		backgroundSprite.draw(spriteBatch);
		for (Map.Entry<String, Hero> hero : heroes.entrySet()) {
			hero.getValue().draw(spriteBatch, currentFrame);
		}
		spriteBatch.end();
		currentFrame = movementFrames[4];
	}
	
	@Override
	public void dispose () {
		shape.dispose();
	}
}
