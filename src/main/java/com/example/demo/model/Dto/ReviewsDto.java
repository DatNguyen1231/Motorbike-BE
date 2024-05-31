package com.example.demo.model.Dto;

import com.example.demo.model.entity.Product;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewsDto {

    private Long id;
    private Long idCartDetail;
    private int rating;
    private String comment;
}
