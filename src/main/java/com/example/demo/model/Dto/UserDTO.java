package com.example.demo.model.Dto;

import com.example.demo.model.entity.DAOUser;
import com.example.demo.model.entity.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private int status;
    private String role;

  public   UserDTO(DAOUser user){
       this.id=user.getId();
        this.username=user.getUsername();
        this. fullName=user.getFullName();
        this. email=user.getEmail();
        this. phoneNumber=user.getPhoneNumber();
        this. address=user.getAddress();
        this.status=user.getStatus();
        this.role=user.getRole().getRole();
    }
}
