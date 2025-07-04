package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository repository;


    public UserResponse register(@Valid RegisterRequest request) {

        UserResponse userResponse = new UserResponse();

        if (repository.existsByEmail(request.getEmail())){
            User existingUser = repository.findByEmail(request.getEmail());

            userResponse.setId(existingUser.getId());
            userResponse.setKeycloakId(existingUser.getKeycloakId());
            userResponse.setEmail(existingUser.getEmail());
            userResponse.setPassword(existingUser.getPassword());
            userResponse.setFirstName(existingUser.getFirstName());
            userResponse.setLastName(existingUser.getLastName());
            userResponse.setCreatedAt(existingUser.getCreatedAt());
            userResponse.setUpdatedAt(existingUser.getUpdatedAt());
            return userResponse;
        }
        User user =  new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setKeycloakId(request.getKeycloakId());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());


       User savedUser =  repository.save(user);

       userResponse.setId(savedUser.getId());
       userResponse.setEmail(savedUser.getEmail());
       userResponse.setPassword(savedUser.getPassword());
       userResponse.setKeycloakId(savedUser.getKeycloakId());
       userResponse.setFirstName(savedUser.getFirstName());
       userResponse.setLastName(savedUser.getLastName());
       userResponse.setCreatedAt(savedUser.getCreatedAt());
       userResponse.setUpdatedAt(savedUser.getUpdatedAt());

    return userResponse;
    }

    public UserResponse getUserProfile(String userId) {

        User user = repository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setPassword(user.getPassword());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());

        return userResponse;
     }

//    public Boolean existsById(@Valid String userId) {
//        log.info("Calling User Validation API for userId: {}", userId);
//        return repository.existsById(userId);
//    }
//


    public Boolean existsbyKeycloakId(@Valid String userId) {
        log.info("Calling User Validation API for userId: {}", userId);
        return repository.existsByKeycloakId(userId);
    }
}
