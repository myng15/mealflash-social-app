package com.mynguyen.projects.MealFlashSocialPlatform.repository;

import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.username = :username")
    User getUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User getUserByEmail(String email);

}
