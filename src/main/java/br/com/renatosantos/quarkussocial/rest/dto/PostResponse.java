package br.com.renatosantos.quarkussocial.rest.dto;

import br.com.renatosantos.quarkussocial.domain.model.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private Long id;
private String text;
private LocalDateTime dateTime;

public PostResponse(Post post) {
    this.id = post.getId();
    this.text = post.getText();
    this.dateTime = post.getDateTime();
}

public static PostResponse fromEntity(Post post){
    return new PostResponse(post);
}
}
