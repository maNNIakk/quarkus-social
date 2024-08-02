package br.com.renatosantos.quarkussocial.rest.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only " +
            "alphabetic characters")
    private String name;

    @Min(value = 0, message = "Age must be between 0 and 130")
    @Max(value = 130, message = "Age must be between 0 and 130")
    @NotNull(message = "Age is required")
    private Integer age;
}
