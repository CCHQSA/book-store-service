package com.epam.rd.autocode.spring.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDtoRequest {

    @NotBlank(message = "Name is required.")
    @Size(min = 2, max = 50, message = "Name must be between {min} and {max} characters")
    private String name;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d\\s]).+$",
            message = "Use at least one uppercase letter, one lowercase letter, one number, and one special character.")
    private String password;
}
