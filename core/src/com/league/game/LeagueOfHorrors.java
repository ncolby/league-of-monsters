package com.league.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.league.game.Handlers.NetworkHandler;
import com.league.game.models.Entity.AbilityEntity;
import com.league.game.models.Entity.HeroGameEntity;
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
    public Map<String, List<AbilityEntity>> abilityEntityMap;

    @Override
    public void create() {
        SpringApplication.run(LeagueOfHorrors.class);
        spriteBatch = new SpriteBatch();
		ApplicationContext ctx = new AnnotationConfigApplicationContext(LeagueOfHorrors.class);
        assetManager = (AssetManager) ctx.getBean("assetManager");
        abilityEntityMap = loadAbilities(assetManager);
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

    private static Map<String, List<AbilityEntity>> loadAbilities(AssetManager assetManager) {
        List<AbilityEntity> abilityEntityList = new ArrayList<AbilityEntity>();
        Map<String, List<AbilityEntity>> abilityEntityMap = new HashMap<String, List<AbilityEntity>>();
        AbilityEntity abilityEntity = new AbilityEntity();
        TextureRegion abilityTextureRegion = new TextureRegion(assetManager.get("pumpkin_head_vines.png", Texture.class));
        abilityEntity.setEntityImage(abilityTextureRegion);
        abilityEntityList.add(abilityEntity);
        abilityEntityMap.put("pumpkin_head" , abilityEntityList);
        return abilityEntityMap;
    }
}
