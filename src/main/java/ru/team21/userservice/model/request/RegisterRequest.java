package ru.team21.userservice.model.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


    @Email
    @NotBlank()
    private String email;
    @Size(min = 6, message = "Пароль должен быть не меньше 6 символов")
    private String password;

}
