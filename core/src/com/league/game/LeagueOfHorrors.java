package com.league.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.league.game.Handlers.NetworkHandler;
import com.league.game.models.AbilityEntity;
import com.league.game.models.HeroGameEntity;
import com.league.game.screens.GameRenderScreen;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@SpringBootApplication()
public class LeagueOfHorrors extends Game {

    public final int VIEW_PORT_WIDTH = 1000;
    public final int VIEW_PORT_HEIGHT = 500;
    public SpriteBatch spriteBatch;
    public NetworkHandler networkHandler;
    public AssetManager assetManager;
    public Map<String, HeroGameEntity> heroes;
    public List<AbilityEntity> abilityEntityList;

    @Override
    public void create() {
        SpringApplication.run(LeagueOfHorrors.class);
        spriteBatch = new SpriteBatch();
		ApplicationContext ctx = new AnnotationConfigApplicationContext(LeagueOfHorrors.class);
        assetManager = (AssetManager) ctx.getBean("assetManager");
//        assetManager = new AssetManager();
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

    private static List<AbilityEntity> loadAbilities(AssetManager assetManager) {
        List<AbilityEntity> abilityList = new ArrayList<AbilityEntity>();
//        AbilityEntity ability = new AbilityEntity();
//        assetManager.load("pumpkin_head_vines.png", Texture.class);
//        assetManager.finishLoading();
//        TextureRegion abilityTextureRegion = new TextureRegion(assetManager.get("pumpkin_head_vines.pgn", Texture.class));
//        abilityList.add(ability);
        return abilityList;
    }
}
