package com.mynguyen.projects.MealFlashSocialPlatform.controller;

import com.mynguyen.projects.MealFlashSocialPlatform.model.Category;
import com.mynguyen.projects.MealFlashSocialPlatform.model.Recipe;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.CategoryRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.RecipeRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.validators.CategoryValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private CategoryValidator categoryValidator;

    @GetMapping("/categories")
    public String listCategories(Model model){
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        return "categories";
    }

    @GetMapping("/categories/new")
    public String showNewCategoryForm(Model model){
        model.addAttribute("category", new Category());

        return "category-form";
    }

    @InitBinder("category")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(categoryValidator);
    }

    @PostMapping("/categories/save")
    public String saveNewCategory(@Valid Category category, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "category-form";
        }
        else {
            categoryRepo.save(category);
            return "redirect:/categories";
        }
    }
}
