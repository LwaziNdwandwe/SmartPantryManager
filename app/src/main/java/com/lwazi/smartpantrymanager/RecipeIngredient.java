package com.lwazi.smartpantrymanager;

public class RecipeIngredient {

    private int id;
    private int recipeId;
    private String ingredientName;
    private double quantityRequired;
    private String unit;


    //constructor
    public RecipeIngredient(int recipeId, String ingredientName, double quantityRequired, String unit){
        this.recipeId = recipeId;
        this.ingredientName = ingredientName;
        this.quantityRequired = quantityRequired;
        this.unit = unit;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public double getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(double quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
