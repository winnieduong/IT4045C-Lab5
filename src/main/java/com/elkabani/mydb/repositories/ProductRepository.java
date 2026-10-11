package com.elkabani.mydb.repositories;

import com.elkabani.mydb.entities.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}