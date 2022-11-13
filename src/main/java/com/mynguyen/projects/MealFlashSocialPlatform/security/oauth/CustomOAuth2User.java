package com.mynguyen.projects.MealFlashSocialPlatform.security.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private OAuth2User oAuth2User;
    private String clientName;

    public CustomOAuth2User(OAuth2User oAuth2User, String clientName) {
        this.oAuth2User = oAuth2User;
        this.clientName = clientName;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getAttribute("name");
    }

    public String getFullName() {
        return oAuth2User.getAttribute("name");
    }

    public String getUsername() {
        String email = oAuth2User.getAttribute("email");
        String username = email.substring(0, email.indexOf("@"));
        return username;
    }

    public String getEmail(){
        return oAuth2User.getAttribute("email");
    }

//    public Integer getId(){
//        User user = userRepo.getUserByEmail(this.getEmail());
//        return user.getId();
//    }

    public String getClientName() {
        return this.clientName;
    }

}
