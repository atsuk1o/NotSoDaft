package com.notsodaft.controller;

import com.notsodaft.model.Review;
import com.notsodaft.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class PropertyController {

    private final ReviewService reviewService;
    private final ObjectMapper objectMapper;

    public PropertyController(ReviewService reviewService, ObjectMapper objectMapper) {
        this.reviewService = reviewService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String index(Model model) {
        try{
            List<Review> approved = reviewService.getApprovedReviews();
            model.addAttribute("reviewsJson", objectMapper.writeValueAsString(approved));
        }catch (Exception e){
            model.addAttribute("reviewsJson", "[]");
        }
        return "index";
    }
}