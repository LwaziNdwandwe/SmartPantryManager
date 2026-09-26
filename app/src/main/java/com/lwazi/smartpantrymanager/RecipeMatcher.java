package com.lwazi.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

//this class is the "smart" matching logic for the application

public class RecipeMatcher {

    //it takes everything currently in the pantry and the RecipeDataSource
    //returns only the recipes that can be fully made
    public static List<Recipe> getSuggestedRecipes(List<Recipe> allRecipes, List<Ingredient> pantryIngredients, RecipeDataSource recipeDataSource) {

        List<Recipe> suggestedRecipes = new ArrayList<>(); //this will hold only the recipes that fully match

        //checks every recipe one at a time
        for (Recipe recipe : allRecipes) {

            //gets the list of ingredients this specific recipe needs
            List<RecipeIngredient> requiredIngredients = recipeDataSource.getIngredientsForRecipe(recipe.getId());

            boolean canMakeRecipe = true; //this is true until proven otherwise

            //checks every ingredient this recipe requires
            for (RecipeIngredient required : requiredIngredients) {

                Ingredient matchingPantryIngredient = null; //this holds the pantry match if one exists

                //searches the pantry for an ingredient with the same name
                for (Ingredient pantryItem : pantryIngredients) {
                    if (pantryItem.getName().equalsIgnoreCase(required.getIngredientName())) {
                        matchingPantryIngredient = pantryItem;
                        break;
                    }
                }

                //if no matching ingredient was found in the pantry at all this recipe fails
                if (matchingPantryIngredient == null) {
                    canMakeRecipe = false;
                    break;
                }

                //if the units don't match exactly it does not check the quantity or unit
                if (!matchingPantryIngredient.getUnit().equalsIgnoreCase(required.getUnit())) {
                    canMakeRecipe = false;
                    break;
                }

                //if the pantry does not have enough of this ingredient this recipe fails
                if (matchingPantryIngredient.getQuantity() < required.getQuantityRequired()) {
                    canMakeRecipe = false;
                    break;
                }
            }

            //it only adds this recipe to the results if every ingredients passed
            if (canMakeRecipe) {
                suggestedRecipes.add(recipe);
            }
        }

        return suggestedRecipes;
    }
}