package com.example.smartfridgelite;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<RecipeManager.Recipe> recipes = new ArrayList<>();

    public void setRecipes(List<RecipeManager.Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeManager.Recipe r = recipes.get(position);
        holder.tvTitle.setText("🍽️ " + r.title);
        holder.tvIngredients.setText("🥗 Ingredientes: " + r.ingredients.replace(",", ", "));
        holder.tvInstructions.setText("📋 " + r.instructions);
    }

    @Override
    public int getItemCount() { return recipes.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvIngredients, tvInstructions;

        ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvRecipeTitle);
            tvIngredients = v.findViewById(R.id.tvUsedIngredients);
            tvInstructions = v.findViewById(R.id.tvMissingIngredients);
        }
    }
}