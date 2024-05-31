package com.example.demo.model.Dto;

import com.example.demo.model.entity.Img;
import com.example.demo.model.entity.Product;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProductSomeReponseDto {
    private long id;
    private String name;
    private double originalPrice;
    private double newPrice;
    private float discount;
    private long quantity;
    private String detailType;
    private List<Img> images = new ArrayList<>();

    public ProductSomeReponseDto(Product product) {
        this.setId(product.getId());
        this.setName(product.getName());
        this.setOriginalPrice(product.getPrice());
        this.setNewPrice(product.getPrice() - (product.getPrice() * product.getDiscount() / 100));
        this.setDiscount(product.getDiscount());
        this.setQuantity(product.getQuantity());
        this.setDetailType(getDetail(product.getDetailType()));
        this.setImages(product.getImages());
    }

    public String getDetail(Long detail) {
        switch (detail.intValue()) {
            case 1:
                return "HonDa";

            case 2:
                return "Ducati";
            case 3:
                return "Yamaha";
            case 4:
                return "Kawasaki";
            case 5:
                return "Dầu nhớt";
            case 6:
                return "Gương";
            case 7:
                return "Phanh";
            case 8:
                return "Bánh xe";
            default:
                return detail.toString();
        }
    }
}
