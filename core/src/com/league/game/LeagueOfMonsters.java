package com.league.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

	ShapeRenderer shape;
	Hero hero;
	JSONParser parser;
	JSONObject gameState;

	private Map<String, Hero> heroesState;
	private Map<String, JSONObject> heroes;
	Socket socket;

	@Override
	public void create () {
		gameState = new JSONObject();
		parser = new JSONParser();
		heroesState = new HashMap<>();
		heroes = new HashMap<>();
		gameState.put("xPos", 100L);
		gameState.put("yPos", 100L);
		shape = new ShapeRenderer();
		hero = Hero.builder().xPos(50).yPos(50).size(50).build();
		try {
			socket = IO.socket("http://localhost:3000");
			socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					Hero hero = Hero.builder().xPos(50L).yPos(50L).size(50).build();
					heroesState.put(socket.id(), hero);
					heroes.put(socket.id(), new JSONObject());
					heroes.get(socket.id()).put("xPos", 50L);
					heroes.get(socket.id()).put("yPos", 50L);
					System.out.println("Connected to Game Server");
				}
			});
			socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					System.out.println("Disconnected from Game Server");
				}
			});
			socket.on("updateState", new Emitter.Listener() {
				@Override
				public void call(Object... args) {
					try {
						gameState = (JSONObject) parser.parse(String.valueOf(args[0]));
						JSONArray connectedPlayers = (JSONArray) gameState.get("connected");
						StateManager.updateStateOfAllHeroes(connectedPlayers, heroes);
						for (Map.Entry<String, JSONObject> hero: heroes.entrySet()) {
							String playerId = hero.getKey();
							if (heroesState.containsKey(playerId)) {
								heroesState.get(playerId).update(hero.getValue());
							} else {
								heroesState.put(playerId, Hero.builder().xPos(50L).yPos(50L).size(50).build());
							}
						}
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
		shape.begin(ShapeRenderer.ShapeType.Filled);
		for (Map.Entry<String, Hero> hero : heroesState.entrySet()) {
			hero.getValue().draw(shape);
		}
		shape.end();
	}
	
	@Override
	public void dispose () {
		shape.dispose();
	}
}
