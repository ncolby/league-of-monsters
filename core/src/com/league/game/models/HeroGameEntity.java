package com.league.game.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.league.game.enums.FacingDirection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HeroGameEntity extends LivingGameEntity {
    private boolean isAttacking = false;
    private boolean isMoving = false;
    private String heroId;

    private String heroName;
    private FacingDirection facingDirection = FacingDirection.NONE;
    private List<AbilityEntity> abilities = new ArrayList<AbilityEntity>();
}
