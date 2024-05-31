package com.example.demo.service;

import com.example.demo.model.Dto.ReviewsDto;
import com.example.demo.model.Dto.UserRequestDto;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<?> getInformation();
    ResponseEntity<?> changePassword(String oldPassword,String newPassword, String enterPassword);
    ResponseEntity<?> changeCustomerInformation(UserRequestDto userRequestDto);
    ResponseEntity<?> getAllUser(int page, int size, Long id);
    ResponseEntity<?> lockAccount(Long id);
}
