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
public class UseEvent extends AbstractEvent {

    @Id
    @SequenceGenerator(name = "useEventGenerator", sequenceName = "use_event_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "useEventGenerator")
    private final Long id = null;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    public UseEvent(MatchHero matchHero, Item item, Long timestamp) {
        super(matchHero, timestamp);
        this.item = item;
    }
}
