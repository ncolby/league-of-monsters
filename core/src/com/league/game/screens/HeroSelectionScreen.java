package com.league.game.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.league.game.LeagueOfHorrors;

public class HeroSelectionScreen extends ScreenAdapter {
   public static final int REAPER_X_POS = 300;
   public static final int PUMPKIN_X_POS = 100;
   private LeagueOfHorrors gameManager;
   private Stage stage;

   public HeroSelectionScreen(LeagueOfHorrors gameManager) {
      this.gameManager = gameManager;
   }

   @Override
   public void show() {
      stage = new Stage(new ScreenViewport());
      Drawable reaperImage = new TextureRegionDrawable(new TextureRegion(gameManager.assetManager.get("reaper_selection.png", Texture.class)));
      Drawable pumpkinImage = new TextureRegionDrawable(new TextureRegion(gameManager.assetManager.get("pumpkin_selection.png", Texture.class)));
      final ImageButton pumpkinSelector = new ImageButton(pumpkinImage);
      final ImageButton reaperSelector = new ImageButton(reaperImage);
      reaperSelector.setSize(100, 100);
      reaperSelector.setPosition(REAPER_X_POS, 100);
      pumpkinSelector.setSize(100, 100);
      pumpkinSelector.setPosition(PUMPKIN_X_POS, 100);

      reaperSelector.addListener(new InputListener() {
         @Override
         public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            reaperSelector.setPosition(REAPER_X_POS, 100);
            gameManager.selectedHeroName = "reaper";
            gameManager.setScreen(new LoadingScreen(gameManager));
         }
         @Override
         public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            reaperSelector.setPosition(REAPER_X_POS, 150);
            return true;
         }
      });
      pumpkinSelector.addListener(new InputListener() {
         @Override
         public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            pumpkinSelector.setPosition(PUMPKIN_X_POS, 100);
            gameManager.selectedHeroName = "pumpkin";
            gameManager.setScreen(new LoadingScreen(gameManager));
         }
         @Override
         public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            pumpkinSelector.setPosition(PUMPKIN_X_POS, 150);
            return true;
         }
      });
      stage.addActor(pumpkinSelector);
      stage.addActor(reaperSelector);
      Gdx.input.setInputProcessor(stage);
   }

   @Override
   public void render(float delta) {
      ScreenUtils.clear(0, 0, 0, 0);
      stage.act();
      stage.draw();
   }

   @Override
   public void hide() {
      Gdx.input.setInputProcessor(null);
      stage.dispose();
   }

}
