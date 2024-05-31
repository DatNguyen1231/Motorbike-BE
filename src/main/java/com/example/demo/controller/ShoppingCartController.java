package com.example.demo.controller;

import com.example.demo.model.Dto.ShoppingCartDto;
import com.example.demo.service.ShoppingCartService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;

@PreAuthorize("hasRole('ROLE_USER')")
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

    @PostMapping("/get-all-oder/{status}")
    public ResponseEntity<?> getAll(@PathVariable int status) {
        return shoppingCartService.getAllCard(status);
    }


    //duyet don
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/approve-the-order/{id}")
    public ResponseEntity<?> approveTheOrder(@PathVariable Long id) {
        int status = 2;//approve the order
        return shoppingCartService.orderOrCancel(id, status);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/cancel-the-order/{id}")
    public ResponseEntity<?> cancelTheOrder(@PathVariable Long id) {
        int status = 3;//cancel the order
        return shoppingCartService.orderOrCancel(id, 3);
    }

    @PostMapping("/get-cart-by-user")
    public ResponseEntity<?> getCartByUser() {
        return shoppingCartService.getCartByUser(0);
    }

    @PostMapping("/get-cart-by-user/{status}")
    public ResponseEntity<?> getCartByUserAndStatus(@PathVariable int status) {
        return shoppingCartService.getAllCardByUser(status);
    }

    @PostMapping("/get-cart-by-user-paid")
    public ResponseEntity<?> getCartByUserPaid() {
        return shoppingCartService.getCartByUser(1);
    }

    @PostMapping("/get-revenue-by-day")
    public ResponseEntity<?> getRevenueByDay(
            @RequestParam("firstDay") @DateTimeFormat(pattern = "yyyy-MM-dd") Date firstDay,
            @RequestParam("lastDay") @DateTimeFormat(pattern = "yyyy-MM-dd") Date lastDay,
            @RequestParam("status") int status
    ) {
        // Điều chỉnh giờ của lastDay
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastDay);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        lastDay = calendar.getTime();
        return shoppingCartService.getRevenue(firstDay, lastDay, status);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteByIdShoppingCartDetail(@RequestParam("id") long id) {
        return shoppingCartService.deleteByIdShoppingCartDetail(id);
    }


//    @GetMapping("/test")
//    public List<ProductQuantityProjection> test() {
//        return shoppingCartService.test();
//    }
}

