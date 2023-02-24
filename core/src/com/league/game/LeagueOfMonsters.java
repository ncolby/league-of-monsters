package com.league.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.league.game.enums.FacingDirection;
import com.league.game.Handlers.InputHandler;
import com.league.game.Handlers.StateHandler;
import com.league.game.models.HeroGameEntity;
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
	private static final int VIEWPORT_HEIGHT = 500;
	private static final int VIEWPORT_WIDTH = 1000;
	private Animation<TextureRegion> heroMovementAnimation;
	private Animation<TextureRegion> heroAttackAnimation;
	private Animation<TextureRegion> heroAbilityAnimation;
	private TextureRegion heroIdleAnimation;
	private SpriteBatch spriteBatch;
	private float animationDuration;
	private OrthographicCamera playerCamera;
	private TextureRegion currentFrame;
	private TextureRegion abilityFrame;
	private JSONParser parser;
	private JSONObject gameState;

	private Map<String, HeroGameEntity> heroes;
	private Nexus nexus;
	private Socket socket;
	private Texture backgroundTexture;
	private TextureRegion backgroundTextureRegion;
	private Viewport viewport;


	@Override
	public void create () {
		SpringApplication.run(LeagueOfMonsters.class);
		playerCamera = new OrthographicCamera();
		viewport = new FitViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, playerCamera);
		playerCamera.position.set(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0);

		nexus = Nexus.builder().xPos(2000L).yPos(-100).build();
		nexus.setNexusSprite("nexus.png");
		backgroundTexture = new Texture("gamemap.png");
		backgroundTextureRegion = new TextureRegion(backgroundTexture);
		gameState = new JSONObject();
		parser = new JSONParser();
		heroes = new HashMap<String, HeroGameEntity>();
		spriteBatch = new SpriteBatch();
		animationDuration = 0f;
		heroMovementAnimation = ImageProcessor.getImageAnimation("pumpkin_head_walk.png");
		heroAttackAnimation = ImageProcessor.getImageAnimation("pumpkin_head_attack.png");
		heroIdleAnimation = ImageProcessor.getImageStill("pumpkin_head_walk.png");
		heroAbilityAnimation = ImageProcessor.getImageAnimation("pumpkin_head_vines.png");
		try {
			socket = IO.socket("http://localhost:3000");
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					heroes.put(socket.id(), new HeroGameEntity(
							0, 0, 0, 0, null, 0,
							null,false, false, FacingDirection.NONE, socket.id()));
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
		ScreenUtils.clear(0, 0, 0, 0);
		animationDuration += Gdx.graphics.getDeltaTime();
		InputHandler.handleInput(socket);
		spriteBatch.setProjectionMatrix(playerCamera.combined);
		socket.emit("getState");
		spriteBatch.begin();
		nexus.draw(spriteBatch);
		spriteBatch.draw(backgroundTextureRegion, 0, 0);
		for (Map.Entry<String, HeroGameEntity> hero : heroes.entrySet()) {
			if (hero.getKey().equals(socket.id())) {
				playerCamera.position.x = hero.getValue().getXPos();
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
			hero.getValue().setEntityImage(currentFrame);
			hero.getValue().draw(spriteBatch);
//			if (abilityFrame != null && hero.getValue().getFacingDirection().equals(FacingDirection.LEFT)) {
//				hero.getValue().drawAbility(spriteBatch, abilityFrame, hero.getValue().getXPos() - 200, hero.getValue().getYPos());
//			} else if ( abilityFrame != null && hero.getValue().getFacingDirection().equals(FacingDirection.RIGHT)) {
//				hero.getValue().drawAbility(spriteBatch, abilityFrame, hero.getValue().getXPos() + 100, hero.getValue().getYPos());
//			}
		}
		spriteBatch.end();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
	
	@Override
	public void dispose () {
		spriteBatch.dispose();
		backgroundTexture.dispose();
	}
}
