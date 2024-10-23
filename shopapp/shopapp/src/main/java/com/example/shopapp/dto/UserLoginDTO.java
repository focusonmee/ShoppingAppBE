package com.example.shopapp.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class UserLoginDTO {

    @NotBlank(message = "Phone number is required")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "Password can not be null")
    private String password;

    @Min(value=1, message="You must enter role's Id")
    @JsonProperty("role_id")
    private Long RoleId;
}
