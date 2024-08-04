package br.com.renatosantos.quarkussocial.rest.dto;

import br.com.renatosantos.quarkussocial.domain.model.Follower;
import jakarta.validation.Valid;
import lombok.Data;

@Data
public class FollowerResponse {

    private Long id;
    private String name;

    public FollowerResponse() {

    }

    public FollowerResponse(@Valid Follower follower) {
         this(follower.getId(), follower.getFollower().getName());
    }

    public FollowerResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
