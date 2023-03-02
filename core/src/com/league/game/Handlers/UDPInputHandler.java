package com.league.game.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.league.game.LeagueOfHorrors;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UDPInputHandler {

    public static void handleInput(LeagueOfHorrors gameManager) {
        Map<String, String> command = new HashMap<String, String>();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (gameManager.getUdpNetworkHandler() != null) {
                command.put("command", "left_" + gameManager.getPlayerId());
                gameManager.udpNetworkHandler.sendData(JSONObject.toJSONString(command));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (gameManager.udpNetworkHandler != null) {
                command.put("command", "right_" + gameManager.getPlayerId());
                gameManager.udpNetworkHandler.sendData(JSONObject.toJSONString(command));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (gameManager.udpNetworkHandler != null) {
                command.put("command", "up_" + gameManager.getPlayerId());
                gameManager.udpNetworkHandler.sendData(JSONObject.toJSONString(command));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (gameManager.udpNetworkHandler != null) {
                command.put("command", "down_" + gameManager.getPlayerId());
                gameManager.udpNetworkHandler.sendData(JSONObject.toJSONString(command));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            if (gameManager.udpNetworkHandler != null) {
                command.put("command", "skill1_" + gameManager.getPlayerId());
                gameManager.udpNetworkHandler.sendData(JSONObject.toJSONString(command));
            }
        }
    }

}
