package com.example.demo.controller;


import com.example.demo.model.Dto.JwtRequest;
import com.example.demo.model.Dto.UserRequestDto;
import com.example.demo.service.serviceImpl.JwtUserDetailsService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class JwtAuthenticationController {
    private final JwtUserDetailsService userDetailsService;

    public JwtAuthenticationController(JwtUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserRequestDto user) {
        return userDetailsService.save(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        return userDetailsService.login(authenticationRequest);
    }
}
