package com.league.game.models;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LivingGameEntity extends GameEntity{
    private long health = 0;
    private TextureRegion healthBarImage;

//    public LivingGameEntity(long xPos, long yPos, long width, long height, TextureRegion entityImage, long health, TextureRegion healthBarImage) {
//       super(xPos, yPos, width, height, entityImage);
//       this.health = health;
//       this.healthBarImage = healthBarImage;
//    }
}
