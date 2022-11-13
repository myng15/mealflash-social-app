package com.mynguyen.projects.MealFlashSocialPlatform.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_list_item")
public class ShoppingListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "servings")
    private float servings;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ShoppingListItem() {

    }

    public ShoppingListItem(float servings, Recipe recipe, User creator) {
        this.servings = servings;
        this.recipe = recipe;
        this.user = creator;
    }

    public ShoppingListItem(Integer id, float servings, Recipe recipe, User creator) {
        this.id = id;
        this.servings = servings;
        this.recipe = recipe;
        this.user = creator;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getServings() {
        return servings;
    }

    public void setServings(float servings) {
        this.servings = servings;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User creator) {
        this.user = creator;
    }

    @Override
    public String toString() {
        return "ShoppingListItem{" +
                "id=" + id +
                ", servings=" + servings +
                ", recipe=" + recipe +
                ", user=" + user +
                '}';
    }

    @Transient
    public List<String> getIngredientSubtotals() {
        List<Ingredient> ingredients = this.getRecipe().getIngredients();
        List<String> subtotals = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            BigDecimal subtotal = ingredient.getAmount().multiply(BigDecimal.valueOf(this.getServings()));
            subtotals.add(String.valueOf(subtotal));
        }
        return subtotals;
    }

}
