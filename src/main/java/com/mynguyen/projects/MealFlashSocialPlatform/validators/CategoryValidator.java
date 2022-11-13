package com.mynguyen.projects.MealFlashSocialPlatform.validators;

import com.mynguyen.projects.MealFlashSocialPlatform.model.Category;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class CategoryValidator implements Validator {
    @Autowired
    CategoryRepository categoryRepo;

    @Override
    public boolean supports(Class<?> aClass) {
        return Category.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Category category = (Category) target;
//        Category existingCategory = categoryRepo.findById(category.getId()).get();
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "","Category must be named.");
        if (category.getId() == null && categoryRepo.getCategoryByName(category.getName()) != null) {
            errors.rejectValue("name","", "This category already exists.");
        }
    }
}
