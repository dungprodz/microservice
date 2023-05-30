package com.example.profileservice.model.responsebody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProfileResponseBody {
    private long id;
    private String email;
    private String status;
    private double initialBalance;
    private String name;
    private String role;
}
