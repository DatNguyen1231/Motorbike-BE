package com.example.demo.model.Dto;

import com.example.demo.model.entity.ShoppingCartDetail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RevenueStatisticsDetailDTO {
    private Long idProduct;
    private String nameProduct;
    private long quantityOrder;
    private double totalMoney;


    public RevenueStatisticsDetailDTO(ShoppingCartDetail shoppingCartDetail) {
        this.idProduct = shoppingCartDetail.getProduct().getId();
        this.nameProduct = shoppingCartDetail.getProduct().getName();
        this.quantityOrder = shoppingCartDetail.getQuantityCart();
        this.totalMoney = getTotalMoney(shoppingCartDetail) ;

    }

    public double getTotalMoney(ShoppingCartDetail shoppingCartDetail) {
        return  (shoppingCartDetail.getProduct().getPrice() -
                (shoppingCartDetail.getProduct().getPrice() * shoppingCartDetail.getProduct().getDiscount() / 100 ))
                *  shoppingCartDetail.getQuantityCart();
    }
}
