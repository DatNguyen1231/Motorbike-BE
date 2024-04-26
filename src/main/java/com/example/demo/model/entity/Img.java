package com.example.demo.model.entity;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import javax.persistence.*;

@Entity
@Table(name = "Img")
@Getter
@Setter
public class Img {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImg;

    @Type(type = "text")
    @Column(name = "image", columnDefinition = "TEXT")
    private String imgData;

    private String content;

}
