package com.example.demo.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "OTPs")

@Getter
@Setter
@NoArgsConstructor
public class OTP {
    @Id
    private Long userId;
    private String otp;
    private Instant expirationTime;

    public OTP(Long userId,String otp, Instant expirationTime ){
        this.userId=userId;
        this.otp=otp;
        this.expirationTime=expirationTime;
    }

}
