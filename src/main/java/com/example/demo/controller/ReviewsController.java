package com.example.demo.controller;

import com.example.demo.model.Dto.ReviewsDto;
import com.example.demo.service.ReviewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/reviews")
public class ReviewsController {

    private final ReviewsService reviewsService;

    public ReviewsController(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addProduct(@RequestBody ReviewsDto reviewsDto) {
        return reviewsService.add(reviewsDto);
    }

    @GetMapping("/get/{productId}")
    public ResponseEntity<?> get(@PathVariable long productId) {
        return reviewsService.get(productId);

    }
}

