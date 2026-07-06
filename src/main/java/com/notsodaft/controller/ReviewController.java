package com.notsodaft.controller;

import com.notsodaft.model.Review;
import com.notsodaft.model.User;
import com.notsodaft.service.ReviewService;
import com.notsodaft.service.UserService;
import com.notsodaft.util.EircodeValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/reviews")
public class ReviewController{

    private final ReviewService reviewService;
    private final UserService userService;
    private final EircodeValidator eircodeValidator;

    public ReviewController(ReviewService reviewService, UserService userService, EircodeValidator eircodeValidator){
        this.reviewService = reviewService;
        this.userService = userService;
        this.eircodeValidator = eircodeValidator;
    }

    @GetMapping("/submit")
    public String submitForm(){
        return "reviews/submit";
    }

    @PostMapping("/submit")
    public String submitReview(@RequestParam String eircode,
                               @RequestParam String address,
                               @RequestParam String county,
                               @RequestParam int dampnessScore,
                               @RequestParam int heatingScore,
                               @RequestParam int maintenanceScore,
                               @RequestParam int overallScore,
                               @RequestParam String reviewText,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes){

        if (!eircodeValidator.isValid(eircode)) {
            redirectAttributes.addFlashAttribute("error", "Invalid Eircode format. Example: D01 AB12");
            return "redirect:/reviews/submit";
        }

        User author = userService.findByEmail(authentication.getName());

        Review review = new Review();
        review.setEircode(eircodeValidator.format(eircode));
        review.setAddress(address);
        review.setCounty(county);
        review.setDampnessScore(dampnessScore);
        review.setHeatingScore(heatingScore);
        review.setMaintenanceScore(maintenanceScore);
        review.setOverallScore(overallScore);
        review.setReviewText(reviewText);
        review.setAuthor(author);
        review.setStatus(Review.ReviewStatus.PENDING);

        reviewService.save(review);

        redirectAttributes.addFlashAttribute("success", "Review submitted! It will appear after admin approval.");
        return "redirect:/";
    }
}