package com.lwazi.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddIngredientActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editQuantity;
    private EditText editUnit;
    private PantryDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);

        editName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editIngredientQuantity);
        editUnit = findViewById(R.id.editIngredientUnit);

        dataSource = new PantryDataSource(this);

        Button buttonSave = findViewById(R.id.buttonSaveIngredient);
        buttonSave.setOnClickListener(view -> saveIngredient());
    }

    //This reads the form fields, validates them and saves a new Ingredient if valid
    private void saveIngredient() {
        String name = editName.getText().toString().trim();
        String quantityText = editQuantity.getText().toString().trim();
        String unit = editUnit.getText().toString().trim();

        //validation
        if (name.isEmpty() || quantityText.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return; // stop here, don't save anything
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            //this makes sure letters are not inserted in the quantity field
            Toast.makeText(this, "Quantity must be a valid number", Toast.LENGTH_SHORT).show();
            return;
        }

        dataSource.open();
        dataSource.addIngredient(name, quantity, unit);
        dataSource.close();

        Toast.makeText(this, name + " added to pantry", Toast.LENGTH_SHORT).show();
        finish(); //closes this screen and returns to MainActivity
    }
}
