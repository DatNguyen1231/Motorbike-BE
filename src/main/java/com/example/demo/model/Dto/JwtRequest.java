package com.example.demo.model.Dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
This class is required for storing the username and password we recieve from the client.
* */
@Getter
@Setter
public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String password;

    public JwtRequest() {

    }

    public JwtRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }
}
