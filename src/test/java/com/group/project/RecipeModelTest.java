package com.group.project;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeModelTest {
    // 1) Constructor + getters – checks all fields are stored correctly
    @Test
    void constructorAndGetters_shouldReturnValuesPassedInConstructor() {
        RecipeModel recipe = new RecipeModel(
                "1",
                "Chicken Biryani",
                "Bangladeshi",
                "25g",
                "45 min",
                "Medium",
                "Spicy rice with chicken",
                "rice, chicken, spices"
        );

        assertEquals("1", recipe.getId());
        assertEquals("Chicken Biryani", recipe.getName());
        assertEquals("Bangladeshi", recipe.getCuisine());
        assertEquals("25g", recipe.getProtein());
        assertEquals("45 min", recipe.getTime());
        assertEquals("Medium", recipe.getDifficulty());
        assertEquals("Spicy rice with chicken", recipe.getDetails());
        assertEquals("rice, chicken, spices", recipe.getIngredients());
    }

    // 2) toString() – should return the name when name is not null
    @Test
    void toString_shouldReturnNameWhenNotNull() {
        RecipeModel recipe = new RecipeModel(
                "2",
                "Veggie Curry",
                "Indian",
                "10g",
                "30 min",
                "Easy",
                "Simple vegetable curry",
                "potato, carrot, peas"
        );

        String text = recipe.toString();
        assertEquals("Veggie Curry", text);
    }

    // 3) toString() – should return empty string when name is null
    @Test
    void toString_shouldReturnEmptyStringWhenNameIsNull() {
        RecipeModel recipe = new RecipeModel(
                "3",
                null,                    // name is null here
                "Unknown",
                "0g",
                "0 min",
                "Unknown",
                "No details",
                "no ingredients"
        );

        String text = recipe.toString();
        assertEquals("", text);
    }
}