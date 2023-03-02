package com.league.game.Handlers;

import com.league.game.enums.FacingDirection;
import com.league.game.models.AbilityEntity;
import com.league.game.models.HeroGameEntity;
import com.serializers.SerializableAbilityEntity;
import com.serializers.SerializableGameState;
import com.serializers.SerializableHeroEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UDPStateHandler {
    public static Map<String, HeroGameEntity> replicateServerState(SerializableGameState gameState) {
        Map<String, HeroGameEntity> newPlayersOnClient = new HashMap<String, HeroGameEntity>();
        for (Map.Entry<String, SerializableHeroEntity> entry : gameState.getConnectedPlayers().entrySet()) {
            SerializableHeroEntity serializableHeroEntity = entry.getValue();
            HeroGameEntity heroGameEntity = new HeroGameEntity();
            heroGameEntity.setHeroName(serializableHeroEntity.getHeroName());
            heroGameEntity.setHeroId(serializableHeroEntity.getId());
            heroGameEntity.setXPos(serializableHeroEntity.getXPos());
            heroGameEntity.setYPos(serializableHeroEntity.getYPos());
            heroGameEntity.setMoving(serializableHeroEntity.isMoving());
            heroGameEntity.setAttacking(serializableHeroEntity.isAttacking());
            heroGameEntity.setWidth(serializableHeroEntity.getWidth());
            heroGameEntity.setHeight(serializableHeroEntity.getHeight());
            heroGameEntity.setFacingDirection(getFacingDirection(serializableHeroEntity.getFacingDirection()));
            heroGameEntity.setAbilities(mapAbilityToHero(serializableHeroEntity.getAbilities()));
            newPlayersOnClient.put(serializableHeroEntity.getId(), heroGameEntity);
        }
        return newPlayersOnClient;
    }

    private static FacingDirection getFacingDirection(String facingDirection) {
        return (facingDirection.equals(FacingDirection.LEFT.getDirection()) ?
                FacingDirection.LEFT : (facingDirection.equals(FacingDirection.RIGHT.getDirection())) ?
                FacingDirection.RIGHT : FacingDirection.NONE);
    }

    private static List<AbilityEntity> mapAbilityToHero(List<SerializableAbilityEntity> serializableAbilityEntities) {
        List<AbilityEntity> abilityEntities = new ArrayList<AbilityEntity>();
        for (int i = 0; i < serializableAbilityEntities.size(); i++) {
            AbilityEntity abilityEntity = new AbilityEntity();
            abilityEntity.setAbilityName(serializableAbilityEntities.get(i).getAbilityName());
            abilityEntity.setXPos(serializableAbilityEntities.get(i).getXPos());
            abilityEntity.setYPos(serializableAbilityEntities.get(i).getYPos());
            abilityEntity.setWidth(serializableAbilityEntities.get(i).getWidth());
            abilityEntity.setWidth(serializableAbilityEntities.get(i).getHeight());
            abilityEntity.setDamage((long) serializableAbilityEntities.get(i).getDamage());
            abilityEntities.add(abilityEntity);
        }
        return abilityEntities;
    }
}
