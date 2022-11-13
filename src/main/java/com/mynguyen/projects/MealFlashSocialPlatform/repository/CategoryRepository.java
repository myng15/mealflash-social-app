package com.mynguyen.projects.MealFlashSocialPlatform.repository;

import com.mynguyen.projects.MealFlashSocialPlatform.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Category getCategoryByName(String name);
}
