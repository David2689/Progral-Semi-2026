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

    public static class RecipeItem {
        public String title;
        public String ingredients;
        public String instructions;
        public boolean isOnline;
        public String mealId;

        // Constructor para recetas online
        public RecipeItem(String title, String mealId) {
            this.title = title;
            this.mealId = mealId;
            this.ingredients = "";
            this.instructions = "Toca para ver la receta completa 👆";
            this.isOnline = true;
        }

        // Constructor para recetas locales
        public RecipeItem(RecipeManager.Recipe recipe) {
            this.title = recipe.title;
            this.ingredients = recipe.ingredients.replace(",", ", ");
            this.instructions = recipe.instructions;
            this.isOnline = false;
            this.mealId = null;
        }
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(RecipeItem recipe);
    }

    private List<RecipeItem> recipes = new ArrayList<>();
    private OnRecipeClickListener clickListener;

    public RecipeAdapter(OnRecipeClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setRecipes(List<RecipeItem> recipes) {
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
        RecipeItem r = recipes.get(position);
        holder.tvTitle.setText("🍽️ " + r.title);

        if (r.isOnline) {
            holder.tvIngredients.setText("🌐 Receta en línea - TheMealDB");
            holder.tvInstructions.setText("📋 " + r.instructions);
            // Hacer la card clickeable
            holder.itemView.setOnClickListener(v -> clickListener.onRecipeClick(r));
        } else {
            holder.tvIngredients.setText("🥗 Ingredientes: " + r.ingredients);
            holder.tvInstructions.setText("📋 " + r.instructions);
            holder.itemView.setOnClickListener(null);
        }
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