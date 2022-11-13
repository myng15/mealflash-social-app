package com.mynguyen.projects.MealFlashSocialPlatform.controller;

import com.mynguyen.projects.MealFlashSocialPlatform.MealFlashUserDetails;
import com.mynguyen.projects.MealFlashSocialPlatform.model.*;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.MyFollowerRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.RecipeRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.RoleRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.UserRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.security.oauth.AuthenticationProvider;
import com.mynguyen.projects.MealFlashSocialPlatform.service.SecurityService;
import com.mynguyen.projects.MealFlashSocialPlatform.service.UserService;
import com.mynguyen.projects.MealFlashSocialPlatform.validators.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RecipeRepository recipeRepo;

    @Autowired
    private RecipeController recipeController;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private MyFollowerRepository followerRepo;

    @GetMapping("/users")
    public String listUsers(Model model){
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);

        return "users";
    }

    @GetMapping("/users/save")
    public String redirectAfterFailedSave(Authentication authentication, Model model){
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("user", new User());
            return "redirect:/login";
        }
        else {
            MealFlashUserDetails userPrincipal = (MealFlashUserDetails) authentication.getPrincipal();
            User user = userRepo.findById(userPrincipal.getId()).get();
            model.addAttribute("user", user);
            return "redirect:/user-profile";
        }
    }

    @InitBinder("user")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userValidator);
        //or binder.setValidator(emailValidator); if there is only one validator.
        // Since there are other validators in the entity class (with annotation sign "@") besides the custom validator "userValidator", addValidators() MUST be used.
        // setValidator() clears existing (default) validators so @Valid annotation won't work as expected. addValidators() can take multiple validators as parameters separated by commas.
    }

    @PostMapping("users/save")
    public String saveUser(Authentication authentication, @Valid User user, BindingResult result, //BindingResult must come right after the model object that is validated or else Spring will fail to validate the object and throw an exception.
                           HttpServletRequest request,
                           Model model, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("alertClass", "alert-danger");

        if (result.hasErrors()) {
//            model.addAttribute("user", user);
            return "register-form";
        }
        else if (authentication != null) {
            MealFlashUserDetails userDetails = (MealFlashUserDetails) authentication.getPrincipal();
            User userPrincipal = userRepo.findById(userDetails.getId()).get();

            if (user.getId() != null && user.getId() == userPrincipal.getId()) {
                User editedUser = saveEdits(userPrincipal, request);
                model.addAttribute("user", editedUser);

                redirectAttributes.addFlashAttribute("successMessage", "Profile has been updated.");
                redirectAttributes.addFlashAttribute("alertClass", "alert-success");

                return "redirect:/user-profile";
            }
        }
        else if (authentication == null && user.getId() == null){
            userService.saveUserAfterLocalRegistration(user);
//            securityService.authWithAuthManager(request, user.getEmail(), user.getPassword());
            redirectAttributes.addFlashAttribute("successMessage", "Hi " + user.getFname() + ", you have been " +
                    "successfully " +
                    "registered.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        }
            return "redirect:/login";
    }

    @GetMapping(value = {"/user-profile"})
    public String viewUserProfile(@AuthenticationPrincipal MealFlashUserDetails userPrincipal, Model model){
        User user = userRepo.findById(userPrincipal.getId()).get();
        model.addAttribute("user", user);

        return "user-profile";
    }

//    @GetMapping(value = {"/users/saveEdits"})
//    public String viewUserProfileAfterEditing(@AuthenticationPrincipal MealFlashUserDetails userPrincipal,
//                                              Model model){
//        User user = userRepo.findById(userPrincipal.getId()).get();
//        model.addAttribute("user", user);
//
//        return "redirect:/user-profile";
//    }

//    @GetMapping("/users/my-recipes")
//    public String showRecipesofUserForm(@AuthenticationPrincipal MealFlashUserDetails userPrincipal, Model model){
////        User user = userRepo.findById(id).get();
//        User user = userRepo.findById(userPrincipal.getId()).get();
//        model.addAttribute("user", user);
//
////        List<Recipe> recipesFromCreator = recipeController.getRecipesFromCreator(id);
//        List<Recipe> recipesFromCreator = user.getRecipes();
//        model.addAttribute("recipeList", recipesFromCreator);
//
//        return "user-recipes";
//    }

    @GetMapping("/users/recipes-by/{id}")
    public String showRecipesofUserForm(@PathVariable("id") Integer id,
                                        Model model){
        User user = userRepo.findById(id).get();
        model.addAttribute("user", user);

        List<Recipe> recipesFromCreator = user.getRecipes();
        model.addAttribute("recipes", recipesFromCreator);

        return "user-recipes";
    }

    @GetMapping("/users/follow/{username}")
    public String followUser(@PathVariable("username") String username,
                             @AuthenticationPrincipal MealFlashUserDetails userPrincipal,
                             Model model, RedirectAttributes redirectAttributes) {
        if (userPrincipal == null) {
            return "login";
        }
        User followedUser = userRepo.getUserByUsername(username);
        User me = userRepo.findById(userPrincipal.getId()).get();
//        if (followedUser.getId() == me.getId()) {
//            redirectAttributes.addFlashAttribute("errorMessage", "You cannot follow yourself.");
//            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
//            return "redirect:/recipes/creator/" + me.getUsername();
//        } else {
        redirectAttributes.addFlashAttribute("successMessage", "You are now following " + followedUser.getUsername() + ".");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        MyFollower follower = new MyFollower(new MyFollower.MyFollowerId(followedUser.getId(), userPrincipal.getId()), followedUser, me);
        followedUser.addFollower(follower);
        followerRepo.save(follower);
        model.addAttribute("userPrincipal", me);
        model.addAttribute("user", followedUser);
        return "redirect:/recipes/creator/" + followedUser.getUsername();
//        }
    }

    @GetMapping("/users/{id}/followers")
    public String listFollowers(@PathVariable("id") Integer id, Model model) {

        List<MyFollower> followers = followerRepo.findAll();
        List<User> myFollowers = new ArrayList<>();
        for (MyFollower follower : followers) {
            if (follower.getMe().getId() == id) {
                myFollowers.add(follower.getMyFollower());
            }
        }

        model.addAttribute("users", myFollowers);
        return "users";
    }

    @GetMapping("/users/{id}/following")
    public String listFollowing(@PathVariable("id") Integer id, Model model) {

        List<MyFollower> followers = followerRepo.findAll();
        List<User> followedByMe = new ArrayList<>();
        for (MyFollower follower : followers) {
            if (follower.getMyFollower().getId() == id) {
                followedByMe.add(follower.getMe());
            }
        }

        model.addAttribute("users", followedByMe);
        return "users";
    }
//    public String showEditRecipeForm(@PathVariable("id") Integer id, Model model){
//        Recipe recipe = recipeRepo.findById(id).get();
//        model.addAttribute("recipe", recipe);
//        List<Category> categories = categoryRepo.findAll();
//        model.addAttribute("categories", categories);
//
//        return "recipe-form";

//    @GetMapping("/users/edit")
//    public String showEditUserForm(@AuthenticationPrincipal MealFlashUserDetails userPrincipal, Model model) {
//        User user = userRepo.findById(userPrincipal.getId()).get();
//        model.addAttribute("user", user);
//
//        List<Role> roles = roleRepo.findAll();
//        model.addAttribute("roles", roles);
//
//        return "user-form";
//    }
    @GetMapping("/users/edit/{id}")
    public String showEditForm(@AuthenticationPrincipal MealFlashUserDetails userPrincipal, @PathVariable("id") Integer id, Model model) {
//        User user = userRepo.findById(id).get();
        User user = userRepo.findById(userPrincipal.getId()).get();
        model.addAttribute("user", user);

//        List<Role> roles = roleRepo.findAll();
//        model.addAttribute("roles", roles);

//        return "user-form";
        return "register-form";
    }

//    @PostMapping("/users/saveEdits")
    @ResponseBody
    public User saveEdits(/*@AuthenticationPrincipal MealFlashUserDetails userPrincipal*/User user,
                            HttpServletRequest request
//                            Model model, BindingResult result,
//                            RedirectAttributes redirectAttributes
                            ){
//        User user = userRepo.findById(userPrincipal.getId()).get();
//        if (result.hasErrors()) {
//            model.addAttribute("user", user);
//
//            List<Role> roles = roleRepo.findAll();
//            model.addAttribute("roles", roles);
//            return "user-form";
//        }

        String[] fname = request.getParameterValues("fname");
        String[] lname = request.getParameterValues("lname");
        String[] username = request.getParameterValues("username");
        String[] password = request.getParameterValues("password");
        String newPassword = password[0];
//        String[] email = request.getParameterValues("email");

        user.setFname(fname[0]);
        user.setLname(lname[0]);
        user.setEmail(user.getEmail());
        user.setUsername(username[0]);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setEnabled(true);
//        user.setAuthProvider(AuthenticationProvider.LOCAL);
        user.setAuthProvider(user.getAuthProvider());
        user.setRoles(user.getRoles());
//        userRepo.save(user);
        User savedUser = userRepo.save(user);
        return savedUser;
//        model.addAttribute("user", user);
//        redirectAttributes.addFlashAttribute("successMessage", "Profile has been updated.");
//        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

//        return "redirect:/user-profile";
    }
//    public String saveEdits(User user, Model model){
//        user.setEnabled(true);
//        user.setAuthProvider(AuthenticationProvider.LOCAL);
//        userRepo.save(user);
//        model.addAttribute("user", user);
//
//        return "user-profile";
//    }
    @GetMapping("/users/admin/{id}")
    public String assignAdminRole(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        User user = userRepo.findById(id).get();
        Role admin = roleRepo.findByTitle("Admin");
        user.addRole(admin);
        userRepo.save(user);
        redirectAttributes.addFlashAttribute("adminMessage", user.getFname() + " is now an " + user.getRoles());
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userRepo.deleteById(id);

        return "redirect:/users";
    }


}
