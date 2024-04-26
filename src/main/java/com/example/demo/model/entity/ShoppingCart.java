package com.example.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ShoppingCart")
@Getter
@Setter
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date paymentDate;
    //1 đang chờ trong giỏ hàng user 2 đã mua
    private int status;

    @ManyToOne
    @JoinColumn(name = "id_Users", nullable = false)
    private DAOUser user;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "ShoppingCartId",nullable = false)
    private List<ShoppingCartDetail> shoppingCartDetails = new ArrayList<>();


}
