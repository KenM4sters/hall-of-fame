package com.characterselection.characterselection.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "character")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class Character {
    @Id
    @UuidGenerator
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    private String name;
    private String imageUrl;
    private String game;
}
