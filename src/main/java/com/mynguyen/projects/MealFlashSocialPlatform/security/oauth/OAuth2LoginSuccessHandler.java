package com.mynguyen.projects.MealFlashSocialPlatform.security.oauth;

import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
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
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
        /*SavedRequestAwareAuthenticationSuccessHandler*/ {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getEmail();
        String fullName = oAuth2User.getName();
        String fname = fullName.substring(0, fullName.lastIndexOf(" "));
        String lname = fullName.substring(fullName.lastIndexOf(" ") + 1);
        String clientName = oAuth2User.getClientName();
        User user = userRepo.getUserByEmail(email);
        if(user == null){
            //register new user to database
            userService.createNewUserAfterOAuthLoginSuccess(email, fname, lname,
                    AuthenticationProvider.GOOGLE);
        } else {
            // update existing user in database
            userService.updateUserAfterOAuthLoginSuccess(user, fname, lname,
                    AuthenticationProvider.GOOGLE);

            String redirectURL = request.getContextPath();

            if (user.hasRole("Admin")) {
                redirectURL += "/admin";
            } else /*if (user.hasRole("Owner"))*/ {
                redirectURL += "/";
            }
            response.sendRedirect(redirectURL);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
