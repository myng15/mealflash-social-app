package com.mynguyen.projects.MealFlashSocialPlatform;

import com.mynguyen.projects.MealFlashSocialPlatform.model.Recipe;
import com.mynguyen.projects.MealFlashSocialPlatform.model.ShoppingListItem;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.ShoppingListItemRepository;
//import com.sun.tools.javac.util.List; /*- Import this to be able to run testAddMultipleItems(). This and the following cannot be imported at the same time*/
import java.util.List; /*- Import this to be able to run testListItems()*/
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ShoppingListTests {
    @Autowired
    private ShoppingListItemRepository shoppingListItemRepo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testAddItem(){
        Recipe recipe = entityManager.find(Recipe.class, 67);
        User user = entityManager.find(User.class, 59);

        ShoppingListItem item = new ShoppingListItem(3, recipe, user);

        shoppingListItemRepo.save(item);
    }

    @Test
    public void testAddItemById(){
        Recipe recipe = new Recipe(10);
        User user = new User(2);

        ShoppingListItem item = new ShoppingListItem(2, recipe, user);

        shoppingListItemRepo.save(item);
    }

    @Test
    public void testGetItemByUser(){
        User user = new User(6);
        List<ShoppingListItem> items = shoppingListItemRepo.findByUser(user);

        assertEquals(1, items.size());
    }

//    @Test
//    public void testAddMultipleItems(){
//        Recipe recipe1 = new Recipe(2);
//        Recipe recipe2 = new Recipe(5);
//        Recipe recipe3 = new Recipe(16);
//        User creator = new User(8);
//
//        ShoppingListItem item1 = new ShoppingListItem(1, recipe1, creator);
//        ShoppingListItem item2 = new ShoppingListItem(2, recipe2, creator);
//        ShoppingListItem item3 = new ShoppingListItem(1, recipe3, creator);
//
//        shoppingListItemRepo.saveAll(List.of(item1, item2, item3));
//    }

    @Test
    public void testListItems(){
        List<ShoppingListItem> items = shoppingListItemRepo.findAll();
        items.forEach(System.out::println);
    }

    @Test
    public void testUpdateItem(){
        ShoppingListItem item = shoppingListItemRepo.findById(4).get();
        item.setServings(3);
        item.setRecipe(new Recipe(17));
    }

    @Test
    public void testRemoveItem(){
        shoppingListItemRepo.deleteById(1);
    }
}
