package com.league.game.gameConfig;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.league.game")
public class GameConfig {

    @Bean
    public AssetManager assetManager() {
        AssetManager assetManager = new AssetManager();
        assetManager.load("pumpkin_head_walk.png", Texture.class);
        assetManager.load("pumpkin_head_vines.png", Texture.class);
        assetManager.load("pumpkin_head_attack.png", Texture.class);
        assetManager.load("nexus.png", Texture.class);
        assetManager.load("gamemap.png", Texture.class);
        assetManager.finishLoading();
        return assetManager;
    }
}
