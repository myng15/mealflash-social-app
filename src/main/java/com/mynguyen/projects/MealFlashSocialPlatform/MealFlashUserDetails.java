package com.mynguyen.projects.MealFlashSocialPlatform;

import com.mynguyen.projects.MealFlashSocialPlatform.model.Role;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import com.mynguyen.projects.MealFlashSocialPlatform.security.oauth.AuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class MealFlashUserDetails implements UserDetails {
    private User user;

    public MealFlashUserDetails() {
    }

    public MealFlashUserDetails(User user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles){
            authorities.add(new SimpleGrantedAuthority(role.getTitle()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
//        return this.getFullName();
    }

    public String getEmail() {
        return user.getEmail();
    } //or directly overriding the getUsername() method defined by UserDetails interface i.e.let it
    // return user.getEmail() instead

    public String getFirstName(){
        return user.getFname();
    }

    public String getFullName(){
        return user.getFname() + " " + user.getLname();
    }

    public Integer getId() {
        return user.getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public boolean hasRole(String roleTitle){
        return user.hasRole(roleTitle);
    }


    @Override
    public String toString() {
        return "" + getEmail() +
                "";
    }
}
