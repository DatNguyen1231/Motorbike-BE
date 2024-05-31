package com.example.demo.controller;

import com.example.demo.model.Dto.ReviewsDto;
import com.example.demo.service.ReviewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;

    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody ReviewsDto reviewsDto) {
        return reviewsService.addReview(reviewsDto);
    }

    @GetMapping("/get/{productId}")
    public ResponseEntity<?> get(@PathVariable long productId) {
        return reviewsService.getReview(productId);
    }
}

