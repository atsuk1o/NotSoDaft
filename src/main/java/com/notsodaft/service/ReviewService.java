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
}