package com.league.game.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AbilityEntity extends GameEntity {

    private float cooldown;
    private float duration;
    @Builder
    public AbilityEntity(long xPos, long yPos, long width, long height, TextureRegion entityImage, float cooldown, float duration) {
        super(xPos, yPos, width, height, entityImage);
        this.cooldown = cooldown;
        this.duration = duration;
    }
}
