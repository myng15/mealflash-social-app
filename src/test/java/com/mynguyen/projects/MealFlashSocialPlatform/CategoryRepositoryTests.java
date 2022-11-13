package com.mynguyen.projects.MealFlashSocialPlatform;

import com.mynguyen.projects.MealFlashSocialPlatform.model.Category;
import com.mynguyen.projects.MealFlashSocialPlatform.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CategoryRepositoryTests {
    @Autowired
    private CategoryRepository repo;

    @Test
    public void testCreateCategory(){
//Test with plain DataJpaTest .save()
        Category category = repo.save(new Category("barbecue"));
        assertThat(category.getId()).isGreaterThan(0);
    }
}
