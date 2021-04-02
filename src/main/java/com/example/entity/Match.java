package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Match {

    @Id
    @NotNull
    private Long id;

    @NotNull
    @OneToOne(mappedBy = "match", cascade = CascadeType.MERGE)
    private Payload payload;

    private Long duration;

    @NotNull
    private Boolean isIngested = Boolean.FALSE;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "match", cascade = CascadeType.MERGE)
    private Set<MatchHero> matchHeroes = new HashSet<>();

    public Match(Long id, String payload) {
        this.id = id;
        this.payload = new Payload(this, payload);
    }

    @PrePersist
    public void prePerist() {
        setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
    }

    @PreUpdate
    public void preUpdate() { setUpdatedAt(LocalDateTime.now(ZoneOffset.UTC)); }

    public MatchHero addMatchHero(Hero hero) {
        MatchHero matchHero = getMatchHeroes().stream().filter(heroInMatch -> hero.equals(heroInMatch.getHero()))
                .findFirst().orElse(null);
        if (Objects.isNull(matchHero)) {
            matchHero = new MatchHero(this, hero);
            getMatchHeroes().add(matchHero);
        }
        return matchHero;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Match match = (Match) o;
        return Objects.equals(getId(), match.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
