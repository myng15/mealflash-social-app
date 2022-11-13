package com.mynguyen.projects.MealFlashSocialPlatform.controller;

import com.mynguyen.projects.MealFlashSocialPlatform.MealFlashUserDetails;
import com.mynguyen.projects.MealFlashSocialPlatform.model.*;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.*;
import com.mynguyen.projects.MealFlashSocialPlatform.security.oauth.CustomOAuth2User;
import com.mynguyen.projects.MealFlashSocialPlatform.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RecipeController {
    @Autowired
    private RecipeRepository recipeRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private IngredientRepository ingredientRepo;

    @Autowired
    private ShoppingListItemRepository shoppingListItemRepo;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/recipes")
    public String listRecipes(Model model) {
        List<Recipe> recipes = recipeRepo.findAll();

        model.addAttribute("recipes", recipes);

        return "recipes";
    }

    @GetMapping("/recipe-{id}")
    public String showRecipe(@PathVariable("id") Integer recipeID,
                             @AuthenticationPrincipal MealFlashUserDetails userPrincipal,
                             Model model, RedirectAttributes redirectAttributes) {
        if (userPrincipal == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Log in to view details and do more with " +
                    "MealFlash recipes.");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            return "redirect:/login";
        }
            Recipe recipe = recipeRepo.findById(recipeID).get();
            model.addAttribute("recipe", recipe);
            User creator = recipe.getCreator();
            model.addAttribute("creator", creator);

            return "recipe";

    }

    @GetMapping("/recipes/category/{categoryID}")
    public String showRecipesByCategory(@PathVariable("categoryID") Integer categoryID,
                                        Model model){
        Category category = categoryRepo.findById(categoryID).get();
        model.addAttribute("category", category);

        List<Recipe> recipes = recipeRepo.findAll();
        List<Recipe> recipesOfCategory = recipes.stream()
                                                .filter(recipe -> recipe.hasCategory(category.getName()))
                                                .collect(Collectors.toList());
        model.addAttribute("recipes", recipesOfCategory);

        return "category-recipes";
    }

    @GetMapping("/recipes/ingredient/{ingredientID}")
    public String showRecipesByIngredient(@PathVariable("ingredientID") Integer ingredientID,
                                        Model model){
        Ingredient ingredient = ingredientRepo.findById(ingredientID).get();
        model.addAttribute("ingredient", ingredient);

        List<Recipe> recipes = recipeRepo.findAll();
        List<Recipe> recipesWithIngredient = recipes.stream()
                                                    .filter(recipe -> recipe.hasIngredient(ingredient.getName()))
                                                    .collect(Collectors.toList());
        model.addAttribute("recipes", recipesWithIngredient);

        return "ingredient-recipes";
    }

    @GetMapping("/recipes/new")
    public String showNewRecipeForm(@AuthenticationPrincipal MealFlashUserDetails userPrincipal, Model model) {
//        User user = userRepo.findById(userPrincipal.getId()).get();
//        model.addAttribute("user", user);

        Recipe recipe = new Recipe();
        for (int i = 0; i < 3; i++) {
            recipe.addIngredient(new Ingredient());
        }
        model.addAttribute("recipe", recipe);

        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        return "recipe-form";
    }

//    @RequestMapping(value="/recipes/new", params={"addIngredient"})
//    public String addIngredientRow(Recipe recipe, BindingResult bindingResult, Model model) {
//        recipe.getIngredients().add(new Ingredient());
//        model.addAttribute("recipe", recipe);
//
//        List<Category> categories = categoryRepo.findAll();
//        model.addAttribute("categories", categories);
//
//        return "recipe-form";
//    }
//
//    @RequestMapping(value="/recipes/new", params={"removeIngredient"})
//    public String removeIngredientRow(Recipe recipe, BindingResult bindingResult,
//                                      HttpServletRequest request, Model model) {
//        Integer rowId = Integer.valueOf(request.getParameter("removeIngredient"));
//        recipe.getIngredients().remove(rowId.intValue());
//        model.addAttribute("recipe", recipe);
//
//        List<Category> categories = categoryRepo.findAll();
//        model.addAttribute("categories", categories);
//
//        return "recipe-form";
//    }

    private static final String AJAX_HEADER_NAME = "X-Requested-With";
    private static final String AJAX_HEADER_VALUE = "XMLHttpRequest";
    @PostMapping(params = "addIngredient", path = {"/recipes/new", "/recipes/edit/{id}"})
    public String addIngredient(Recipe recipe, Model model,
                                HttpServletRequest request) {
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        recipe.getIngredients().add(new Ingredient());

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME))) {
            // It is an Ajax request, render only #items fragment of the page.
            return "recipe-form::#items";
        } else {
            // It is a standard HTTP request, render whole page.
            return "recipe-form";
        }
//        return "recipe-form";
}

    // "removeItem" parameter contains index of an item that will be removed.
    @PostMapping(params = "removeIngredient", path = {"/recipes/new", "/recipes/edit/{id}"})
    public String removeIngredient(Recipe recipe, @RequestParam("removeIngredient") int index,
                                HttpServletRequest request, Model model) {
        recipe.getIngredients().remove(index);

        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);

        if (AJAX_HEADER_VALUE.equals(request.getHeader(AJAX_HEADER_NAME))) {
            return "recipe-form::#items";
        } else {
            return "recipe-form";
        }
    }

//    public static String uploadDirectory = System.getProperty("user.dir") + "/src/main/resources/recipe-images"; //
//    (Path above is relative to the application directory)
    @PostMapping(value="/recipes/new", params = "save")
    public String saveRecipe(@AuthenticationPrincipal MealFlashUserDetails userPrincipal,
                             Recipe recipe,
                             HttpServletRequest request,
                             Model model,
//                             @PathVariable("imageFileName") String imageFileName,
//                             @Param("imageFileName") String imageFileName,
                             @RequestParam("imageFile") MultipartFile multipartFile) throws IOException {
        String[] ingredientIDs = request.getParameterValues("ingredientID");

//      ***If number of ingredient inputs is static
//      and each has the parameter name="ingredientName"/"ingredientAmount"/"ingredientAmountUnit":
//      String[] ingredientNames = request.getParameterValues("ingredientName");
//      String[] ingredientAmounts = request.getParameterValues("ingredientAmount");
//      String[] ingredientAmountUnits = request.getParameterValues("ingredientAmountUnit");

//      ***If ingredient inputs can be dynamically added/removed by the client
//      and each has the parameter name="ingredient[\d+].name"/"ingredient[\d+].amount"/"ingredient[\d+].amountUnit":
//      First thought: passing in Regex to get the collection of parameter values
//      Ex: request.getParameterValues("^ingredients\\[\\d+\\]\\.name$")
//      Most methods (that do accept a string) however don't take regex as parameters.
//      Regex can only be used with methods e.g. as Pattern instances or as strings (like String#replaceAll).

        Enumeration parameterNames = request.getParameterNames();
        List<String> parameterNamesList = Collections.list(parameterNames);
//        List<String> ingredientIDs = new ArrayList<>();
        List<String> ingredientNames = new ArrayList<>();
        List<String> ingredientAmounts = new ArrayList<>();
        List<String> ingredientAmountUnits = new ArrayList<>();
        for( String param : parameterNamesList){
//            if( param.endsWith(".id") ){
//                String parameterValue = request.getParameter(param);
//                ingredientIDs.add(parameterValue);
//            }
            if( param.endsWith(".name") ){
                String parameterValue = request.getParameter(param);
                ingredientNames.add(parameterValue);
            }
            if( param.endsWith(".amount") ){
                String parameterValue = request.getParameter(param);
                ingredientAmounts.add(parameterValue);
            }
            if( param.endsWith(".amountUnit") ){
                String parameterValue = request.getParameter(param);
                ingredientAmountUnits.add(parameterValue);
            }
        }

        for(int i = 0; i < ingredientNames.size(); i++) {
            // Edit mode
            if (ingredientIDs != null && ingredientIDs.length > 0) {
                if (ingredientIDs[i].length() > 0) {

                    recipe.setIngredient(Integer.valueOf(ingredientIDs[i]), ingredientNames.get(i),
                            BigDecimal.valueOf(Double.parseDouble(ingredientAmounts.get(i))), ingredientAmountUnits.get(i));
                    recipeRepo.save(recipe);
                    // reference of the new ingredient to the recipe has been done when defining this method in Recipe entity class.
                } else {
                    recipe.addIngredient(ingredientNames.get(i), BigDecimal.valueOf(Double.parseDouble(ingredientAmounts.get(i))),
                            ingredientAmountUnits.get(i));
                }
            }
            // New Recipe mode
            else {
                recipe.addIngredient(ingredientNames.get(i), BigDecimal.valueOf(Double.parseDouble(ingredientAmounts.get(i))),
                        ingredientAmountUnits.get(i));
                // reference of the new ingredient to the recipe has been done when defining this method in Recipe entity class.
            }
        }
//        String imageName = "";
////        String imageNameTest = request.getParameter("imageFile");
//        if (multipartFile == null) {
//            if (recipe.getId() != null && (recipe.getImage() != null && recipe.getImage() != "")) {
////            String[] imageNames = request.getParameterValues("imageFile");
////            imageName = imageNames[0];
////            imageName = recipe.getImage();
//                imageName = imageFileName;
//            }
//        } else {
//            imageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//        }
//        recipe.setImage(imageName);

        User user = userRepo.findById(userPrincipal.getId()).get();
        recipe.setCreator(user);

        Recipe savedRecipe = recipeRepo.save(recipe);

//      String uploadDir = "./src/main/resources/static/recipe-images/" + savedRecipe.getId();
        // Path "./src/main/resources/static/recipe-images/" is relative to the application directory - placing uploadDir in the application folder ist not recommended

//        String uploadDir = "../MealFlash-recipe-images/" + savedRecipe.getId();
        String uploadDir = "../MealFlash-recipe-images/" + savedRecipe.getId();
        // Path "../MealFlash-recipe-images/" is 1 level up outside of the application directory

        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

//        String testPath = uploadDir + "/" + imageName;

//        InputStream inputStream;
//        try {
//            if (multipartFile == null && imageName != null) {
////                BufferedImage img = ImageIO.read(RecipeController.class.getResource(uploadDir + imageName));
//                inputStream = new FileInputStream(new File(uploadDir + "/" + imageName));
//            } else {
//                inputStream = multipartFile.getInputStream();
//            }
//            Path imagePath = uploadPath.resolve(imageName);
//            Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
//        } catch (IOException e) {
//            throw new IOException("Could not save image file: " + imageName, e);
//        }
//        String imageName = "error";
//        InputStream inputStream;

     if (multipartFile.getSize() > 0) {
            String imageName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            recipe.setImage(imageName);
            recipeRepo.save(recipe);
            model.addAttribute("imageFileName", imageName);
//            inputStream = multipartFile.getInputStream();
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path imagePath = uploadPath.resolve(imageName);
                Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
            }
            catch (IOException e) {
                throw new IOException("Could not save image file: ", e);
            }
        }
//        else {
//            if (recipe.getId() != null && (recipe.getImage() != null && recipe.getImage() != "")){
//                String[] imageNames = request.getParameterValues("imageFile");
//                imageName = imageNames[0];
//                savedRecipe.setImage(imageName);
//                recipeRepo.save(savedRecipe);
//                inputStream = new FileInputStream(new File("/Users/mynguyen15/OneDrive - Otto-Friedrich-Universität Bamberg/PROGRAMMING/Projects/Java/MealFlash-recipe-images/30" + "/" + imageName));
//                Path imagePath = uploadPath.resolve(imageName);
//                Files.copy(inputStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
//            }
//        }



        model.addAttribute("user", user);

        List<Recipe> recipesFromCreator = user.getRecipes();
        model.addAttribute("recipes", recipesFromCreator);

        model.addAttribute("test1", ingredientNames.get(3));
//        model.addAttribute("test2", testPath);
        return "user-recipes";
    }


//    public String saveRecipe(@PathVariable("username") String username, Recipe recipe,
//                             HttpServletRequest request,
//                             Model model) {
//        String[] ingredientIDs = request.getParameterValues("ingredientID");
//        String[] ingredientNames = request.getParameterValues("ingredientName");
//        String[] ingredientAmounts = request.getParameterValues("ingredientAmount");
//        String[] ingredientAmountUnits = request.getParameterValues("ingredientAmountUnit");
//
//        for(int i = 0; i < ingredientNames.length; i++) {
//            if (ingredientIDs != null && ingredientIDs.length > 0) {
//                recipe.setIngredient(Integer.valueOf(ingredientIDs[i]), ingredientNames[i], Float.valueOf(ingredientAmounts[i]), ingredientAmountUnits[i]);
//            } else {
//                recipe.addIngredient(ingredientNames[i], Float.parseFloat(ingredientAmounts[i]), ingredientAmountUnits[i]);
//            }
//        }
//
////        System.out.println(userPrincipal.getUsername());
//        User user = userRepo.getUserByUsername(username);
//        recipe.setCreator(user);
//        recipeRepo.save(recipe);
//        user.addRecipe(recipe);
//
//        model.addAttribute("user", user);
//
//        List<Recipe> recipesFromCreator = user.getRecipes();
//
////        User user = userRepo.getUserByEmail(userPrincipal.getEmail());
////        model.addAttribute("user", user);
//
//        model.addAttribute("recipeList", recipesFromCreator);
//
//        return "user-recipes";
//    }
//    public String saveRecipe(@PathVariable("id") Integer id, Recipe recipe, HttpServletRequest request,
//     Model model) {
//        String[] ingredientIDs = request.getParameterValues("ingredientID");
//        String[] ingredientNames = request.getParameterValues("ingredientName");
//        String[] ingredientAmounts = request.getParameterValues("ingredientAmount");
//        String[] ingredientAmountUnits = request.getParameterValues("ingredientAmountUnit");
//
//        for(int i = 0; i < ingredientNames.length; i++) {
//            if (ingredientIDs != null && ingredientIDs.length > 0) {
//                recipe.setIngredient(Integer.valueOf(ingredientIDs[i]), ingredientNames[i], Float.valueOf(ingredientAmounts[i]), ingredientAmountUnits[i]);
//            } else {
//                recipe.addIngredient(ingredientNames[i], Float.parseFloat(ingredientAmounts[i]), ingredientAmountUnits[i]);
//            }
//        }
//        String[] creatorIDs = request.getParameterValues("creatorID");
//        User user = userRepo.findById(Integer.valueOf(creatorIDs[0])).get();
//        recipeRepo.save(recipe);
//
////        User user = userRepo.findById(creatorId).get();
////        user.addRecipe(recipe);
////        recipe.setCreator(user);
//
//        model.addAttribute("user", user);
//        List<Recipe> recipesFromCreator = getRecipesFromCreator(Integer.valueOf(creatorIDs[0]));
//        model.addAttribute("recipeList", recipesFromCreator);
//
//        return "user-recipes";
////        return "redirect:/recipes";
//    }

    @GetMapping("/recipes/edit/{id}")
    public String showEditRecipeForm(@PathVariable("id") Integer id, Model model) {
        Recipe recipe = recipeRepo.findById(id).get();
        model.addAttribute("recipe", recipe);
        List<Category> categories = categoryRepo.findAll();
        model.addAttribute("categories", categories);
        return "recipe-form";
    }

    @GetMapping("/recipes/delete/{id}")
    public String deleteRecipe(@PathVariable("id") Integer id,
                               @AuthenticationPrincipal MealFlashUserDetails userPrincipal,
                               Model model, RedirectAttributes redirectAttributes) {
        Recipe recipe = recipeRepo.findById(id).get();
        List<Ingredient> ingredientsOfRecipe = recipe.getIngredients();
        ingredientRepo.deleteAll(ingredientsOfRecipe);

        List<ShoppingListItem> shoppingListItemsOfRecipe = recipe.getShoppingListItems();
        shoppingListItemRepo.deleteAll(shoppingListItemsOfRecipe);

        Set<Category> categoriesOfRecipe = recipe.getCategories();
        for (Iterator<Category> iterator = categoriesOfRecipe.iterator(); iterator.hasNext(); ) {
            Category category = iterator.next();
            iterator.remove();
        }

        recipeRepo.deleteById(id);

        User user = userRepo.findById(userPrincipal.getId()).get();
        model.addAttribute("user", user);
        List<Recipe> recipesFromCreator = user.getRecipes();
        model.addAttribute("recipes", recipesFromCreator);

        redirectAttributes.addFlashAttribute("successMessage", "Your \"" + recipe.getTitle() + "\"" + " recipe has been deleted.");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/users/recipes-by/" + user.getId().toString();
    }

    @GetMapping("/recipes/creator/{username}")
    public String viewCreatorProfile(@PathVariable("username") String username,
                                     @AuthenticationPrincipal MealFlashUserDetails userPrincipal, Model model) {
        User user = userRepo.getUserByUsername(username);
        model.addAttribute("user", user);

        if (userPrincipal != null) {
            User me = userRepo.findById(userPrincipal.getId()).get();
            model.addAttribute("userPrincipal", me);
        }
        return "user-profile";
    }


//This method with business logic could be put in Service layer (e.g. RecipeService) but is put in here to keep it simple
    public List<Recipe> getRecipesFromCreator(Integer id) {
        List<Recipe> recipes = recipeRepo.findAll();
        List<Recipe> recipesFromCreator = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getCreator().getId() == id) {
                recipesFromCreator.add(recipe);
            }
        }
        return recipesFromCreator;
    }

}

