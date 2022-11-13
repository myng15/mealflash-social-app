package com.mynguyen.projects.MealFlashSocialPlatform.service;

import com.mynguyen.projects.MealFlashSocialPlatform.security.oauth.AuthenticationProvider;
import com.mynguyen.projects.MealFlashSocialPlatform.model.Role;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.RoleRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public void saveUserAfterLocalRegistration(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setAuthProvider(AuthenticationProvider.LOCAL);
        Role role = roleRepo.findByTitle("Owner");
        user.setRoles(new HashSet<Role>(Arrays.asList(role)));
        userRepo.save(user);
    }

//    public void createNewUserAfterOAuthLoginSuccess(String email, String fname, String lname,
//                                                    AuthenticationProvider provider){
//        User user = new User();
//        user.setEmail(email);
//        user.setFname(fname);
//        user.setLname(lname);
//        user.setEnabled(true);
//        user.setAuthProvider(provider);
//
//        userRepo.save(user);
//    };

    public void createNewUserAfterOAuthLoginSuccess(String email, String fname, String lname,
                                                    AuthenticationProvider provider){
        User user = new User();
        user.setEmail(email);
        user.setFname(fname);
        user.setLname(lname);
        user.setEnabled(true);
        user.setAuthProvider(provider);
//        Role role = roleRepo.findByTitle("Owner");
//        user.setRoles(new HashSet<Role>(Arrays.asList(role)));

        userRepo.save(user);
    };

    public void updateUserAfterOAuthLoginSuccess(User user, String fname, String lname,
                                                 AuthenticationProvider provider) {
        user.setFname(fname);
        user.setLname(lname);
        user.setAuthProvider(provider);

        userRepo.save(user);
    }
}
