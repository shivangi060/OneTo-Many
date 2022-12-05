package com.example.jpa.jpaonetomany.repository;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.jpa.jpaonetomany.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

	Page<Category> findByProductId(Long productId, Pageable pageable);
	Optional<Category> findByIdAndProductId(Long id, Long productId);
	//Page<Category> findByIdAndProductId(Long categoryId, Long productId);
}
