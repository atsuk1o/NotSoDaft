package com.notsodaft.controller;

import com.notsodaft.model.Review;
import com.notsodaft.model.ReviewPhoto;
import com.notsodaft.model.User;
import com.notsodaft.repository.ReviewPhotoRepository;
import com.notsodaft.service.ReviewService;
import com.notsodaft.service.UserService;
import com.notsodaft.util.EircodeValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final EircodeValidator eircodeValidator;
    private final ReviewPhotoRepository reviewPhotoRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public ReviewController(ReviewService reviewService,
                            UserService userService,
                            EircodeValidator eircodeValidator,
                            ReviewPhotoRepository reviewPhotoRepository) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.eircodeValidator = eircodeValidator;
        this.reviewPhotoRepository = reviewPhotoRepository;
    }

    @GetMapping("/submit")
    public String submitForm() {
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
                            @RequestParam(required = false) Double lat,
                            @RequestParam(required = false) Double lng,
                            @RequestParam(value = "photos", required = false) MultipartFile[] photos,
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
        if (lat != null) review.setLat(lat);
        if (lng != null) review.setLng(lng);

        Review saved = reviewService.save(review);

        if (photos != null) {
            for (MultipartFile photo : photos) {
                if (!photo.isEmpty()) {
                    try {
                        Path uploadPath = Paths.get(uploadDir);
                        Files.createDirectories(uploadPath);
                        String fileName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
                        Path filePath = uploadPath.resolve(fileName);
                        Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                        ReviewPhoto rp = new ReviewPhoto();
                        rp.setReview(saved);
                        rp.setFileName(photo.getOriginalFilename());
                        rp.setFilePath("/uploads/reviews/" + fileName);
                        reviewPhotoRepository.save(rp);
                    } catch (IOException e) {
                        // skip
                    }
                }
            }
        }

        redirectAttributes.addFlashAttribute("success", "Review submitted! It will appear after approval.");
        return "redirect:/";
    }
}