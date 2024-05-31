package com.example.demo.model.Dto;

import com.example.demo.model.entity.Img;
import com.example.demo.model.entity.Product;
import com.example.demo.model.entity.TypeProduct;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Util.Calculate.calculateNewPrice;
import static com.example.demo.Util.Calculate.roundToOneDecimal;

@Getter
@Setter
public class ProductDetailDto {

    private Long id;
    private String name;
    private double originalPrice;
    private double newPrice;
    private long quantity;
    private float Discount;
    //  1:Kawasaki2:Ducati 3:Honda4:Suziki    5:đầu nhớt  6:Phanh xe 7Gương 8Bánh xe
    private Long detailType;
    private TypeProduct typeProduct;
    private String describe;
    private List<Img> images = new ArrayList<>();

    public ProductDetailDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.originalPrice = product.getPrice();
        this.newPrice = roundToOneDecimal(calculateNewPrice(product.getPrice(), product.getDiscount()));
        this.quantity = product.getQuantity();
        this.Discount = product.getDiscount();
        this.detailType = product.getDetailType();
        this.typeProduct = product.getTypeProduct();
        this.describe = product.getDescribe();
        this.images = product.getImages();
    }
}
