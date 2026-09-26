package com.lwazi.smartpantrymanager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog; // lets us show a Yes/No confirmation popup
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import android.content.Intent;


public class MainActivity extends AppCompatActivity implements IngredientAdapter.OnIngredientLongClickListener {

    private PantryDataSource dataSource;
    private IngredientAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new PantryDataSource(this);

        recyclerView = findViewById(R.id.recyclerViewIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabAddIngredient = findViewById(R.id.fabAddIngredient);
        fabAddIngredient.setOnClickListener(view -> {
            //navigates from this screen to AddIngredientActivity
            Intent intent = new Intent(MainActivity.this, AddIngredientActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open(); //opens the database connection
        loadIngredients(); //reloads fresh data
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close(); //closes it again once this screen is no longer visible
    }

    //this reads all ingredients from the database and displays them
    private void loadIngredients() {
        List<Ingredient> ingredients = dataSource.getAllIngredients();
        if (adapter == null) {
            //creates the adapter and attach it to the RecyclerView
            //"this" is passed in as the long-click listener, since MainActivity now implements it
            adapter = new IngredientAdapter(ingredients, this);
            recyclerView.setAdapter(adapter);
        } else {
            //adapter already exists, refresh data
            adapter.updateIngredients(ingredients);
        }
    }

    //this is called automatically by IngredientAdapter whenever a row is long-pressed
    @Override
    public void onIngredientLongClick(Ingredient ingredient) {
        //builds a small popup asking the user to confirm before deleting
        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage("Are you sure you want to delete " + ingredient.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    //user confirmed and performs the delete
                    dataSource.deleteIngredient(ingredient);
                    loadIngredients(); //refreshes the list so the deleted row disappears
                })
                .setNegativeButton("Cancel", null) //this does nothing it just closes the popup
                .show();
    }
}