package com.notsodaft.controller;

import com.notsodaft.model.Review;
import com.notsodaft.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController{
    private final ReviewService reviewService;

    public AdminController(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        List<Review> pending = reviewService.getPendingReviews();
        List<Review> approved = reviewService.getApprovedReviews();
        model.addAttribute("pending", pending);
        model.addAttribute("approved", approved);
        return "admin/dashboard";
    }

    @PostMapping("/reviews/{id}/approve")
    public String approve(@PathVariable Long id, RedirectAttributes redirectAttributes){
        reviewService.approve(id);
        redirectAttributes.addFlashAttribute("success", "Review approved.");
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/reviews/{id}/reject")
    public String reject(@PathVariable Long id, RedirectAttributes redirectAttributes){
        reviewService.reject(id);
        redirectAttributes.addFlashAttribute("success", "Review rejected.");
        return "redirect:/admin/dashboard";
    }
}