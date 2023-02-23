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
import com.league.game.enums.FacingDirection;
import com.league.game.Handlers.InputHandler;
import com.league.game.heroes.Hero;
import com.league.game.Handlers.StateHandler;
import com.league.game.models.Nexus;
import com.league.game.utils.ImageProcessor;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class LeagueOfMonsters extends ApplicationAdapter {
	private Animation<TextureRegion> heroMovementAnimation;
	private Animation<TextureRegion> heroAttackAnimation;

	private Animation<TextureRegion> heroAbilityAnimation;
	private TextureRegion heroIdleAnimation;
	private SpriteBatch spriteBatch;
	private float animationDuration;
	private OrthographicCamera playerCamera;
	private TextureRegion currentFrame;

	private TextureRegion abilityFrame;

	private ShapeRenderer shape;
	private JSONParser parser;
	private JSONObject gameState;
	private Map<String, Hero> heroes;
	private Nexus nexus;
	private Socket socket;
	private static Texture backgroundTexture;
	private static Sprite backgroundSprite;

	@Override
	public void create () {
		SpringApplication.run(LeagueOfMonsters.class);
		nexus = Nexus.builder().xPos(2000L).yPos(-100).build();
		nexus.setNexusSprite("nexus.png");
		backgroundTexture = new Texture("background.png");
		backgroundSprite = new Sprite(backgroundTexture);
		backgroundSprite.setSize( 3645, 1024);
		backgroundSprite.setPosition(120, 235);
		gameState = new JSONObject();
		parser = new JSONParser();
		heroes = new HashMap<String, Hero>();
		spriteBatch = new SpriteBatch();
		animationDuration = 0f;
		playerCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		playerCamera.position.set(0, 0, 0);
		playerCamera.update();
		shape = new ShapeRenderer();
		heroMovementAnimation = ImageProcessor.getImageAnimation("pumpkin_head_walk.png");
		heroAttackAnimation = ImageProcessor.getImageAnimation("pumpkin_head_attack.png");
		heroIdleAnimation = ImageProcessor.getImageStill("pumpkin_head_walk.png");
		heroAbilityAnimation = ImageProcessor.getImageAnimation("pumpkin_head_vines.png");
		try {
			socket = IO.socket("http://localhost:3000");
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					heroes.put(socket.id(), Hero.builder().xPos(0).yPos(0).facingDirection(FacingDirection.NONE).heroId(socket.id()).build());
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
						heroes = StateHandler.replicateServerState(connectedPlayers);
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
		ScreenUtils.clear(0, 240, 0, 0);
		animationDuration += Gdx.graphics.getDeltaTime();
		InputHandler.handleInput(socket);
		spriteBatch.setProjectionMatrix(playerCamera.combined);
		socket.emit("getState");
		spriteBatch.begin();
		nexus.draw(spriteBatch);
		backgroundSprite.draw(spriteBatch);
		for (Map.Entry<String, Hero> hero : heroes.entrySet()) {
			if (hero.getKey().equals(socket.id())) {
				playerCamera.position.set(hero.getValue().getXPos()  + 150, hero.getValue().getYPos() + 240, 0);
				playerCamera.update();
			}
			if (hero.getValue().isMoving()) {
				currentFrame = heroMovementAnimation.getKeyFrame(animationDuration, true);
			} else if (hero.getValue().isAttacking()) {
				currentFrame = heroAttackAnimation.getKeyFrame(animationDuration / 2, true);
				abilityFrame = heroAbilityAnimation.getKeyFrame(animationDuration / 2, true);
			} else {
				currentFrame = heroIdleAnimation;
				abilityFrame = null;
			}
			hero.getValue().draw(spriteBatch, currentFrame);
			if (abilityFrame != null && hero.getValue().getFacingDirection().equals(FacingDirection.LEFT)) {
				hero.getValue().drawAbility(spriteBatch, abilityFrame, hero.getValue().getXPos() - 200, hero.getValue().getYPos());
			} else if ( abilityFrame != null && hero.getValue().getFacingDirection().equals(FacingDirection.RIGHT)) {
				hero.getValue().drawAbility(spriteBatch, abilityFrame, hero.getValue().getXPos() + 100, hero.getValue().getYPos());
			}
		}
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
	
	@Override
	public void dispose () {
		shape.dispose();
		spriteBatch.dispose();
		backgroundTexture.dispose();
	}
}
