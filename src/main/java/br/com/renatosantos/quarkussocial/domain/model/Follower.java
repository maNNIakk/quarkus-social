package br.com.renatosantos.quarkussocial.domain.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity(name = "\"Followers\"")
public class Follower {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;
}
