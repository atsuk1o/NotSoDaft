package com.notsodaft.service;

import com.notsodaft.model.Review;
import com.notsodaft.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReviewService{
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository){
        this.reviewRepository = reviewRepository;
    }

    public Review save(Review review){
        return reviewRepository.save(review);
    }

    public List<Review> getPendingReviews(){
        return reviewRepository.findByStatus(Review.ReviewStatus.PENDING);
    }

    public List<Review> getApprovedReviews(){
        return reviewRepository.findByStatus(Review.ReviewStatus.APPROVED);
    }

    public Review getById(Long id){
        return reviewRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Review not found"));
    }

    public void approve(Long id){
        Review review = getById(id);
        review.setStatus(Review.ReviewStatus.APPROVED);
        reviewRepository.save(review);
    }

    public void reject(Long id){
        Review review = getById(id);
        review.setStatus(Review.ReviewStatus.REJECTED);
        reviewRepository.save(review);
    }

    public List<Review> getByAuthorId(Long userId){
        return reviewRepository.findByAuthorId(userId);
    }

    public void update(Long id, String eircode, String address, String county, int dampnessScore, int heatingScore, int maintenanceScore, int overallScore, String reviewText, Double lat, Double lng, Review.PropertyType propertyType){
        Review review = getById(id);
        review.setEircode(eircode);
        review.setAddress(address);
        review.setCounty(county);
        review.setDampnessScore(dampnessScore);
        review.setHeatingScore(heatingScore);
        review.setMaintenanceScore(maintenanceScore);
        review.setOverallScore(overallScore);
        review.setReviewText(reviewText);
        if (lat != null) review.setLat(lat);
        if (lng != null) review.setLng(lng);
        review.setPropertyType(propertyType);
        review.setStatus(Review.ReviewStatus.PENDING);
        reviewRepository.save(review);
    }

    public List<Review> getApprovedWithFilters(String county, Review.PropertyType propertyType, String search){
        return reviewRepository.findApprovedWithFilters(
            (county != null && !county.isEmpty()) ? county : null,
            propertyType,
            (search != null && !search.isEmpty()) ? search : null
        );
    }
}