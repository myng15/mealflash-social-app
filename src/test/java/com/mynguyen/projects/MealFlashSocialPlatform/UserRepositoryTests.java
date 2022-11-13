package com.mynguyen.projects.MealFlashSocialPlatform;

import com.mynguyen.projects.MealFlashSocialPlatform.model.MyFollower;
import com.mynguyen.projects.MealFlashSocialPlatform.model.Role;
import com.mynguyen.projects.MealFlashSocialPlatform.model.User;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.MyFollowerRepository;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
    @Autowired
    private UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MyFollowerRepository followerRepo;

    @Test
    public void testCreateUser(){
//Test with plain DataJpaTest .save()
        User user = repo.save(new User("Sabine", "Müller", "sabine.müller@gmail.com"));
        assertThat(user.getId()).isGreaterThan(0); /*if id is of type Long, compare id with OL instead of 0*/
    }
    @Test
    public void testCreateRoles(){
        Role admin = new Role("Admin");
        Role owner = new Role("Owner");
        Role follower = new Role("Follower");

        entityManager.persist(admin);
        entityManager.persist(owner);
        entityManager.persist(follower);
    }

    @Test
    public void testCreateUserWithOneRole(){
        Role admin = entityManager.find(Role.class, 1);
        User user = new User("alexa", "siri", true, "Alexa", "Siri", "alexa.siri@ai.com");
        user.addRole(admin);

        repo.save(user);
    }

    @Test
    public void testCreateUserWithMultipleRoles(){
        Role owner = entityManager.find(Role.class, 2);
        Role admin = entityManager.find(Role.class, 1);
        User user = new User("johndoe", "abcde", "John", "Doe", "john.doe@gmail.com");
        user.addRole(owner);
        user.addRole(admin);

        repo.save(user);
    }

    @Test
    public void testAssignRoleToExistingUser(){
        User user = repo.findById(31).get();
        Role admin = entityManager.find(Role.class, 1);
        user.addRole(admin);
    }

    @Test
    public void testFollowUser(){
        User user = repo.findById(30).get();
        User followedUser = repo.findById(40).get();
        MyFollower follower = new MyFollower(new MyFollower.MyFollowerId(40, 30), followedUser, user);
        followedUser.addFollower(follower);
        followerRepo.save(follower);
    }
}
