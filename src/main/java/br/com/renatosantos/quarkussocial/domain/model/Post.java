package br.com.renatosantos.quarkussocial.domain.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "\"Posts\"")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_text")
    private String text;

    @Column(name = "datetime")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
