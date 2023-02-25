package com.league.game.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AbilityEntity extends GameEntity {
    private long duration;
    private long cooldown;
    private long damage;
}
