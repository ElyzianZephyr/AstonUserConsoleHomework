package org.userservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Запрос на создание или обновление пользователя")
public record UserRequest(

        @Schema(description = "Имя пользователя", example = "Иван Иванов", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Имя пользователя не может быть пустым")
        @Size(max = 100, message = "Имя пользователя не может быть длиннее 100 символов")
        String name,

        @Schema(description = "Возраст пользователя", example = "25")
        @Min(value = 1, message = "Возраст должен быть больше 0")
        @Max(value = 150, message = "Возраст не может быть больше 150 лет")
        Integer age,

        @Schema(description = "Email пользователя", example = "ivan@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Email не может быть пустым")
        @Email(message = "Email должен иметь корректный формат и содержать '@'")
        @Size(max = 150, message = "Email не может быть длиннее 150 символов")
        String email
) {
}