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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/reviews")
public class ReviewController{

    private final ReviewService reviewService;
    private final UserService userService;
    private final EircodeValidator eircodeValidator;
    private final ReviewPhotoRepository reviewPhotoRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.proof.dir}")
    private String proofUploadDir;

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
                               @RequestParam(required = false) Double lat,
                               @RequestParam(required = false) Double lng,
                               @RequestParam(required = false) Review.PropertyType propertyType,
                               @RequestParam(value = "photos", required = false) MultipartFile[] photos,
                               @RequestParam(value = "proofDoc", required = false) MultipartFile proofDoc,
                               Authentication authentication,
                               RedirectAttributes redirectAttributes){

        if (!eircodeValidator.isValid(eircode)){
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
        review.setPropertyType(propertyType);
        review.setStatus(Review.ReviewStatus.PENDING);
        if (lat != null) review.setLat(lat);
        if (lng != null) review.setLng(lng);

        Review saved = reviewService.save(review);

        if (photos != null){
            for (MultipartFile photo : photos){
                if (!photo.isEmpty()){
                    try{
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
                    } catch(IOException e){
                        // skip
                    }
                }
            }
        }

        if (proofDoc != null && !proofDoc.isEmpty()){
            try{
                Path uploadPath = Paths.get(proofUploadDir);
                Files.createDirectories(uploadPath);
                String fileName = UUID.randomUUID() + "_proof_" + proofDoc.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(proofDoc.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                saved.setProofFileName(proofDoc.getOriginalFilename());
                saved.setProofFilePath("/proof/" + fileName);
                reviewService.save(saved);
            } catch(IOException e){
                // skip
            }
        }

        redirectAttributes.addFlashAttribute("success", "Review submitted! It will appear after approval.");
        return "redirect:/";
    }

    @GetMapping("/my")
    public String myReviews(Authentication authentication, Model model){
        User user = userService.findByEmail(authentication.getName());
        model.addAttribute("reviews", reviewService.getByAuthorId(user.getId()));
        return "reviews/my";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id,
                           Authentication authentication,
                           Model model){
        Review review = reviewService.getById(id);
        User user = userService.findByEmail(authentication.getName());
        if (!review.getAuthor().getId().equals(user.getId())){
            return "redirect:/reviews/my";
        }
        if (review.getStatus() == Review.ReviewStatus.APPROVED){
            return "redirect:/reviews/my";
        }
        model.addAttribute("review", review);
        return "reviews/edit";
    }

    @PostMapping("/{id}/edit")
    public String editReview(@PathVariable Long id,
                             @RequestParam String eircode,
                             @RequestParam String address,
                             @RequestParam String county,
                             @RequestParam int dampnessScore,
                             @RequestParam int heatingScore,
                             @RequestParam int maintenanceScore,
                             @RequestParam int overallScore,
                             @RequestParam String reviewText,
                             @RequestParam(required = false) Double lat,
                             @RequestParam(required = false) Double lng,
                             @RequestParam(required = false) Review.PropertyType propertyType,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes){
        Review review = reviewService.getById(id);
        User user = userService.findByEmail(authentication.getName());
        if (!review.getAuthor().getId().equals(user.getId())){
            return "redirect:/reviews/my";
        }
        if (review.getStatus() == Review.ReviewStatus.APPROVED){
            return "redirect:/reviews/my";
        }
        reviewService.update(id, eircode, address, county,
            dampnessScore, heatingScore, maintenanceScore, overallScore,
            reviewText, lat, lng, propertyType);
        redirectAttributes.addFlashAttribute("success", "Review updated and resubmitted for approval.");
        return "redirect:/reviews/my";
    }
}