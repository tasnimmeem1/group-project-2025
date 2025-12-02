package com.group.project;

public class RecipeModel {
    private final String id;
    private final String name;
    private final String cuisine;
    private final String protein;
    private final String time;
    private final String difficulty;
    private final String details;
    private final String ingredients;

    // Old style constructor (details only) still works
    public RecipeModel(String id,
                       String name,
                       String cuisine,
                       String protein,
                       String time,
                       String difficulty,
                       String details) {
        this(id, name, cuisine, protein, time, difficulty, details, "");
    }

    // Full constructor with ingredients
    public RecipeModel(String id,
                       String name,
                       String cuisine,
                       String protein,
                       String time,
                       String difficulty,
                       String details,
                       String ingredients) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.protein = protein;
        this.time = time;
        this.difficulty = difficulty;
        this.details = details;
        this.ingredients = ingredients;
    }

    public String getId()         { return id; }
    public String getName()       { return name; }
    public String getCuisine()    { return cuisine; }
    public String getProtein()    { return protein; }
    public String getTime()       { return time; }
    public String getDifficulty() { return difficulty; }
    public String getDetails()    { return details; }
    public String getIngredients(){ return ingredients; }

    @Override
    public String toString() {
        return name == null ? "" : name;
    }
}
