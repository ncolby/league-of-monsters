package com.league.game.Handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import io.socket.client.Socket;


public class InputHandler {

    public static void handleInput(Socket socket) {
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
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            if (socket != null) {
                socket.emit("command", "skill_1");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            if (socket != null) {
                socket.emit("command", "skill_2");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            if (socket != null) {
                socket.emit("command", "skill_3");
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            if (socket != null) {
                socket.emit("command", "skill_4");
            }
        }
    }
}
