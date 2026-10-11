package com.elkabani.mydb.repositories;

import com.elkabani.mydb.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
}