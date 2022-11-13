package com.mynguyen.projects.MealFlashSocialPlatform.repository;

import com.mynguyen.projects.MealFlashSocialPlatform.model.MyFollower;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface MyFollowerRepository extends JpaRepository<MyFollower, MyFollower.MyFollowerId> {

//    @Query("SELECT u FROM User u WHERE u.username = :username")
//    public User getUserByUsername(String username);
//
//    @Query("SELECT u FROM User u WHERE u.email = :email")
//    public User getUserByEmail(String email);

}
