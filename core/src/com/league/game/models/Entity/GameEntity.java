package com.league.game.models.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@AllArgsConstructor
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
      spriteBatch.draw(entityImage, xPos, yPos);
   }

   public boolean doEntitiesCollide(GameEntity gameEntity) {
       long otherGameEntityBegin = gameEntity.getXPos();
       long otherGameEntityEnd = gameEntity.getXPos() + gameEntity.getWidth();
       long thisGameEntityBegin = xPos;
       long thisGameEntityEnd = xPos + width;
       if ((thisGameEntityBegin >= otherGameEntityBegin && thisGameEntityBegin <= otherGameEntityEnd) || (
               thisGameEntityEnd >= otherGameEntityBegin && thisGameEntityEnd <= otherGameEntityEnd
               )) {
           return true;
       } else {
           return false;
       }
   }

}
