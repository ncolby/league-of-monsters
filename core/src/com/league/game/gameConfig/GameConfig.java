package com.league.game.gameConfig;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.league.game.models.AbilityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ComponentScan(basePackages = "com.league.game")
public class GameConfig {

    @Bean
    public AssetManager assetManager() {
        AssetManager assetManager = new AssetManager();
        assetManager.load("background.png", Texture.class);
        assetManager.load("pumpkin_idle.png", Texture.class);
        assetManager.load("pumpkin_moving.png", Texture.class);
        assetManager.load("pumpkin_1.png", Texture.class);
        assetManager.load("pumpkin_2.png", Texture.class);
        assetManager.load("reaper_idle.png", Texture.class);
        assetManager.load("reaper_moving.png", Texture.class);
        assetManager.load("reaper_1.png", Texture.class);
        assetManager.load("reaper_2.png", Texture.class);
        assetManager.finishLoading();
        return assetManager;
    }

    @Bean
    @Autowired
    public Map<String, List<AbilityEntity>> abilityEntityMap(AssetManager assetManager) {
        List<AbilityEntity> abilityEntityList = new ArrayList<AbilityEntity>();
        Map<String, List<AbilityEntity>> abilityEntityMap = new HashMap<String, List<AbilityEntity>>();
        AbilityEntity abilityEntity = new AbilityEntity();
        TextureRegion abilityTextureRegion = new TextureRegion(assetManager.get("pumpkin_1.png", Texture.class));
        abilityEntity.setEntityImage(abilityTextureRegion);
        abilityEntity.setAbilityName("pumpkin_1");
        abilityEntityList.add(abilityEntity);
        abilityEntityMap.put("pumpkin" , abilityEntityList);
        return abilityEntityMap;
    }
}
