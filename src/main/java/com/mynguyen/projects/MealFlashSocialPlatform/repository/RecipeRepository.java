package com.mynguyen.projects.MealFlashSocialPlatform.repository;

import com.mynguyen.projects.MealFlashSocialPlatform.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecipeRepository extends JpaRepository<Recipe, Integer> {

}
