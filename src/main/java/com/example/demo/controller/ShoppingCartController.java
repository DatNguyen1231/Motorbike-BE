package com.example.demo.controller;

import com.example.demo.model.Dto.ShoppingCartDto;
import com.example.demo.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("user/shopping-cart")
@RestController
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addTypeProduct(@RequestBody ShoppingCartDto shoppingCartDto) {
        return shoppingCartService.addCart(shoppingCartDto);
    }

    @PutMapping("/update-cart")
    public ResponseEntity<?> updateCart(@RequestParam("idCartDetail") long idCartDetail, @RequestParam("quantityCart") int quantityCart) {
        return shoppingCartService.updateCart(idCartDetail, quantityCart);
    }

    @PostMapping("/payment")
    public ResponseEntity<?> payment() {
        return shoppingCartService.paymentCart();
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return shoppingCartService.getAllCard();
    }

    @PostMapping("/get-cart-by-user")
    public ResponseEntity<?> getCartByUser() {
        return shoppingCartService.getCartByUser();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteByIdShoppingCartDetail(@RequestParam("id") long id) {
        return shoppingCartService.deleteByIdShoppingCartDetail(id);
    }


}
