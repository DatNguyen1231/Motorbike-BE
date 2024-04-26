package com.example.demo.model.Dto;

import com.example.demo.model.entity.DAOUser;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/*
This is class is required for creating a response containing the JWT to be returned to the user.
 */
@Getter
@Setter
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String token;
    private UserDTO userDTO;

    public JwtResponse(String token, DAOUser daoUser) {
        this.token = token;
        userDTO = new UserDTO(daoUser);
    }
}
