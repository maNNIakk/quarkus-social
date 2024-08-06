package br.com.renatosantos.quarkussocial.domain.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

@Data
@JsonPropertyOrder({"id","name","age"})
@Entity(name = "\"Users\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(name = "name")
    private String name;
     @Column(name = "age")
    private Integer age;

}
