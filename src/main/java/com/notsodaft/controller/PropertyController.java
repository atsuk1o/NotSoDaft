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
    public String index(@RequestParam(required = false) String county,
                        @RequestParam(required = false) String propertyType,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) String view,
                        Model model){
        try{
            List<Review> reviews;
            boolean isFiltered = (county != null && !county.isEmpty()) ||
                                (propertyType != null && !propertyType.isEmpty()) ||
                                (search != null && !search.isEmpty());

            if (isFiltered){
                Review.PropertyType type = null;
                if (propertyType != null && !propertyType.isEmpty()){
                    type = Review.PropertyType.valueOf(propertyType);
                }
                reviews = reviewService.getApprovedWithFilters(county, type, search);
            } else {
                reviews = reviewService.getSmartFeed();
            }

            model.addAttribute("reviewsJson", objectMapper.writeValueAsString(reviews));
            model.addAttribute("county", county);
            model.addAttribute("search", search);
            model.addAttribute("propertyType", propertyType != null && !propertyType.isEmpty()
                ? Review.PropertyType.valueOf(propertyType) : null);
        } catch(Exception e){
            model.addAttribute("reviewsJson", "[]");
        }
        return "index";
    }
}