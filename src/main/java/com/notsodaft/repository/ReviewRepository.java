package com.notsodaft.repository;

import com.notsodaft.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByStatus(Review.ReviewStatus status);
    List<Review> findByAuthorId(Long userId);
    List<Review> findByEircode(String eircode);
}