package com.mynguyen.projects.MealFlashSocialPlatform.controller;

import com.mynguyen.projects.MealFlashSocialPlatform.MealFlashUserDetails;
import com.mynguyen.projects.MealFlashSocialPlatform.model.Category;
import com.mynguyen.projects.MealFlashSocialPlatform.model.Recipe;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.RecipeRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.UserRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.security.oauth.LocalLoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RecipeRepository recipeRepo;

    @Autowired
    private LocalLoginSuccessHandler loginSuccessHandler;

    @GetMapping(value = {"", "/"})
    public String viewHomePage(Authentication authentication, Model model){
        List<Recipe> recipes = recipeRepo.findAll();
        model.addAttribute("recipes", recipes);

        if (authentication != null) {
            MealFlashUserDetails userDetails = (MealFlashUserDetails) authentication.getPrincipal();

            if (userDetails.hasRole("Admin")) {
                return "admin";
            }
        }

        return "index";
    }

    public void redirectAccordingToRole(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication){
        try {
            loginSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = {"/admin"})
    public String viewAdminPage(Model model){
        List<Recipe> recipes = recipeRepo.findAll();
        model.addAttribute("recipes", recipes);
        return "admin";
    }

    @GetMapping("/login")
    public String viewLoginPage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "login";
        }
        return "redirect:/";
//        return "index";
    }

    @GetMapping("/register")
    public String showNewUserForm(Model model){
        model.addAttribute("user", new User());

        return "register-form";
    }

    @GetMapping("/logout")
    public String viewLogoutPage(){
        return "index";
    }

    @GetMapping("/403")
    public String error403(){
        return "403";
    }

   /* @GetMapping("/search")
    public String search(@Param("keyword") String keyword) {
        return "search-results";

    }*/
}
