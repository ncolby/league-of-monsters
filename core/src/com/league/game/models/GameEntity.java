package com.league.game.models;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class GameEntity {
   private long xPos = 0;
   private long yPos = 0;
   private long width = 0;
   private long height = 0;
   private TextureRegion entityImage;

   public void draw(SpriteBatch spriteBatch) {
      spriteBatch.draw(this.getEntityImage(), this.getXPos(), this.getYPos(), this.getWidth(), this.getHeight());
   }
}
