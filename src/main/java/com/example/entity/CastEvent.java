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
public class CastEvent extends AbstractEvent {

    @Id
    @SequenceGenerator(name = "castEventGenerator", sequenceName = "cast_event_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "castEventGenerator")
    private final Long id = null;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "spell_id")
    private Spell spell;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "target_hero_id")
    private Hero targetHero;

    @NotNull
    private Integer level;

    public CastEvent(MatchHero matchHero, Spell spell, Hero targetHero, Integer level, Long timestamp) {
        super(matchHero, timestamp);
        this.spell = spell;
        this.targetHero = targetHero;
        this.level = level;
    }
}
