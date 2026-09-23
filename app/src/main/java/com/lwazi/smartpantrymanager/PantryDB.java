package com.lwazi.smartpantrymanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PantryDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantryManager.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";

    //Recipes table
    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_INSTRUCTIONS  = "instructions";

    //RecipeIngredients table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COLUMN_RI_ID = "id"; // this row's own primary key
    public static final String COLUMN_RI_RECIPE_ID = "recipe_id"; // which recipe this ingredient requirement belongs to
    public static final String COLUMN_RI_INGREDIENT_NAME = "ingredient_name";
    public static final String COLUMN_RI_QUANTITY_REQUIRED = "quantity_required";
    public static final String COLUMN_RI_UNIT = "unit";


    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_INGREDIENTS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_QUANTITY + " REAL NOT NULL, " +
                    COLUMN_UNIT + " TEXT NOT NULL);";

    //SQL query creating recipe table
    private static final String RECIPE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_RECIPES + "(" + COLUMN_RECIPE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RECIPE_NAME + " TEXT NOT NULL, " +
                    COLUMN_INSTRUCTIONS + " TEXT);";

    //Sql query creating recipe ingredients table
    private static final String RECIPE_INGREDIENTS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + "(" +
                    COLUMN_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                    COLUMN_RI_INGREDIENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_RI_QUANTITY_REQUIRED + " REAL NOT NULL, " +
                    COLUMN_RI_UNIT + " TEXT NOT NULL, " +
                    "FOREIGN KEY(" + COLUMN_RI_RECIPE_ID + ") REFERENCES " +
                    TABLE_RECIPES + "(" + COLUMN_RECIPE_ID + "));";

    public PantryDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);//creates the ingredients table
        db.execSQL(RECIPE_TABLE_CREATE);//creates recipe table
        db.execSQL(RECIPE_INGREDIENTS_TABLE_CREATE);//creates the recipe ingredients table
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        onCreate(db);
    }

}
