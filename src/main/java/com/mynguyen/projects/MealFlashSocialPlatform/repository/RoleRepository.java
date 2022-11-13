package com.mynguyen.projects.MealFlashSocialPlatform.repository;

import com.mynguyen.projects.MealFlashSocialPlatform.model.Role;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByTitle(String title); //the method name must include the exact name of the
    // corresponding parameter of the Role class that is referred to. i.e. findByTitle and not e
    // .g. findByRoleTitle because Role class doesn't have any parameter named "roleTitle"

}
