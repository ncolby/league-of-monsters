package com.league.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.league.game.Handlers.UDPCreationHandler;
import com.league.game.Handlers.UDPInputHandler;
import com.league.game.LeagueOfHorrors;

public class DummyScreen extends ScreenAdapter {

    private LeagueOfHorrors gameManager;

    public DummyScreen(LeagueOfHorrors gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void show(){
        UDPCreationHandler.handleCreation(gameManager.udpNetworkHandler);
    }

    @Override
    public void render(float delta) {
        UDPInputHandler.handleInput(gameManager.udpNetworkHandler);
    }

    @Override
    public void hide(){}
}
