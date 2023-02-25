package com.league.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.league.game.LeagueOfHorrors;
import com.league.game.Handlers.InputHandler;
import com.league.game.models.HeroGameEntity;
import com.league.game.utils.ImageProcessor;

import java.util.Map;

public class GameRenderScreen extends ScreenAdapter {

    private static final String BACKGROUND_IMAGE_NAME = "gamemap.png";
    private LeagueOfHorrors gameManager;
    private Viewport viewport;
    private final Camera playerCamera = new OrthographicCamera();
    private TextureRegion backgroundTextureRegion;
    private float animationDuration;
//    private TextureRegion abilityFrame;
    private TextureRegion currentFrame;
    private Animation<TextureRegion> heroMovementAnimation;
    private Animation<TextureRegion> heroAttackAnimation;
    private Animation<TextureRegion> heroAbilityAnimation;
    private TextureRegion heroIdleAnimation;


    public GameRenderScreen(LeagueOfHorrors gameManager) {
        this.gameManager = gameManager;
        backgroundTextureRegion = new TextureRegion(gameManager.assetManager.get(BACKGROUND_IMAGE_NAME, Texture.class));
        viewport = new FitViewport(gameManager.VIEW_PORT_WIDTH, gameManager.VIEW_PORT_HEIGHT, playerCamera);
    }

    @Override
    public void show() {
        playerCamera.position.set(new Vector3(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0));
        heroMovementAnimation = ImageProcessor.getImageAnimation("pumpkin_head_walk.png");
        heroAttackAnimation = ImageProcessor.getImageAnimation("pumpkin_head_attack.png");
        heroIdleAnimation = ImageProcessor.getImageStill("pumpkin_head_walk.png");
        heroAbilityAnimation = ImageProcessor.getImageAnimation("pumpkin_head_vines.png");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 0);
        gameManager.spriteBatch.setProjectionMatrix(playerCamera.combined);
        InputHandler.handleInput(gameManager.networkHandler.getSocket());
        gameManager.networkHandler.getSocket().emit("getState");
        gameManager.getSpriteBatch().begin();
        gameManager.getSpriteBatch().draw(backgroundTextureRegion, 0, 0);
        renderHeroes();
        gameManager.getSpriteBatch().end();
    }

    @Override
    public void resize(int width, int height) {
       viewport.update(width, height);
    }

    @Override
    public void hide() {
    }

    private void renderHeroes() {
        animationDuration += Gdx.graphics.getDeltaTime();
        for (Map.Entry<String, HeroGameEntity> hero : gameManager.heroes.entrySet()) {
            if (hero.getKey().equals(gameManager.networkHandler.getSocket().id())) {
                playerCamera.position.x = hero.getValue().getXPos();
                playerCamera.update();
            }
            if (hero.getValue().isMoving()) {
                currentFrame = heroMovementAnimation.getKeyFrame(animationDuration, true);
            } else if (hero.getValue().isAttacking()) {
                currentFrame = heroAttackAnimation.getKeyFrame(animationDuration / 2, true);
//                abilityFrame = heroAbilityAnimation.getKeyFrame(animationDuration / 2, true);
            } else {
                currentFrame = heroIdleAnimation;
//                abilityFrame = null;
            }
            hero.getValue().setEntityImage(currentFrame);
            hero.getValue().draw(gameManager.spriteBatch);
//          if (abilityFrame != null && hero.getValue().getFacingDirection().equals(FacingDirection.LEFT)) {
//				hero.getValue().drawAbility(spriteBatch, abilityFrame, hero.getValue().getXPos() - 200, hero.getValue().getYPos());
//			} else if ( abilityFrame != null && hero.getValue().getFacingDirection().equals(FacingDirection.RIGHT)) {
//				hero.getValue().drawAbility(spriteBatch, abilityFrame, hero.getValue().getXPos() + 100, hero.getValue().getYPos());
//			}
        }
    }

}
