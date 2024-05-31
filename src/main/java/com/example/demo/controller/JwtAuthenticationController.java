package com.example.demo.controller;


import com.example.demo.model.Dto.JwtRequest;
import com.example.demo.model.Dto.UserRequestDto;
import com.example.demo.service.CustomerService;
import com.example.demo.service.serviceImpl.JwtUserDetailsService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class JwtAuthenticationController {
    private final JwtUserDetailsService userDetailsService;


    public JwtAuthenticationController(JwtUserDetailsService userDetailsService, CustomerService customerService) {
        this.userDetailsService = userDetailsService;

    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserRequestDto user) {
        return userDetailsService.register(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        return userDetailsService.login(authenticationRequest);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete-by-user/{id}")
    public ResponseEntity<?> deleteByUser(@PathVariable Long id) {
        return userDetailsService.deleteUser(id);
    }

}
