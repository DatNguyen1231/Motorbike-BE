package com.example.demo.model.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Reviews")
@Getter
@Setter
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rating;
    private String comment;
    private Date dateReview;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "id_Product", nullable = false)
    private Product product;
    @ManyToOne
    @JoinColumn(name = "id_User", nullable = false)
    private DAOUser user;


}
