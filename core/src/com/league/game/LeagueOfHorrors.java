package com.league.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.league.game.Handlers.NetworkHandler;
import com.league.game.models.HeroGameEntity;
import com.league.game.screens.GameRenderScreen;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Getter
@Slf4j
public class LeagueOfHorrors extends Game {
    public final int VIEW_PORT_WIDTH = 1000;
    public final int VIEW_PORT_HEIGHT = 500;
    public SpriteBatch spriteBatch;

    public NetworkHandler networkHandler;
    public AssetManager assetManager;
    public Map<String, HeroGameEntity> heroes;
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        assetManager = new AssetManager();
        heroes = new HashMap<String, HeroGameEntity>();
        networkHandler = new NetworkHandler(this);
        networkHandler.getAndConfigureSocket();
        if (networkHandler.getSocket() != null) {
            setScreen(new GameRenderScreen(this));
        } else {
            log.error("Enable to create a socket.");
        }
    }

    @Override
    public void dispose() {
       spriteBatch.dispose();
       assetManager.dispose();
    }
}
