package com.elkabani.mydb.controllers;

import com.elkabani.mydb.dto.ProductDto;
import com.elkabani.mydb.entities.Category;
import com.elkabani.mydb.entities.Product;
import com.elkabani.mydb.mappers.ProductMapper;
import com.elkabani.mydb.repositories.CategoryRepository;
import com.elkabani.mydb.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapper productMapper;

    // 1. Fetch All Products (with optional filter)
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(
            @RequestParam(name = "categoryId", required = false) Integer categoryId) {

        List<Product> products;
        if (categoryId == null) {
            products = productRepository.findAll();
        } else {
            products = productRepository.findByCategoryId(categoryId);
        }

        List<ProductDto> productDtos = products.stream()
                .map(productMapper::toDto)
                .toList();

        return ResponseEntity.ok(productDtos);
    }

    // 2. Fetch a Single Product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> ResponseEntity.ok(productMapper.toDto(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Create a New Product
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        Optional<Category> categoryOpt = categoryRepository.findById(productDto.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Product product = productMapper.toEntity(productDto);
        product.setCategory(categoryOpt.get());

        Product savedProduct = productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productMapper.toDto(savedProduct));
    }

    // 4. Update an Existing Product
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        Optional<Product> existingProductOpt = productRepository.findById(id);
        if (existingProductOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Optional<Category> categoryOpt = categoryRepository.findById(productDto.getCategoryId());
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Product existingProduct = existingProductOpt.get();
        productMapper.update(productDto, existingProduct);
        existingProduct.setCategory(categoryOpt.get());

        Product updatedProduct = productRepository.save(existingProduct);
        return ResponseEntity.ok(productMapper.toDto(updatedProduct));
    }

    // 5. Delete an Existing Product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}