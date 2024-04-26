package com.example.demo.model.Dto;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Builder

public class sms {
    private String ApiKey;
    private String Content;
    private String Phone;
    private String SecretKey;
    private String SmsType;
    private String Brandname;
    private int Sandbox;
    private String campaignid;
}
