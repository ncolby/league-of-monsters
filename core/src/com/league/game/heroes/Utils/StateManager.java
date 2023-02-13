package com.league.game.heroes.Utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Map;

public class StateManager {
    public static void updateStateOfAllHeroes(JSONArray connectedPlayers, Map<String, JSONObject> heroes) {
        JSONArray heroReference;
        JSONObject heroState;
        for (Object hero : connectedPlayers) {
            heroReference = (JSONArray) hero;
            heroState = (JSONObject) heroReference.get(1);
            if (heroes.containsKey(heroReference.get(0))) {
               heroes.get(heroReference.get(0)).put("xPos", heroState.get("xPos"));
               heroes.get(heroReference.get(0)).put("yPos", heroState.get("yPos"));
            } else {
                heroes.put((String) heroReference.get(0), heroState);
            }
        }

    }
}
