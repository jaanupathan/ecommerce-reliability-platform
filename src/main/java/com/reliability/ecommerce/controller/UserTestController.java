package com.reliability.ecommerce.controller;

import com.reliability.ecommerce.dto.UserRequest;
import com.reliability.ecommerce.entity.Role;
import com.reliability.ecommerce.entity.User;
import com.reliability.ecommerce.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test/users")
public class UserTestController {

    private final UserRepository userRepository;

    public UserTestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    public User createUser(@RequestBody UserRequest request) {

        User user = new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                Role.valueOf(request.getRole().toUpperCase())
        );

        return userRepository.save(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}