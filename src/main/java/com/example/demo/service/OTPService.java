package com.example.demo.service;

import org.springframework.http.ResponseEntity;

public interface OTPService {

    ResponseEntity<?> sendOTP(String email);

    ResponseEntity<?> resetPassword(String email, String otp);
}
