package com.league.game.models;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class GameEntity {
   private long xPos;
   private long yPos;
   private long width;
   private long height;
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
