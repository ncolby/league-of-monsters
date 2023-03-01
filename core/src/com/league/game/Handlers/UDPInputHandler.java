package com.league.game.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.socket.client.Socket;
import org.json.simple.JSONObject;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Map;

public class UDPInputHandler {

    public static void handleInput(UDPNetworkHandler udpNetworkHandler) {
        Map<String, String> command = new HashMap<String, String>();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (udpNetworkHandler != null) {
                command.put("command", "left");
                udpNetworkHandler.sendData(JSONObject.toJSONString(command));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (udpNetworkHandler != null) {
                command.put("command", "right");
                udpNetworkHandler.sendData(JSONObject.toJSONString(command));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (udpNetworkHandler != null) {
                command.put("command", "up");
                udpNetworkHandler.sendData(JSONObject.toJSONString(command));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (udpNetworkHandler != null) {
                command.put("command", "down");
                udpNetworkHandler.sendData(JSONObject.toJSONString(command));
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            if (udpNetworkHandler != null) {
                command.put("command", "skill_1");
                udpNetworkHandler.sendData(JSONObject.toJSONString(command));
            }
        }
    }

}
