package com.reliability.ecommerce.controller;

import com.reliability.ecommerce.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @GetMapping("/profile")
    public String profile(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return "Welcome " + user.getName()
                + ". You are authenticated as "
                + user.getRole();
    }
}