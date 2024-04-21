package com.georges.booknetwork.domains.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter@Setter
public class RegistrationRequest {

    private Long id;

    @NotEmpty(message = "")
    @NotBlank(message = "")
    private String firstname;

    @NotEmpty(message = "")
    @NotBlank(message = "")
    private String lastname;

    @NotEmpty(message = "")
    @NotBlank(message = "")
    private String email;

    @NotEmpty(message = "")
    @NotBlank(message = "")
    @Size(min = 8, message = "")
    private String password;

    @NotEmpty(message = "")
    @NotBlank(message = "")
    private String role;
}
