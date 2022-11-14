package com.mynguyen.projects.MealFlashSocialPlatform.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 150, nullable = false, unique = true)
//    @Max(value = 50, message = "Category name can be max. 50 characters long.")
    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Recipe> recipes = new ArrayList<Recipe>();

    public Category() {
    }

    public Category(Integer id) {
        this.id = id;
    }

    public Category(String name) {
        super();
        this.name = name;
    }

    public Category(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void removeRecipeFromCategory(Recipe recipe) {
        this.recipes.remove(recipe);
        recipe.getCategories().remove(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id.equals(category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
