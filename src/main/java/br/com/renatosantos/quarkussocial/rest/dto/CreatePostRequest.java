package br.com.renatosantos.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePostRequest {

    @NotBlank(message = "Can't send a empty post")
    @Size(min = 1, max = 150, message = "Posts need to have between 1 and 150" +
            " characters only")
    private String text;
}
