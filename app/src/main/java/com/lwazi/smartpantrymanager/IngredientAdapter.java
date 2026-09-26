package com.lwazi.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;
    private OnIngredientLongClickListener longClickListener;

    public IngredientAdapter(List<Ingredient> ingredientList, OnIngredientLongClickListener longClickListener) {
        this.ingredientList = ingredientList;
        this.longClickListener = longClickListener;
    }

    //this is called when the RecyclerView needs a brand new empty row
    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false); //turns item_ingredient.xml into a View
        return new IngredientViewHolder(view);
    }

    //this is called to fill a row with a specific ingredient's data
    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.textName.setText(ingredient.getName());
        //combines quantity and unit into one readable string
        holder.textQuantity.setText(ingredient.getQuantity() + " " + ingredient.getUnit());

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onIngredientLongClick(ingredient);
            return true; // tells Android "handled" so it doesn't also register as a normal click
        });
    }

    public interface OnIngredientLongClickListener{
        void onIngredientLongClick(Ingredient ingredient);
    }



    //this tells Android how many rows exist in total
    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    //calls this when the data changes to refresh what's on screen
    public void updateIngredients(List<Ingredient> newIngredients) {
        this.ingredientList = newIngredients;
        notifyDataSetChanged(); //this tells the RecyclerView to redraw itself with the new data
    }

    //holds references to the two TextViews inside one row of item_ingredient.xml
    static class IngredientViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textQuantity;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textIngredientName);
            textQuantity = itemView.findViewById(R.id.textIngredientQuantity);
        }
    }
}