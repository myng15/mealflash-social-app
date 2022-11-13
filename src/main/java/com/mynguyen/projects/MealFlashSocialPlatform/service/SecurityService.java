package com.mynguyen.projects.MealFlashSocialPlatform.service;

import com.mynguyen.projects.MealFlashSocialPlatform.MealFlashUserDetails;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

//Automatically log in after successful registration
@Service
public class SecurityService {
    @Autowired
    @Lazy
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    private Logger logger = LoggerFactory.getLogger(SecurityService.class);

    public void authWithAuthManager(HttpServletRequest request, String email, String password) {
        MealFlashUserDetails userDetails = (MealFlashUserDetails) userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                password, userDetails.getAuthorities());
//        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);

        if (authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.debug(String.format("Auto login %s successfully!", email));
        }

    }

    public void authWithHttpServletRequest(HttpServletRequest request, String email, String password) {
        try {
            request.login(email, password);
        } catch (ServletException e) {
            logger.error("Error while login ", e);
        }
    }


}
