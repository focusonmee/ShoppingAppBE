package com.example.shopapp.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UpdateCategoryResponse {
    @JsonProperty("message")
    private String message;
}
