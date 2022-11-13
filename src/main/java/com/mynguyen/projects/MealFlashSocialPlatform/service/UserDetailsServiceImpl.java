package com.mynguyen.projects.MealFlashSocialPlatform.service;

import com.mynguyen.projects.MealFlashSocialPlatform.MealFlashUserDetails;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);

        if(user == null){
            throw new UsernameNotFoundException("User with email address" + email + "could not be" +
                    "found" +
                    ".");
        }
        return new MealFlashUserDetails(user);
    }

//    @Override
//    if using login with username
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.getUserByUsername(username);
//
//        if(user == null){
//            throw new UsernameNotFoundException("User is not found.");
//        }
//        return new MealFlashUserDetails(user);
//    }

}
