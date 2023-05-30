package com.example.profileservice.model.requestbody;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProfileRequestBody {
    private long id;
    private String email;
    private String name;
    private String status;
    private String role;
}
