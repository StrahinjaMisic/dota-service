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
public class KillEvent extends AbstractEvent {

    @Id
    @SequenceGenerator(name = "killEventGenerator", sequenceName = "kill_event_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "killEventGenerator")
    private final Long id = null;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "target_hero_id")
    private Hero targetHero;

    public KillEvent(MatchHero matchHero, Hero targetHero, Long timestamp) {
        super(matchHero, timestamp);
        this.targetHero = targetHero;
    }
}
