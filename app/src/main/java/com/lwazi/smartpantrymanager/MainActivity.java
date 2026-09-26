package com.lwazi.smartpantrymanager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

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
            //navigation from this screen to AddIngredientActivity
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
            adapter = new IngredientAdapter(ingredients);
            recyclerView.setAdapter(adapter);
        } else {
            //adapter already exists, refresh its data
            adapter.updateIngredients(ingredients);
        }
    }
}