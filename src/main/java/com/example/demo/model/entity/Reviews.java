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
    private int status;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn( referencedColumnName = "id")
    private ShoppingCartDetail shoppingCartDetail;

    @ManyToOne
    @JoinColumn(name = "id_User", nullable = false)
    private DAOUser user;


}
