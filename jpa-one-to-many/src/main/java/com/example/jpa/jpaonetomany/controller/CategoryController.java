package com.example.jpa.jpaonetomany.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.jpa.jpaonetomany.exception.ResourceNotFoundException;
import com.example.jpa.jpaonetomany.model.Category;
import com.example.jpa.jpaonetomany.repository.CategoryRepository;
import com.example.jpa.jpaonetomany.repository.ProductRepository;

@RestController
public class CategoryController {

	 @Autowired
	 private CategoryRepository categoryRepository;
	 
	 @Autowired
	 private ProductRepository  productRepository;
	 
	 @GetMapping("/products/{productId}/categorys")
	 public Page<Category> getAllCategoryByProductId(@PathVariable(value="productId") Long productId,
			                   Pageable pageable){
		 return categoryRepository.findByProductId(productId, pageable);
	 }
	 
	 @PostMapping("/products/{productId}/categorys")
	 public Category creareCategory(@PathVariable (value="productId") Long productId,
			                   @Validated @RequestBody Category category) {
		 return productRepository.findById(productId).map(product -> {
            category.setProduct(product);
            return categoryRepository.save(category);
        }).orElseThrow(() -> new ResourceNotFoundException("ProductId " + productId + " not found"));
    }

    @PutMapping("/products/{productId}/categorys/{categoryId}")
    public Category updateCategory(@PathVariable (value = "productId") Long productId,
                                 @PathVariable (value = "categoryId") Long categoryId,
                                 @Validated @RequestBody Category categoryRequest) {
        if(!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("ProductId " + productId + " not found");
        }

        return categoryRepository.findById(categoryId).map(category -> {
        	category.setName(categoryRequest.getName());
            return categoryRepository.save(category);
        }).orElseThrow(() -> new ResourceNotFoundException("CategoryId " + categoryId + "not found"));
    }

    @DeleteMapping("/products/{productId}/categorys/{categoryId}")
    public ResponseEntity<?> deleteComment(@PathVariable (value = "productId") Long productId,
                              @PathVariable (value = "categoryId") Long categoryId) {
        return categoryRepository.findByIdAndProductId(categoryId, productId).map(category -> {
        	categoryRepository.delete(category);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId + " and productId " + productId));
    
	 }
}

