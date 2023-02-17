package com.league.game.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.socket.client.Socket;


public class InputHandler {

    public static void handleInput(Socket socket) {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (socket != null) {
                socket.emit("command", "left");
                socket.emit("getState");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (socket != null) {
                socket.emit("command", "right");
                socket.emit("getState");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (socket != null) {
                socket.emit("command", "up");
                socket.emit("getState");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            if (socket != null) {
                socket.emit("command", "down");
                socket.emit("getState");
            }
        }
    }

}
