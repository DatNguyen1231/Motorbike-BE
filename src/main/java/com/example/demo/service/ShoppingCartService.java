package com.example.demo.service;

import com.example.demo.model.Dto.ShoppingCartDto;
import org.springframework.http.ResponseEntity;

public interface ShoppingCartService {
    ResponseEntity<?> paymentCart();

    ResponseEntity<?> updateCart(Long idProduct, int quantityCart);

    ResponseEntity<?> addCart(ShoppingCartDto shoppingCartDto);

    ResponseEntity<?> getAllCard();

    ResponseEntity<?> deleteByIdShoppingCartDetail(Long id);

    ResponseEntity<?> getCartByUser();

}
