package com.example.demo.model.Dto;

import com.example.demo.model.entity.ShoppingCartDetail;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ShoppingCartDto {
    private Long idProduct;
    private int quantityCart;
}
