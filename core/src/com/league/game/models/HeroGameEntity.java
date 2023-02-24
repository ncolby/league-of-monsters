package com.league.game.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.league.game.enums.FacingDirection;
import lombok.Getter;

@Getter
public class HeroGameEntity extends LivingGameEntity {
    private boolean isAttacking;
    private boolean isMoving;
    private String heroId;
    private FacingDirection facingDirection;
    public HeroGameEntity(long xPos, long yPos, long width, long height, TextureRegion textureRegion, long health, TextureRegion healthBarImage, boolean isAttacking, boolean isMoving, FacingDirection facingDirection, String heroId) {
        super(xPos, yPos, width, height, textureRegion, health, healthBarImage);
        this.isAttacking = isAttacking;
        this.isMoving = isMoving;
        this.facingDirection = facingDirection;
        this.heroId = heroId;
    }

}
