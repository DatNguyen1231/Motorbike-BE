package com.example.demo.model.Dto;

import com.example.demo.model.entity.Product;
import com.example.demo.model.entity.ShoppingCart;
import com.example.demo.model.entity.ShoppingCartDetail;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class ShoppingCartDtoReturn {

    private String name;
    private String address;
    private String numberPhone;
    private String email;
    private long idCart;
    private String paymentDate;
    private int status;
    private double totalPrice;

    private List<ShoppingCartDetailDto> shoppingCartDetailsDto = new ArrayList<>();

    public ShoppingCartDtoReturn(ShoppingCart shoppingCart) {
        this.name=shoppingCart.getUser().getUsername();
        this.address=shoppingCart.getUser().getAddress();
        this.numberPhone=shoppingCart.getUser().getPhoneNumber();
        this.email=shoppingCart.getUser().getEmail();
        this.idCart = shoppingCart.getId();
        this.status = shoppingCart.getStatus();
        this.paymentDate = formatDate(shoppingCart.getPaymentDate());

        this.shoppingCartDetailsDto = shoppingCart.getShoppingCartDetails().stream()
                .map(ShoppingCartDetailDto::new)
                .collect(Collectors.toList());

        for (ShoppingCartDetailDto a : shoppingCartDetailsDto) {
            this.totalPrice += (double) a.getProductSomeReponseDto().getNewPrice() * a.getQuantityCart();
        }

    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
