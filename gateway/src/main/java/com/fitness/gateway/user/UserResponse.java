package com.fitness.gateway.user;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserResponse {

    private String Id;
    private String keycloakId;
    private String email;
    private String Password;
    private String firstName;
    private String lastName;
    private LocalDateTime CreatedAt;
    private LocalDateTime updatedAt;

}
