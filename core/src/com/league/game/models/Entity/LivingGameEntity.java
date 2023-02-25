package com.league.game.models.Entity;


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
}
