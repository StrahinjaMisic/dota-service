package com.example.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class MatchHero {

    @Id
    @SequenceGenerator(name = "matchHeroGenerator", sequenceName = "match_hero_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "matchHeroGenerator")
    private final Long id = null;

    @ManyToOne
    @JoinColumn(name = "match_id")
    @JsonBackReference
    private Match match;

    @ManyToOne
    @JoinColumn(name = "hero_id")
    private Hero hero;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "matchHero", cascade = CascadeType.MERGE)
    private Set<BuyEvent> buyEvents = new HashSet<>();

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "matchHero", cascade = CascadeType.MERGE)
    private Set<UseEvent> useEvents = new HashSet<>();

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "matchHero", cascade = CascadeType.MERGE)
    private Set<HitEvent> hitEvents = new HashSet<>();

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "matchHero", cascade = CascadeType.MERGE)
    private Set<KillEvent> killEvents = new HashSet<>();

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "matchHero", cascade = CascadeType.MERGE)
    private Set<DeathEvent> deathEvents = new HashSet<>();

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "matchHero", cascade = CascadeType.MERGE)
    private Set<CastEvent> castEvents = new HashSet<>();

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "matchHero", cascade = CascadeType.MERGE)
    private Set<HealEvent> healEvents = new HashSet<>();

    public MatchHero(Match match, Hero hero) {
        this.match = match;
        this.hero = hero;
    }

    public void addBuyEvent(BuyEvent buyEvent) {
        getBuyEvents().add(buyEvent);
    }

    public void addUseEvent(UseEvent useEvent) {
        getUseEvents().add(useEvent);
    }

    public void addHitEvent(HitEvent hitEvent) {
        getHitEvents().add(hitEvent);
    }

    public void addKillsEvent(KillEvent killEvent) {
        getKillEvents().add(killEvent);
    }

    public void addDeathEvent(DeathEvent deathEvent) {
        getDeathEvents().add(deathEvent);
    }

    public void addCastEvent(CastEvent castEvent) {
        getCastEvents().add(castEvent);
    }

    public void addHealEvent(HealEvent healEvent) {
        getHealEvents().add(healEvent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MatchHero matchHero = (MatchHero) o;
        return Objects.equals(getMatch(), matchHero.getMatch()) &&
                Objects.equals(getHero(), matchHero.getHero());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMatch(), getHero());
    }
}
