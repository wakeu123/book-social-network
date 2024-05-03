package com.georges.booknetwork.domains.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {

    @NotEmpty(message = "unable.to.authenticate.empty.email")
    @NotBlank(message = "unable.to.authenticate.null.email")
    private String email;

    @NotEmpty(message = "unable.to.authenticate.empty.password")
    @NotBlank(message = "unable.to.authenticate.null.password")
    private String password;
}
