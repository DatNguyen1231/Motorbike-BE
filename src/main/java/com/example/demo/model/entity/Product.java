package com.example.demo.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Product")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;
    private long quantity;
    private float Discount;
    //  1:Kawasaki2:Ducati 3:Honda4:Suziki    5:đầu nhớt  6:Phanh xe 7Gương 8Bánh xe
    private Long detailType;
    @ManyToOne
    @JoinColumn(name = "id_TypeCar", nullable = false)
    private TypeProduct typeProduct;
    @Type(type = "text")
    @Column(name = "describe", columnDefinition = "TEXT")
    private String describe;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "productMoto_id")
    private List<Img> images = new ArrayList<>();

}

