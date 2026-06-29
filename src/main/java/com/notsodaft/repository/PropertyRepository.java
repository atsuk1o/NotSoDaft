package com.notsodaft.repository;

import com.notsodaft.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long>{
    List<Property> findByVerifiedTrueAndActiveTrue();
}