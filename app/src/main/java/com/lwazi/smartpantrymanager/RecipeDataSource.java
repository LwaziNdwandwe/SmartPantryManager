package com.lwazi.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
public class RecipeDataSource {

    private SQLiteDatabase database;
    private PantryDB dbHelper;


    public RecipeDataSource(Context context){
        dbHelper = new PantryDB(context);
    }

    public void open(){
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    //Adding new recipe without its ingredients yet
    public Recipe addRecipe(String name, String instructions){
        ContentValues values = new ContentValues();
        values.put(PantryDB.COLUMN_RECIPE_NAME, name);
        values.put(PantryDB.COLUMN_INSTRUCTIONS, instructions);

        long insertId = database.insert(PantryDB.TABLE_RECIPES, null, values);

        Recipe newRecipe = new Recipe(name, instructions);
        newRecipe.setId((int) insertId);
        return newRecipe;
    }

    //Adding one ingredient requirement linked to a recipe by its id
    public RecipeIngredient addRecipeIngredient(int recipeId, String ingredientName, double quantityRequired, String unit) {
        ContentValues values = new ContentValues();
        values.put(PantryDB.COLUMN_RI_RECIPE_ID, recipeId); //this links this row back to the recipe
        values.put(PantryDB.COLUMN_RI_INGREDIENT_NAME, ingredientName);
        values.put(PantryDB.COLUMN_RI_QUANTITY_REQUIRED, quantityRequired);
        values.put(PantryDB.COLUMN_RI_UNIT, unit);

        long insertId = database.insert(PantryDB.TABLE_RECIPE_INGREDIENTS, null, values);

        RecipeIngredient newRI = new RecipeIngredient(recipeId, ingredientName, quantityRequired, unit);
        newRI.setId((int) insertId);
        return newRI;
    }

    //Reads every recipe from the database
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        Cursor cursor = database.query(PantryDB.TABLE_RECIPES,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Recipe recipe = cursorToRecipe(cursor);
            recipes.add(recipe);
            cursor.moveToNext();
        }
        cursor.close();

        return recipes;
    }

    private Recipe cursorToRecipe(Cursor cursor) {
        Recipe recipe = new Recipe(
                cursor.getString(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_RECIPE_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_INSTRUCTIONS)));
        recipe.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_RECIPE_ID)));
        return recipe;
    }

    //Reads only the ingredient requirements belonging to a specific recipe
    public List<RecipeIngredient> getIngredientsForRecipe(int recipeId) {
        List<RecipeIngredient> ingredients = new ArrayList<>();

        String selection = PantryDB.COLUMN_RI_RECIPE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(recipeId) };

        Cursor cursor = database.query(PantryDB.TABLE_RECIPE_INGREDIENTS,
                null, selection, selectionArgs, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RecipeIngredient ri = cursorToRecipeIngredient(cursor);
            ingredients.add(ri);
            cursor.moveToNext();
        }
        cursor.close();

        return ingredients;
    }

    private RecipeIngredient cursorToRecipeIngredient(Cursor cursor) {
        RecipeIngredient ri = new RecipeIngredient(
                cursor.getInt(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_RI_RECIPE_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_RI_INGREDIENT_NAME)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_RI_QUANTITY_REQUIRED)),
                cursor.getString(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_RI_UNIT)));
        ri.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_RI_ID)));
        return ri;
    }

    //Deletes a recipe and all of its ingredient requirements
    public void deleteRecipe(Recipe recipe) {
        int recipeId = recipe.getId();
        //deletes the "child" rows first
        //points at a recipe that no longer exists
        database.delete(PantryDB.TABLE_RECIPE_INGREDIENTS,
                PantryDB.COLUMN_RI_RECIPE_ID + " = " + recipeId, null);
        //now it deletes the recipe itself
        database.delete(PantryDB.TABLE_RECIPES,
                PantryDB.COLUMN_RECIPE_ID + " = " + recipeId, null);
    }
}
