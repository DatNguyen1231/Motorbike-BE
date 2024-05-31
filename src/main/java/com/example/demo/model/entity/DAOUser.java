package com.example.demo.model.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name = "Users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DAOUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column
    //  @JsonIgnore
    private String password;

    //
    private int status;
    //thong tin
    private String fullName;
    @Email(message = "{error.invalid_email}")
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    private String address;
    @ManyToOne
    @JoinColumn(name = "id_Role", nullable = false)
    private Role role;


}
