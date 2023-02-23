package com.league.game.heroes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.league.game.enums.FacingDirection;
import lombok.Builder;
import lombok.Getter;
import org.json.simple.JSONObject;

@Builder
@Getter
public class Hero {
    private long xPos;
    private long yPos;
    private String heroId;
    private boolean isAttacking;
    private boolean isMoving;

    private FacingDirection facingDirection;

    public void draw(SpriteBatch spriteBatch, TextureRegion movementFrame) {
        spriteBatch.draw(movementFrame, xPos, yPos);
    }

    public void drawAbility(SpriteBatch spriteBatch, TextureRegion abilityFrame, long abilityXpos, long abilityYpos) {
        spriteBatch.draw(abilityFrame, abilityXpos, abilityYpos);
    }

}
