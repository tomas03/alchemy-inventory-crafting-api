package com.RavenDev.inventory_crafting_api.repository;

import com.RavenDev.inventory_crafting_api.domain.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository< Recipe, Long > {
}