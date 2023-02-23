package com.league.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ImageProcessor {
    public static Animation<TextureRegion> getImageAnimation(String pathToImage) {
        return (new Animation<TextureRegion>(0.15f, getTextureRegion(pathToImage)));
    }

    public static TextureRegion getImageStill(String pathToImage) {
        return getTextureRegion(pathToImage)[4];
    }

    // TODO: handle garbage cleanup of the TextureRegion, use dispose
    private static TextureRegion[] getTextureRegion(String pathToImage) {
        TextureRegion[] movementFrames;
        int spriteCols = 4;
        int spriteRows = 2;
        int frameCount = 0;
        Texture movementSpriteSheet = new Texture(Gdx.files.internal(pathToImage));
        TextureRegion[][] movementSpriteSheetSplits = TextureRegion.
                split(movementSpriteSheet, movementSpriteSheet.getWidth() / spriteCols
                        , movementSpriteSheet.getHeight() / spriteRows);
        movementFrames = new TextureRegion[spriteCols * spriteRows];
        for (int i = 0; i < spriteRows; i++) {
            for (int j = 0; j < spriteCols; j++) {
                movementFrames[frameCount++] = movementSpriteSheetSplits[i][j];
            }
        }
        return movementFrames;
    }
}
