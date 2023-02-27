package com.league.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Component
@Data
public class ImageProcessor {
    public static Animation<TextureRegion> getImageAnimation(String pathToImage, AssetManager assetManager) {
        return (new Animation<TextureRegion>(0.15f, getTextureRegion(pathToImage, assetManager)));
    }

    private static TextureRegion[] getTextureRegion(String pathToImage, AssetManager assetManager) {
        TextureRegion[] movementFrames;
        int spriteCols = 4;
        int spriteRows = 2;
        int frameCount = 0;
        TextureRegion[][] movementSpriteSheetSplits = TextureRegion.
                split(assetManager.get(pathToImage, Texture.class), assetManager.get(pathToImage, Texture.class).getWidth() / spriteCols
                        , assetManager.get(pathToImage, Texture.class).getHeight() / spriteRows);
        movementFrames = new TextureRegion[spriteCols * spriteRows];
        for (int i = 0; i < spriteRows; i++) {
            for (int j = 0; j < spriteCols; j++) {
                movementFrames[frameCount++] = movementSpriteSheetSplits[i][j];
            }
        }
        return movementFrames;
    }
}
