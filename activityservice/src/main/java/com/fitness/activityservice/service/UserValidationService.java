package com.fitness.activityservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationService  {

    private final WebClient userServiceWebClient;

    public boolean validateUser(String userId){
    log.info("Calling User Validation API for UserId, {}", userId);
        try {
            return Boolean.TRUE.equals(userServiceWebClient
                    .get()
                    .uri("/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());
        } catch (WebClientResponseException e) {

            if(e.getStatusCode().equals(HttpStatus.SC_NOT_FOUND)) {
                throw new RuntimeException("User Not Found: " + userId);
            } else if (e.getStatusCode() == org.springframework.http.HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Bad Request");
            }

        }

        return false;

    }
}
