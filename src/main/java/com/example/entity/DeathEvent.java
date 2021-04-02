package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class DeathEvent extends AbstractEvent {

    @Id
    @SequenceGenerator(name = "deathEventGenerator", sequenceName = "death_event_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deathEventGenerator")
    private final Long id = null;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "attacker_hero_id")
    private Hero attackerHero;

    public DeathEvent(MatchHero matchHero, Hero attackerHero, Long timestamp) {
        super(matchHero, timestamp);
        this.attackerHero = attackerHero;
    }
}
