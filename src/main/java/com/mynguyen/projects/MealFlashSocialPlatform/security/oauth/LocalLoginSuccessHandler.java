package com.mynguyen.projects.MealFlashSocialPlatform.security.oauth;

import com.mynguyen.projects.MealFlashSocialPlatform.MealFlashUserDetails;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.UserRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LocalLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        MealFlashUserDetails userDetails = (MealFlashUserDetails) authentication.getPrincipal();

        String redirectURL = request.getContextPath();

        if (userDetails.hasRole("Admin")) {
            redirectURL += "/admin";
        } else if (userDetails.hasRole("Owner")) {
            redirectURL += "/";
        }
        response.sendRedirect(redirectURL);
    }



}



