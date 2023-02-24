package com.league.game.Handlers;


import com.league.game.LeagueOfHorrors;
import com.league.game.models.HeroGameEntity;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@Slf4j
@Getter
public class NetworkHandler {

    private LeagueOfHorrors gameManager;
    private Socket socket;

    public NetworkHandler (LeagueOfHorrors gameManager) {
       this.gameManager = gameManager;
    }

    public void getAndConfigureSocket() {
        try {
            socket = IO.socket("http://localhost:3000");
            configureSocket();
            socket.open();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void configureSocket() {
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                HeroGameEntity heroGameEntity = new HeroGameEntity();
                heroGameEntity.setHeroId(socket.id());
                gameManager.heroes.put(socket.id(), new HeroGameEntity());
                log.info("Connected to Game Server");
            }
        });

        socket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                log.info("Disconnected from Game Server");
            }
        });
        socket.on("updateState", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject gameState = (JSONObject) parser.parse(String.valueOf(args[0]));
                    JSONArray connectedPlayers = (JSONArray) gameState.get("connected");
                    gameManager.heroes = StateHandler.replicateServerState(connectedPlayers);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        });
    }
}
