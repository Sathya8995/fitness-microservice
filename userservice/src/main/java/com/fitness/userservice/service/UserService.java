package com.fitness.userservice.service;

import com.fitness.userservice.dto.RegisterRequest;
import com.fitness.userservice.dto.UserResponse;
import com.fitness.userservice.model.User;
import com.fitness.userservice.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repo;

    public  UserResponse register( RegisterRequest request) {

        if(repo.existsByEmail(request.getEmail())){
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFname(request.getFname());
        user.setLname(request.getLname());

        User savedUser = repo.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setPassword(savedUser.getPassword());
        userResponse.setFname(savedUser.getFname());
        userResponse.setLname(savedUser.getLname());
        userResponse.setCreatedAt(savedUser.getCreatedAt());
        userResponse.setUpdatedAt(savedUser.getUpdatedAt());

        return userResponse;

    }

    @Cacheable(value = "users", key = "#userId")
    public UserResponse getUserProfile(String userId) {
        User user = repo.findById(userId).orElseThrow(() -> new RuntimeException("User not Found"));
        UserResponse userResponse = new UserResponse();
        userResponse.setId(String.valueOf(user.getId()));
        userResponse.setEmail(user.getEmail());
        userResponse.setFname(user.getFname());
        userResponse.setLname(user.getLname());
        userResponse.setCreatedAt(user.getCreatedAt());
        userResponse.setUpdatedAt(user.getUpdatedAt());
        return userResponse;
    }

    public Boolean existByUserId(String userID) {
        return repo.existsById(userID);
    }
}
