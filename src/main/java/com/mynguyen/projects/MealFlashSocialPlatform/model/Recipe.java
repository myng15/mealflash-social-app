package com.mynguyen.projects.MealFlashSocialPlatform.model;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.math.BigDecimal;
import java.util.*;

@Entity
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, name = "title", nullable = false)
    private String title;

    @Column(length = 128, name = "image")
    private String image;

    @Column(length = 4000, name = "instructions")
    private String instructions;

    @Column(name = "time")
    private float time;

    @Column(length = 45, name = "time_unit")
    private String timeUnit;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>();

    @ManyToMany/*(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)*/
    @JoinTable(
            name = "recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
    //mapping many-to-many association to a Set is more efficient than to a List

    @OneToMany(mappedBy = "recipe")
    private List<ShoppingListItem> shoppingListItems = new ArrayList<>();

    public Recipe(){
        this.timeUnit = "minutes";
    }

    public Recipe(Integer id) {
        this.id = id;
    }

    public Recipe(String title) {
        super();
        this.title = title;
    }

    public Recipe(String title, float time, String timeUnit) {
        super();
        this.title = title;
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public Recipe(String title, String instructions, float time, String timeUnit) {
        super();
        this.title = title;
        this.instructions = instructions;
        this.time = time;
        this.timeUnit = timeUnit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title= title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setIngredient(Integer id, String name, BigDecimal amount, String amountUnit){
        this.ingredients.add(new Ingredient(id, name, amount, amountUnit, this));
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public void addCategory(Category category){
        this.categories.add(category);
    }

    public void removeFromCategory(Category category){
        this.categories.remove(category);
    }

    public boolean hasCategory(String categoryName){
        Iterator<Category> iterator = categories.iterator();
        while(iterator.hasNext()){
            Category category = iterator.next();
            if(category.getName().toLowerCase().contains(categoryName.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public void addIngredient(String name, BigDecimal amount, String amountUnit){
        this.ingredients.add(new Ingredient(name, amount, amountUnit, this));
    }

    public void addIngredient(Ingredient ingredient){
        this.ingredients.add(ingredient);
    }

    public boolean hasIngredient(String ingredientName){
        Iterator<Ingredient> iterator = ingredients.iterator();
        while(iterator.hasNext()){
            Ingredient ingredient = iterator.next();
            if(ingredient.getName().toLowerCase().contains(ingredientName.toLowerCase())
               || ingredientName.toLowerCase().contains(ingredient.getName().toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public List<ShoppingListItem> getShoppingListItems() {
        return shoppingListItems;
    }

    public void setShoppingListItems(List<ShoppingListItem> shoppingListItems) {
        this.shoppingListItems = shoppingListItems;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "title='" + title + '\'' +
                '}';
    }

    @Transient
    public String getImagePath() {
        if (image == null || id == null) return null;

        return "/MealFlash-recipe-images/" + id + "/" + image;
        //	http://localhost:8080/MealFlash/MealFlash-recipe-images/67/som-tam-salad.jpeg
    }

}
