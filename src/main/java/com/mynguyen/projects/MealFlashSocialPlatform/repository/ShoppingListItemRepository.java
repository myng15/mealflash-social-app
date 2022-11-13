package com.mynguyen.projects.MealFlashSocialPlatform.repository;

import com.mynguyen.projects.MealFlashSocialPlatform.model.Recipe;
import com.mynguyen.projects.MealFlashSocialPlatform.model.ShoppingListItem;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, Integer> {
    List<ShoppingListItem> findByUser(User user);

    ShoppingListItem findByUserAndRecipe(User user, Recipe recipe);

    @Query("UPDATE ShoppingListItem i SET i.servings = ?1 WHERE i.recipe.id = ?2 AND i.user.id = ?3")
    // Table(name="shopping_list_item") must be declared in ShoppingListItem entity class
    // Entity name (ShoppingListItem) is used (case-sensitively) in @Query, not table name (shopping_list_item).
    @Modifying
    void updateServings(Float quantity, Integer recipeID, Integer userID);

    //Alternative:
//    @Query("UPDATE ShoppingListItem i SET i.servings = :quantity WHERE i.recipe.id = :recipeID AND i.user.id = : userID")
//    Modifying
//    void updateServings(@Param("quantity") Float quantity, @Param("recipeID") Integer recipeID, @Param("userID") Integer userID);

}
