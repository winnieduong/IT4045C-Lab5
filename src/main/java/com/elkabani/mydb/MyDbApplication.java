package com.elkabani.mydb;

import com.elkabani.mydb.entities.Category;
import com.elkabani.mydb.entities.Product;
import com.elkabani.mydb.repositories.CategoryRepository;
import com.elkabani.mydb.repositories.ProductRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;

@SpringBootApplication
public class MyDbApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(MyDbApplication.class, args);

        CategoryRepository categoryRepo = context.getBean(CategoryRepository.class);
        ProductRepository productRepo = context.getBean(ProductRepository.class);

        // A) Create a Product with a New Category
        System.out.println("\n--- OPERATION A ---");
        Category electronics = Category.builder().name("Electronics").build();
        categoryRepo.save(electronics); // Save category first so it gets a primary key ID

        Product camera = Product.builder()
                .name("Camera")
                .price(new BigDecimal("599.99"))
                .category(electronics)
                .build();
        productRepo.save(camera);
        System.out.println("Saved Product: " + camera.getName() + " in Category: " + electronics.getName());

        // B) Create a Product for an Existing Category
        System.out.println("\n--- OPERATION B ---");
        // Retrieve the category we just saved
        Category existingCategory = categoryRepo.findById(electronics.getId()).orElseThrow();

        Product headphones = Product.builder()
                .name("Headphones")
                .price(new BigDecimal("149.99"))
                .category(existingCategory)
                .build();
        productRepo.save(headphones);
        System.out.println("Saved Product: " + headphones.getName() + " in Category: " + existingCategory.getName());

        // C) Delete a Product
        System.out.println("\n--- OPERATION C ---");
        System.out.println("Deleting Product: " + headphones.getName());
        // productRepo.delete(headphones);

        boolean exists = productRepo.findById(headphones.getId()).isPresent();
        System.out.println("Does Headphones still exist in DB? " + exists);
        System.out.println("----------------------------------------------------\n");
    }
}