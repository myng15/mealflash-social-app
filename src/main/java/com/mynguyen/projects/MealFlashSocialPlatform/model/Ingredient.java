package com.mynguyen.projects.MealFlashSocialPlatform.model;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, name = "name", nullable = false)
    @NotEmpty(message = "Ingredient name cannot be empty.")
    private String name;

    @Column(name = "amount", nullable = false)
    @DecimalMin(value = "0.5", message = "Minimum value is 0.5")
    @Digits(integer = 3, fraction = 2, message = "Expected number format: XXX.XX") //validates the number of digits in
    // the integral part and fraction part of a decimal number.
    private BigDecimal amount;

    @Column(length = 45, name = "amount_unit", nullable = false)
    private String amountUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;

    public Ingredient(){

    }

    public Ingredient(Integer id, String name, BigDecimal amount, String amountUnit, Recipe recipe) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.amountUnit = amountUnit;
        this.recipe = recipe;
    }

    public Ingredient(String name, BigDecimal amount, String amountUnit, Recipe recipe) {
        this.name = name;
        this.amount = amount;
        this.amountUnit = amountUnit;
        this.recipe = recipe;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + ": " + amount + " " + amountUnit;
    }

}
