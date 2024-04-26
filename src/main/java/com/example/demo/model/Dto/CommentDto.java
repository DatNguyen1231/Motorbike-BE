package com.example.demo.model.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentDto {
    private String name;
    private String comment;
    private int rating;
    private Date dateReview;

}
