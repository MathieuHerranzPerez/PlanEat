package com.example.mathieuhp.planeat.adapters;

import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.models.Recipe;

import java.util.List;

public class RecipesListItemAdapter extends RecyclerView.Adapter{

    private List<Recipe> recipes;

    //data related methods
    public void setRecipes(List<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    // adapters methods
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0){
            return RecipeHolder.inflate(parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof RecipeHolder){
            Recipe currentRecipe = recipes.get(position);
            ((RecipeHolder) viewHolder).bind(currentRecipe.getImage(), currentRecipe.getName(), currentRecipe.getTags());
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    static class RecipeHolder extends RecyclerView.ViewHolder{

        private ImageView recipeImage;
        private TextView recipeTitle;
        private TextView recipeTags;

        public RecipeHolder(@NonNull View itemView) {
            super(itemView);

            recipeImage = itemView.findViewById(R.id.recipeImage);

        }

        public static RecipeHolder inflate(ViewGroup parent){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_list_item, parent, false);
            return new RecipeHolder(view);
        }

        public void bind(Bitmap image, String title, List<String> tagsList){
            recipeImage.setImageBitmap(image);

            recipeTitle.setText(title);

            String tags = "";
            for (int i = 0; i < tagsList.size(); i++) {
                tags += tagsList.get(i);
                if(i != tagsList.size() - 1){
                    tags += ", ";
                }
            }
            recipeTags.setText(tags);
        }
    }
}
