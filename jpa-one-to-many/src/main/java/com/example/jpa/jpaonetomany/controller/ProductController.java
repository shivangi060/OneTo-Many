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
import com.example.jpa.jpaonetomany.model.Product;
import com.example.jpa.jpaonetomany.repository.ProductRepository;


@RestController
public class ProductController {
  
	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping("/products")
	public Page<Product> getAllProducts(Pageable pageable)
	{
		return productRepository.findAll(pageable);
	}
	
	@PostMapping("/products")
	public Product createProduct(@Validated @RequestBody Product product) {
		return productRepository.save(product);
	}
	
	@PutMapping("/products/{productId}")
	public Product updaProduct(@PathVariable Long productId, @Validated @RequestBody Product productRequest) {
		return productRepository.findById(productId).map(product ->{
			product.setTitle(productRequest.getTitle());
			product.setDescription(productRequest.getDescription());
			return productRepository.save(product);
		}).orElseThrow(() -> new ResourceNotFoundException("ProductId"+ productId+"not found"));
	}
	
	@DeleteMapping("/products/{productId")
	public ResponseEntity<?> deleteProduct(@PathVariable Long productId){
		return productRepository.findById(productId).map(product -> {
			productRepository.delete(product);
			return ResponseEntity.ok().build();
		}).orElseThrow(() -> new ResourceNotFoundException("ProductId"+ productId+"not found"));
	}
}
