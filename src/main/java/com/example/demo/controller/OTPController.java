package com.example.demo.controller;

import com.example.demo.service.OTPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/otp")
public class OTPController {

    private final OTPService otpService;

    public OTPController(OTPService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/otp")
    public ResponseEntity<?> email(@RequestParam("email") String email) {
        return otpService.sendOTP(email);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPass(@RequestParam("email") String email, @RequestParam("otp") String otp) {
        return otpService.resetPassword(email, otp);
    }
}

