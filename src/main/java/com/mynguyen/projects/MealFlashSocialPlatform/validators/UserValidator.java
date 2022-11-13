package com.mynguyen.projects.MealFlashSocialPlatform.validators;

import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    @Autowired
    UserRepository userRepo;

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
//        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "","Email is empty");
        if (user.getId() == null && userRepo.getUserByEmail(user.getEmail()) != null) {
            errors.rejectValue("email","", "This email is already in use.");
        }
        if (user.getId() == null && userRepo.getUserByUsername(user.getUsername()) != null) {
            errors.rejectValue("username","", "This username is already in use.");
        }
    }
}
