package com.example.purebasketbe.domain.recipe;

import com.example.purebasketbe.domain.recipe.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    boolean existsByName(String name);
}
