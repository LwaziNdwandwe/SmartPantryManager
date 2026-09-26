package com.lwazi.smartpantrymanager;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class AddRecipeActivity extends AppCompatActivity {

    private RecipeDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        dataSource = new RecipeDataSource(this);

        EditText editRecipeName = findViewById(R.id.editRecipeName);
        EditText editRecipeInstructions = findViewById(R.id.editRecipeInstructions);
        Button buttonSaveRecipe = findViewById(R.id.buttonSaveRecipe);

        buttonSaveRecipe.setOnClickListener(view -> {
            String name = editRecipeName.getText().toString().trim();
            String instructions = editRecipeInstructions.getText().toString().trim();

            //makes sure the recipe at least has a name before saving
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a recipe name", Toast.LENGTH_SHORT).show();
                return;
            }

            dataSource.open();
            //addRecipe returns the new Recipe including its generated id
            Recipe newRecipe = dataSource.addRecipe(name, instructions);
            dataSource.close();

            Toast.makeText(this, "Recipe saved. Now add its ingredients.", Toast.LENGTH_SHORT).show();

            //moves on to the next screen
            Intent intent = new Intent(AddRecipeActivity.this, AddRecipeIngredientActivity.class);
            intent.putExtra("recipe_id", newRecipe.getId()); //attaches the id as extra data on the Intent
            startActivity(intent);
            finish(); //closes this screen
        });
    }
}