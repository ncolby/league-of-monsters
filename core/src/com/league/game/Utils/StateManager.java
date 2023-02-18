package com.league.game.Utils;

import com.league.game.heroes.Hero;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StateManager {
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
        Map<String, Hero> newPlayersOnClient = new HashMap<>();

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
        long xPos = (long) jsonObjectState.get("xPos");
        long yPos = (long) jsonObjectState.get("yPos");
        boolean isAttacking = (boolean) jsonObjectState.get("isAttacking");
        boolean isMoving = (boolean) jsonObjectState.get("isMoving");
        heroMap.put(playerId, Hero.builder().xPos(xPos).yPos(yPos).isAttacking(isAttacking).isMoving(isMoving).heroId(playerId).build());
    }
}
