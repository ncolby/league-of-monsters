package com.league.game.Handlers;

import com.league.game.enums.FacingDirection;
import com.league.game.models.HeroGameEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StateHandler {
    /**
     * PlayersOnServer is an JSONArray of JSONArrays
     * playersOnServer Structure:
     *      [[player1id , player1State], [player2id , player2State], ... [playerNid , playerNState]]
     * JSONObject is the playerNState
     * This is because we can't serialize node objects to become Java Maps, it would be easier if the server were Java
     * That way we can just send the object over and not need to copy anything
     * **/
    public static Map<String, HeroGameEntity> replicateServerState(JSONArray playersOnServer) {
        JSONObject playerState;
        String playerId;
        Map<String, HeroGameEntity> newPlayersOnClient = new HashMap<String, HeroGameEntity>();

        /**
         Each player would represent the inner JSONArray, and playersOnServer would be the outer JSONArray
         playersOnServer = [[player1id , player1State], [player2id , player2State]]
         playerOnServer = [player1d , player1State], it is a JSONArray
         player1State = { "xPos" : 100L, "yPos" : 100L }, it is a json object
        **/
        for (Object playerOnServer : playersOnServer) {
            playerId = (String) ((JSONArray) playerOnServer).get(0);
            playerState = (JSONObject) ((JSONArray) playerOnServer).get(1);
            mapJSONHeroStateToHeroObject(playerId, playerState, newPlayersOnClient);
        }
        return newPlayersOnClient;
    }


//    This maps the JSONObject values to the Hero Object values
    private static void mapJSONHeroStateToHeroObject (String playerId, JSONObject jsonObjectState, Map<String, HeroGameEntity> heroMap) {
        HeroGameEntity heroGameEntity = new HeroGameEntity();
        String direction = (String) jsonObjectState.get("facingDirection");
        System.out.println(jsonObjectState.toJSONString());
        heroGameEntity.setHeroId(playerId);
        heroGameEntity.setAttacking((Boolean) jsonObjectState.get("isAttacking"));
        heroGameEntity.setFacingDirection(getFacingDirection(direction));
        heroGameEntity.setXPos((Long) jsonObjectState.get("xPos"));
        heroGameEntity.setYPos((Long) jsonObjectState.get("yPos"));
        heroGameEntity.setMoving((Boolean) jsonObjectState.get("isMoving"));
        heroGameEntity.setHeroName((String) jsonObjectState.get("heroName"));
        heroGameEntity.setWidth(0);
        heroGameEntity.setHeight(0);
        heroGameEntity.setHealth(0);
        heroMap.put(playerId, heroGameEntity);
    }

    private static FacingDirection getFacingDirection(String facingDirection) {
       return (facingDirection.equals(FacingDirection.LEFT.getDirection()) ?
               FacingDirection.LEFT : (facingDirection.equals(FacingDirection.RIGHT.getDirection())) ?
               FacingDirection.RIGHT : FacingDirection.NONE);
    }
}
