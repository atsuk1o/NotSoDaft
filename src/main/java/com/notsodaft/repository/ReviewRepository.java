package com.notsodaft.repository;

import com.notsodaft.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByStatus(Review.ReviewStatus status);
    List<Review> findByAuthorId(Long userId);
    List<Review> findByEircode(String eircode);
    @Query("SELECT r FROM Review r WHERE r.status = 'APPROVED' " +
        "AND (:county IS NULL OR r.county = :county) " +
        "AND (:propertyType IS NULL OR r.propertyType = :propertyType) " +
        "AND (:search IS NULL OR (LOWER(r.address) LIKE LOWER(CONCAT('%', :search, '%')) " +
        "OR LOWER(r.eircode) LIKE LOWER(CONCAT('%', :search, '%'))))")
    List<Review> findApprovedWithFilters(
    @Param("county") String county,
    @Param("propertyType") Review.PropertyType propertyType,
    @Param("search") String search);
    @Query("SELECT DISTINCT r FROM Review r " +
       "JOIN Comment c ON c.review = r " +
       "WHERE r.status = 'APPROVED' " +
       "AND c.createdAt >= :since " +
       "GROUP BY r " +
       "HAVING COUNT(c) >= 3 " +
       "ORDER BY COUNT(c) DESC")
    List<Review> findPopularReviews(@Param("since") LocalDateTime since);
    List<Review> findByStatusOrderByCreatedAtDesc(Review.ReviewStatus status);
}