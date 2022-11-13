package com.mynguyen.projects.MealFlashSocialPlatform;

import com.mynguyen.projects.MealFlashSocialPlatform.model.Category;
import com.mynguyen.projects.MealFlashSocialPlatform.model.Ingredient;
import com.mynguyen.projects.MealFlashSocialPlatform.model.Recipe;
import com.mynguyen.projects.MealFlashSocialPlatform.model.ShoppingListItem;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.IngredientRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.RecipeRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.ShoppingListItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RecipeRepositoryTests {
    @Autowired
    private RecipeRepository repo;

    @Autowired
    private IngredientRepository ingredientRepo;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void test(){

    }
    @Test
    public void testCreateCategory(){
//Test with TestEntityManager persist - Test objects will be actually persisted into the database for a "more realistic" test
        Category catKorean = new Category("korean");
        Category catBarbecue = new Category("barbecue");

        entityManager.persist(catKorean);
        entityManager.persist(catBarbecue);
    }

    @Test
    public void testCreateNewRecipeOfOneCategory(){
        Category catSeafood = entityManager.find(Category.class, 1);
//Test would fail if provided id for primaryKey parameter is of wrong type
//If category's id is of type Long: ...find(Category.class, 1L);
//If category's id is of type Float: ...find(Category.class, 1f);

        Recipe recipe = new Recipe("Creamy Shrimp Pasta", "Boil pasta and shrimps", 45, "minutes");
        recipe.addCategory(catSeafood);

        repo.save(recipe);
    }

    @Test
    public void testCreateNewRecipeOfTwoCategories(){
        Category catKorean = entityManager.find(Category.class, 3);
        Category catSoup = entityManager.find(Category.class, 2);

        Recipe recipe = new Recipe("Kimchi jjigae");
        recipe.addCategory(catKorean);
        recipe.addCategory(catSoup);

        repo.save(recipe);
    }

    @Test
    public void testAssignExistingRecipeToNewCategory(){
        Recipe recipe = repo.findById(2).get();
        Category catKorean = entityManager.find(Category.class, 3);
        recipe.addCategory(catKorean);
    }

    @Test
    public void testRemoveExistingRecipeFromCategory(){
        Recipe recipe = repo.findById(2).get();
        Category category = new Category(3);
        recipe.removeFromCategory(category);
    }

    @Test
    public void testCreateRecipeOfNewCategory(){
        Category category = new Category("dessert");
        Recipe recipe = new Recipe("Bingsu");
        recipe.addCategory(category);

        repo.save(recipe);
    }

    @Test
    public void testGetRecipe(){
        Recipe recipe = repo.findById(1).get();
        entityManager.detach(recipe);
        System.out.println(recipe.getTitle());
        System.out.println(recipe.getCategories());
    }

    @Test
    public void testDeleteRecipe(){
        Recipe recipe = repo.findById(65).get();
        List<Ingredient> ingredientsOfRecipe = recipe.getIngredients();
        ingredientRepo.deleteAll(ingredientsOfRecipe);

        List<ShoppingListItem> shoppingListItemsOfRecipe = recipe.getShoppingListItems();
        shoppingListItemRepo.deleteAll(shoppingListItemsOfRecipe);

        Set<Category> categoriesOfRecipe = recipe.getCategories();
        for (Category category : categoriesOfRecipe) {
            recipe.removeFromCategory(category);
        }

        repo.deleteById(65);
    }
}
