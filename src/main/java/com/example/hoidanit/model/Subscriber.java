package com.example.hoidanit.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "subscribers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;

    @JsonIgnoreProperties("subscribers")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "subscriber_skill", joinColumns = @JoinColumn(name = "subscriber_id"),
                inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills;
}
