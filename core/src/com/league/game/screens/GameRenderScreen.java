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
import com.league.game.enums.FacingDirection;
import com.league.game.models.HeroGameEntity;
import com.league.game.utils.ImageProcessor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class GameRenderScreen extends ScreenAdapter {
    private static final String BACKGROUND_IMAGE_NAME = "background.png";
    private TextureRegion abilityFrame;
    private LeagueOfHorrors gameManager;
    private Viewport viewport;
    private final Camera playerCamera = new OrthographicCamera();
    private TextureRegion backgroundTextureRegion;
    private float animationDuration;
    private TextureRegion currentFrame;
    private Animation<TextureRegion> heroAttackAnimation;
    private Animation<TextureRegion> heroAbilityAnimation;
    private Animation<TextureRegion> heroIdleAnimation;


    public GameRenderScreen(LeagueOfHorrors gameManager) {
        this.gameManager = gameManager;
        backgroundTextureRegion = new TextureRegion(gameManager.assetManager.get(BACKGROUND_IMAGE_NAME, Texture.class));
        viewport = new FitViewport(gameManager.VIEW_PORT_WIDTH, gameManager.VIEW_PORT_HEIGHT, playerCamera);
    }

    @Override
    public void show() {
        playerCamera.position.set(new Vector3(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2, 0));
        heroAttackAnimation = ImageProcessor.getImageAnimation("pumpkin_1.png");
        heroIdleAnimation = ImageProcessor.getImageAnimation("pumpkin_idle.png");
        heroAbilityAnimation = ImageProcessor.getImageAnimation("pumpkin_2.png");
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
                currentFrame = gameManager.animationMap.get(hero.getValue().getHeroName()).get(hero.getValue().getHeroName() + "_moving.png").getKeyFrame(animationDuration / 2, true);
            } else if (hero.getValue().isAttacking()) {
                currentFrame = gameManager.animationMap.get(hero.getValue().getHeroName()).get(hero.getValue().getHeroName() + "_idle.png").getKeyFrame(animationDuration / 2, true);
//                abilityFrame = gameManager.animationMap.get(hero.getValue().getHeroName()).get(hero.getValue().getHeroName() + "_1.png").getKeyFrame(animationDuration / 2, true);
//                currentFrame = heroAttackAnimation.getKeyFrame(animationDuration / 2, true);
//                abilityFrame = hero.getValue().getAbilities().get(0).getEntityImage();
            } else {
                currentFrame = gameManager.animationMap.get(hero.getValue().getHeroName()).get(hero.getValue().getHeroName() + "_idle.png").getKeyFrame(animationDuration / 2, true);
                abilityFrame = null;
//                currentFrame = heroIdleAnimation.getKeyFrame(animationDuration, true);
            }
            hero.getValue().setEntityImage(currentFrame);
            hero.getValue().draw(gameManager.spriteBatch);
            if (abilityFrame != null && hero.getValue().getFacingDirection().equals(FacingDirection.LEFT)) {
                hero.getValue().getAbilities().get(0).draw(gameManager.spriteBatch);
            } else if ( abilityFrame != null && hero.getValue().getFacingDirection().equals(FacingDirection.RIGHT)) {
                hero.getValue().getAbilities().get(0).draw(gameManager.spriteBatch);
            }
        }
    }

}
