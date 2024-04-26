package com.example.demo.service;

import com.example.demo.model.Dto.ReviewsDto;
import org.springframework.http.ResponseEntity;

public interface CustomerService {
    ResponseEntity<?> get(ReviewsDto reviewsDto);
}
