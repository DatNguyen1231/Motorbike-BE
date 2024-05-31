package com.example.demo.service;

import com.example.demo.model.Dto.ShoppingCartDto;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface ShoppingCartService {
    ResponseEntity<?> paymentCart();

    ResponseEntity<?> orderOrCancel(Long id, int status);

    ResponseEntity<?> updateCart(Long idProduct, int quantityCart);

    ResponseEntity<?> addCart(ShoppingCartDto shoppingCartDto);

    ResponseEntity<?> getAllCard(int status);

    ResponseEntity<?> getRevenue(Date firstDay, Date lastDay, int status);

    ResponseEntity<?> deleteByIdShoppingCartDetail(Long id);

    ResponseEntity<?> getCartByUser(int status);
     ResponseEntity<?> getAllCardByUser(int status);

    // List<ProductQuantityProjection> test();
}
