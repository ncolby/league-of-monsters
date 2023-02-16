package com.league.game.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Builder;

@Builder
public class Nexus {
    private long xPos;
    private long yPos;
    private long health;
    private Sprite nexusSprite;

    private Texture texture;
    private TextureRegion textureRegion;

    public void setNexusSprite(String textureName) {
        texture = new Texture(textureName);
        textureRegion = new TextureRegion(texture);
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(textureRegion, xPos, yPos);
    }
}
