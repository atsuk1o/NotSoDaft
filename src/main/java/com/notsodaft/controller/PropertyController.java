package com.notsodaft.controller;

import com.notsodaft.model.Review;
import com.notsodaft.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public String index(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String county,
            @RequestParam(required = false) Review.PropertyType propertyType,
            Model model) {
        
        try {
            List<Review> reviews = reviewService.getApprovedWithFilters(county, propertyType, search);
            
            model.addAttribute("reviewsJson", objectMapper.writeValueAsString(reviews));
            model.addAttribute("search", search);
            model.addAttribute("county", county);
            model.addAttribute("propertyType", propertyType);
            
        } catch (Exception e) {
            model.addAttribute("reviewsJson", "[]");
        }
        return "index";
    }
}