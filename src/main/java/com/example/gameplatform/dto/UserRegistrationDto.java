package com.example.gameplatform.dto;

import jakarta.validation.constraints.*;

public class UserRegistrationDto {
    @NotBlank(message = "Имя необходимо")
    public String name;

    @NotBlank(message = "Email необходим")
    @Email(message = "Неправильный email")
    public String email;

    @NotBlank(message = "Пароль необходим")
    @Size(min = 6, message = "Пароль должен быть минимум 6ти значный")
    public String password;

    @Min(value = 12, message = "Возраст должен быть больше 12")
    @Max(value = 100, message = "Возраст должен быть меньше 100")
    public int age;
}
