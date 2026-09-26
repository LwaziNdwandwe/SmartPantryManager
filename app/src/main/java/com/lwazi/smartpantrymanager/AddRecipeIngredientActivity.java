package com.lwazi.smartpantrymanager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddRecipeIngredientActivity extends AppCompatActivity {

    private RecipeDataSource dataSource;
    private int recipeId; //the recipe were adding ingredients to

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe_ingredient);

        dataSource = new RecipeDataSource(this);

        //reads back the recipe id that AddRecipeActivity attached to this Intent
        //the -1 is a fallback default in case something went wrong and no id was passed
        recipeId = getIntent().getIntExtra("recipe_id", -1);

        EditText editIngredientName = findViewById(R.id.editIngredientName);
        EditText editQuantityRequired = findViewById(R.id.editQuantityRequired);
        EditText editUnit = findViewById(R.id.editUnit);
        Button buttonSaveAndAddAnother = findViewById(R.id.buttonSaveAndAddAnother);
        Button buttonDone = findViewById(R.id.buttonDone);

        buttonSaveAndAddAnother.setOnClickListener(view -> {
            String ingredientName = editIngredientName.getText().toString().trim();
            String quantityText = editQuantityRequired.getText().toString().trim();
            String unit = editUnit.getText().toString().trim();

            //makes sure nothing was left empty before saving
            if (ingredientName.isEmpty() || quantityText.isEmpty() || unit.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double quantityRequired;
            try {
                quantityRequired = Double.parseDouble(quantityText);
            } catch (NumberFormatException e) {
                //this runs if the user typed something that is not a valid number
                Toast.makeText(this, "Quantity must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            dataSource.open();
            dataSource.addRecipeIngredient(recipeId, ingredientName, quantityRequired, unit);
            dataSource.close();

            Toast.makeText(this, ingredientName + " added to recipe", Toast.LENGTH_SHORT).show();

            //clears the fields so the next ingredient can be typed without leftover text
            editIngredientName.setText("");
            editQuantityRequired.setText("");
            editUnit.setText("");
        });

        buttonDone.setOnClickListener(view -> {
            finish();
        });
    }
}