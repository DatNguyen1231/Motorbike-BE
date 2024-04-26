package com.example.demo.model.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ReviewsReturn {

    List<CommentDto> comments;
    int quantityReviews;
    float averageRating;
}


