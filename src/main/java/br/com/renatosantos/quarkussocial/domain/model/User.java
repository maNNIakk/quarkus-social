package br.com.renatosantos.quarkussocial.domain.model;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.persistence.*;
import lombok.*;

@Data
@JsonbPropertyOrder({"id","name","age"})
@Entity(name = "\"User\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(name = "name")
    private String name;
     @Column(name = "age")
    private Integer age;

}
