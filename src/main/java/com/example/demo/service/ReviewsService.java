package com.example.demo.service;


import com.example.demo.model.Dto.ReviewsDto;
import org.springframework.http.ResponseEntity;

public interface ReviewsService {
    ResponseEntity<?> addReview(ReviewsDto reviewsDto);

    ResponseEntity<?> getReview(Long productID);

}
