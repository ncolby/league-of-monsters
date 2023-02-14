package com.league.game.heroes;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lombok.Builder;
import lombok.Getter;
import org.json.simple.JSONObject;

@Builder
@Getter
public class Hero {
    private long xPos;
    private long yPos;
    private int size;

    private String heroId;

    public void update(JSONObject gameState) {
        xPos = (long) gameState.get("xPos");
        yPos = (long) gameState.get("yPos");
    }

    public void draw(SpriteBatch spriteBatch, TextureRegion movementFrame) {
        spriteBatch.draw(movementFrame, xPos, yPos);
    }

}
