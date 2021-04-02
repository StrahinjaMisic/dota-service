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
public class HealEvent extends AbstractEvent {

    @Id
    @SequenceGenerator(name = "healEventGenerator", sequenceName = "heal_event_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "healEventGenerator")
    private final Long id = null;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "spell_id")
    private Spell spell;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "target_hero_id")
    private Hero targetHero;

    @NotNull
    private Integer amount;

    public HealEvent(MatchHero matchHero, Item item, Spell spell, Hero targetHero, Integer amount, Long timestamp) {
        super(matchHero, timestamp);
        this.item = item;
        this.spell = spell;
        this.targetHero = targetHero;
        this.amount = amount;
    }
}
