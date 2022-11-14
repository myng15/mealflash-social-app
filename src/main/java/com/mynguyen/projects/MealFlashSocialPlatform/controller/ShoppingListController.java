package com.mynguyen.projects.MealFlashSocialPlatform.controller;

import com.mynguyen.projects.MealFlashSocialPlatform.MealFlashUserDetails;
import com.mynguyen.projects.MealFlashSocialPlatform.model.Ingredient;
import com.mynguyen.projects.MealFlashSocialPlatform.model.Recipe;
import com.mynguyen.projects.MealFlashSocialPlatform.model.ShoppingListItem;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.RecipeRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.ShoppingListItemRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@Transactional //recommended to be in Service layer if using one
public class ShoppingListController {
    @Autowired
    private ShoppingListItemRepository shoppingListItemRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RecipeRepository recipeRepo;

    @GetMapping("/shopping-list")
    public String showShoppingList(@AuthenticationPrincipal MealFlashUserDetails userPrincipal, Model model){
        User user = userRepo.findById(userPrincipal.getId()).get();
        List<ShoppingListItem> shoppingListItems = shoppingListItemRepo.findByUser(user);

        model.addAttribute("shoppingListItems", shoppingListItems);
        return "shopping-list";
    }
    @GetMapping("/recipes/into-shopping-list/{id}")
    public String addRecipeToShoppingList(@PathVariable("id") Integer recipeID,
                                          @AuthenticationPrincipal MealFlashUserDetails userPrincipal,
                                          Model model, RedirectAttributes redirectAttributes){
        if (userPrincipal == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Log in to add recipes to your shopping list.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/login";
        }

        User user = userRepo.findById(userPrincipal.getId()).get();
        Recipe recipe = recipeRepo.findById(recipeID).get();
        ShoppingListItem item = shoppingListItemRepo.findByUserAndRecipe(user, recipe);

        float addedServings = 1;
        if (item != null) {
            addedServings += item.getServings();
            item.setServings(addedServings);
        } else {
            item = new ShoppingListItem(addedServings, recipe, user);
        };
        shoppingListItemRepo.save(item);

//        return "recipe::#addToShoppingListMessage"; //if use ajax function addToShoppingList.js then return only #addToShoppingListMessage section, no page refresh
        redirectAttributes.addFlashAttribute("addToShoppingListMessage", "Recipe for 1 serving of \"" + recipe.getTitle() + "\"" + " was added to your shopping list.");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/shopping-list";
    }

    @GetMapping("/recipes/out-of-shopping-list/{id}")
    public String removeRecipeFromShoppingList(@PathVariable("id") Integer recipeID,
                                          @AuthenticationPrincipal MealFlashUserDetails userPrincipal,
                                          Model model, RedirectAttributes redirectAttributes){
        if (userPrincipal == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Log in to modify your shopping list.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/login";
        }

        User user = userRepo.findById(userPrincipal.getId()).get();
        Recipe recipe = recipeRepo.findById(recipeID).get();
        ShoppingListItem item = shoppingListItemRepo.findByUserAndRecipe(user, recipe);

        if (item == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "\"" + recipe.getTitle() + "\"" + " recipe wasn't in your shopping list.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/shopping-list";
        }

        shoppingListItemRepo.deleteById(item.getId());

        List<ShoppingListItem> shoppingListItems = shoppingListItemRepo.findByUser(user);
        model.addAttribute("shoppingListItems", shoppingListItems);

        redirectAttributes.addFlashAttribute("successMessage", "\"" + recipe.getTitle() + "\"" + " recipe has been removed from your shopping list.");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/shopping-list";
    }

    @PostMapping("/shopping-list/update/{recipeID}/{quantity}")
    @ResponseBody
    public List<String> updateServingsAndSubtotals(@PathVariable("recipeID") Integer recipeID,
                                 @PathVariable("quantity") Float quantity,
                                 @AuthenticationPrincipal MealFlashUserDetails userPrincipal,
                                 Model model, RedirectAttributes redirectAttributes){
//        if (userPrincipal == null){
//            redirectAttributes.addFlashAttribute("errorMessage", "Log in to add recipes to your shopping list.");
//            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
//            return "redirect:/login";
//        }
        shoppingListItemRepo.updateServings(quantity, recipeID, userPrincipal.getId());
        Recipe recipe = recipeRepo.findById(recipeID).get();
        User user = userRepo.findById(userPrincipal.getId()).get();
        List<String> subtotals = shoppingListItemRepo.findByUserAndRecipe(user, recipe).getIngredientSubtotals();
//        User user = userRepo.findById(userPrincipal.getId()).get();
//        List<ShoppingListItem> shoppingListItems = shoppingListItemRepo.findByUser(user);
//        model.addAttribute("shoppingListItems", shoppingListItems);

//        List<String> subtotals = updateServingsAndIngredientSubtotals(quantity, recipeID, userPrincipal.getId());
//        String subtotal = subtotals.get(1);
//        model.addAttribute("subtotals", subtotals.get(0));
        return subtotals;
    }

    public List<String> updateServingsAndIngredientSubtotals(Float quantity, Integer recipeID, Integer userID) {
        shoppingListItemRepo.updateServings(quantity, recipeID, userID);
        Recipe recipe = recipeRepo.findById(recipeID).get();
        List<Ingredient> ingredients = recipe.getIngredients();
        List<String> subtotals = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            BigDecimal subtotal = ingredient.getAmount().multiply(BigDecimal.valueOf(quantity));
            subtotals.add(String.valueOf(subtotal));
        }
        return subtotals;
    }
}
