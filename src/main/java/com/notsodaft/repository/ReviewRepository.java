package com.notsodaft.repository;

import com.notsodaft.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}