package com.league.game.Handlers;

import com.league.game.enums.FacingDirection;
import com.league.game.heroes.Hero;
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
    public static Map<String, Hero> replicateServerState(JSONArray playersOnServer) {
        JSONObject playerState;
        String playerId;
        Map<String, Hero> newPlayersOnClient = new HashMap<String, Hero>();

        // Each player would represent the inner JSONArray, and playersOnServer would be the outer JSONArray
        // playersOnServer = [[player1id , player1State], [player2id , player2State]]
        // playerOnServer = [player1d , player1State], it is a JSONArray
        // player1State = { "xPos" : 100L, "yPos" : 100L }, it is a json object
        for (Object playerOnServer : playersOnServer) {
            playerId = (String) ((JSONArray) playerOnServer).get(0);
            playerState = (JSONObject) ((JSONArray) playerOnServer).get(1);
            mapJSONHeroStateToHeroObject(playerId, playerState,newPlayersOnClient);
        }
        return newPlayersOnClient;
    }


    // This maps the JSONObject values to the Hero Object values
    private static void mapJSONHeroStateToHeroObject (String playerId, JSONObject jsonObjectState, Map<String, Hero> heroMap) {
        long xPos = (Long) jsonObjectState.get("xPos");
        long yPos = (Long) jsonObjectState.get("yPos");
        boolean isAttacking = (Boolean) jsonObjectState.get("isAttacking");
        boolean isMoving = (Boolean) jsonObjectState.get("isMoving");
        String direction = (String) jsonObjectState.get("facingDirection");
        FacingDirection facingDirection = FacingDirection.NONE;
        if (direction.equals(FacingDirection.LEFT.getDirection())) {
           facingDirection = FacingDirection.LEFT;
        } else if (direction.equals(FacingDirection.RIGHT.getDirection())){
            facingDirection = FacingDirection.RIGHT;
        }
        heroMap.put(playerId, Hero.builder().xPos(xPos).yPos(yPos).isAttacking(isAttacking).facingDirection(facingDirection).isMoving(isMoving).heroId(playerId).build());

    }
}
