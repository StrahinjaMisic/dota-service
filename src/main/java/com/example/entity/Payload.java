package com.example.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Payload {

    @Id
    @SequenceGenerator(name = "payloadGenerator", sequenceName = "payload_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payloadGenerator")
    private final Long id = null;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id")
    private Match match;

    @NotBlank
    private String text;

    public Payload(Match match, String text) {
        this.match = match;
        this.text = text;
    }
}
