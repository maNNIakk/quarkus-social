package br.com.renatosantos.quarkussocial.domain.model;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonbPropertyOrder({"id","dateTime","text","user"})
@Entity(name = "\"Posts\"")
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

    @PrePersist
    public void prePersist(){
        setDateTime(LocalDateTime.now().withNano(0));
    }
}
