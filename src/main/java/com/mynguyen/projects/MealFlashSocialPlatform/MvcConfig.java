package com.mynguyen.projects.MealFlashSocialPlatform;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
// To upload picture to a folder inside of application folder i.e. the deployed code
// - which shouldn't be done because on server restarts the uploaded images will get erased:
//        exposeDirectory("./src/main/resources/static/recipe-images", registry);

// Instead - Save picture to a folder outside application folder:
        exposeDirectory("../MealFlash-recipe-images", registry);

// exposeDirectory() function is to hide the absolute path instead of doing as follows:
//        String myExternalFilePath = "file:///Users/[Absolute Path]/../MealFlash-recipe-images/";
//        registry.addResourceHandler("/MealFlash-recipe-images/**").addResourceLocations(myExternalFilePath);

// Alternative - add following configuration to application.properties / application.yml
// spring.resources.static-locations = classpath:/static/,file:///Users/[Absolute Path]/../MealFlash-recipe-images/
// (but this way it is also not as convenient to hide the absolute path):
    }

    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");

        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file://"+ uploadPath + "/");
    }
}
