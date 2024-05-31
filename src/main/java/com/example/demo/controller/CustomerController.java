package com.example.demo.controller;

import com.example.demo.model.Dto.UserRequestDto;
import com.example.demo.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@RestController
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/change-customer-information")
    public ResponseEntity<?> changeCustomerInformation(@RequestBody UserRequestDto userRequestDto) {
        return customerService.changeCustomerInformation(userRequestDto);
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/get-information-user")
    public ResponseEntity<?> getInformation() {
        return customerService.getInformation();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam("oldPassword") String oldPassword,
                                            @RequestParam("newPassword") String newPassword,
                                            @RequestParam("enterPassword") String enterPassword) {
        return customerService.changePassword(oldPassword, newPassword, enterPassword);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-all-user/{page}/{size}/{idRole}")
    public ResponseEntity<?> getAllUser(@PathVariable int page, @PathVariable int size, @PathVariable Long idRole) {
        return customerService.getAllUser(page, size, idRole);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/lock-account/{id}")
    public ResponseEntity<?> lockAccount( @PathVariable Long id) {
        return customerService.lockAccount(id);
    }
}
