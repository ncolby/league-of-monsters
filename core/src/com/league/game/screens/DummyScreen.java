package com.league.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.league.game.Handlers.UDPCreationHandler;
import com.league.game.Handlers.UDPInputHandler;
import com.league.game.LeagueOfHorrors;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DummyScreen extends ScreenAdapter {

    private LeagueOfHorrors gameManager;

    public DummyScreen(LeagueOfHorrors gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void show(){
        UDPCreationHandler.handleCreation(gameManager);
    }

    @Override
    public void render(float delta) {
        UDPInputHandler.handleInput(gameManager);
        Map<String, String> command = new HashMap<String, String>();
        command.put("getUpdate", gameManager.getPlayerId());
        gameManager.udpNetworkHandler.sendData(JSONObject.toJSONString(command));
    }

    @Override
    public void hide(){}
}
