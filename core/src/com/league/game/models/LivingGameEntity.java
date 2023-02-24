package com.league.game.models;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;

@Getter
public class LivingGameEntity extends GameEntity{
    private long health;

    private TextureRegion healthBarImage;

    public LivingGameEntity(long xPos, long yPos, long width, long height, TextureRegion entityImage, long health, TextureRegion healthBarImage) {
       super(xPos, yPos, width, height, entityImage);
       this.health = health;
       this.healthBarImage = healthBarImage;
    }
}
