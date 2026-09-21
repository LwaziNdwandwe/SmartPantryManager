package com.lwazi.smartpantrymanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class PantryDataSource {

    private SQLiteDatabase database;//Where queries are ran
    private PantryDB dbHelper; //Creates and manages the database files

    public PantryDataSource(android.content.Context context) {
        dbHelper = new PantryDB(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();//Opens database for reading and writing
    }

    public void close() {
        dbHelper.close();
    }

    public Ingredient addIngredient(String name, double quantity, String unit) {
        ContentValues values = new ContentValues();//Matching column name to value pairs
        //Passing value into method
        values.put(PantryDB.COLUMN_NAME, name);
        values.put(PantryDB.COLUMN_QUANTITY, quantity);
        values.put(PantryDB.COLUMN_UNIT, unit);

        long insertId = database.insert(PantryDB.TABLE_INGREDIENTS, null, values);//Inserts into new row

        Ingredient newIngredient = new Ingredient(name, quantity, unit);//new object matching the constructor
        newIngredient.setId((int) insertId);
        return newIngredient;
    }


    public List<Ingredient> getAllIngredients() {
        List<Ingredient> ingredients = new ArrayList<>();

        Cursor cursor = database.query(PantryDB.TABLE_INGREDIENTS,
                null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Ingredient ingredient = cursorToIngredient(cursor);
            ingredients.add(ingredient);
            cursor.moveToNext();
        }
        cursor.close();

        return ingredients;
    }

    private Ingredient cursorToIngredient(Cursor cursor) {
        Ingredient ingredient = new Ingredient(
                cursor.getString(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_NAME)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_QUANTITY)),
                cursor.getString(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_UNIT)));
        ingredient.setId(cursor.getInt(cursor.getColumnIndexOrThrow(PantryDB.COLUMN_ID)));
        return ingredient;
    }

    //Deletes a single ingredient row based on its id
    public void deleteIngredient(Ingredient ingredient) {
        int id = ingredient.getId(); //getting the id of the ingredient to delete
        database.delete(PantryDB.TABLE_INGREDIENTS, //this is which table to delete from
                PantryDB.COLUMN_ID + " = " + id, null); //only deletes the row matching this id
    }

    //Updates just the quantity column for an existing ingredient
    public void updateIngredientQuantity(Ingredient ingredient, double newQuantity) {
        ContentValues values = new ContentValues(); //holds the column that is being changed
        values.put(PantryDB.COLUMN_QUANTITY, newQuantity); //only updating quantity

        database.update(PantryDB.TABLE_INGREDIENTS, values,
                PantryDB.COLUMN_ID + " = " + ingredient.getId(), null);

        ingredient.setQuantity(newQuantity);
    }

}
