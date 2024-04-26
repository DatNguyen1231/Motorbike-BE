package com.example.demo.model.Dto;

import com.example.demo.model.entity.Product;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductPageDTO {
    private List<Product> products;
    private int page;
    private int size;
    private long totalElements;
    private long totalPages;

}
