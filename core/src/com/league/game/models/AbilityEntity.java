package com.league.game.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AbilityEntity extends GameEntity {

    private float cooldown = 0;
    private float duration = 0;
//    public AbilityEntity(long xPos, long yPos, long width, long height, TextureRegion entityImage, float cooldown, float duration) {
//        super(xPos, yPos, width, height, entityImage);
//        this.cooldown = cooldown;
//        this.duration = duration;
//    }
}
