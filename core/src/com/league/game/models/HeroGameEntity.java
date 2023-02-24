package com.league.game.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.league.game.enums.FacingDirection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HeroGameEntity extends LivingGameEntity {
    private boolean isAttacking = false;
    private boolean isMoving = false;
    private String heroId;
    private FacingDirection facingDirection = FacingDirection.NONE;
//    public HeroGameEntity(long xPos, long yPos, long width, long height, TextureRegion textureRegion, long health, TextureRegion healthBarImage, boolean isAttacking, boolean isMoving, FacingDirection facingDirection, String heroId) {
//        super(xPos, yPos, width, height, textureRegion, health, healthBarImage);
//        this.isAttacking = isAttacking;
//        this.isMoving = isMoving;
//        this.facingDirection = facingDirection;
//        this.heroId = heroId;
//    }

}
