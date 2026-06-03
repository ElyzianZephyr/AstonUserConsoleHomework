package org.userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(

        @NotBlank(message = "Имя пользователя не может быть пустым")
        @Size(max = 100, message = "Имя пользователя не может быть длиннее 100 символов")
        String name,

        @Min(value = 1, message = "Возраст должен быть больше 0")
        @Max(value = 150, message = "Возраст не может быть больше 150 лет")
        Integer age,

        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Email должен иметь корректный формат и содержать '@'")
        @Size(max = 150, message = "Email не может быть длиннее 150 символов")
        String email
) {
}