package com.example.mathieuhp.planeat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.activities.MainActivity;
import com.example.mathieuhp.planeat.activities.RecipeDisplayActivity;
import com.example.mathieuhp.planeat.models.Recipe;

import java.util.List;

public class RecipesListItemAdapter extends RecyclerView.Adapter{

    private List<Recipe> recipes;
    private Context context;

    //data related methods
    public List<Recipe> getRecipes(){
        return this.recipes;
    }

    public void setRecipes(List<Recipe> recipes){
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    //constructor
    public RecipesListItemAdapter(List<Recipe> recipes, Context context){
        this.recipes = recipes;
        this.context = context;
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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof RecipeHolder){
            final Recipe currentRecipe = recipes.get(position);
            ((RecipeHolder) viewHolder).bind(currentRecipe.getImage(), currentRecipe.getName(), currentRecipe.getTags());
            ((RecipeHolder) viewHolder).itemLayout.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Log.d("clicked recipe",  currentRecipe.getName());
                    Intent intent = new Intent(context, RecipeDisplayActivity.class);
                    intent.putExtra("recipe", currentRecipe);
                    context.startActivity(intent);
                }
            });
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

        private LinearLayout itemLayout;
        private ImageView recipeImage;
        private TextView recipeTitle;
        private TextView recipeTags;

        public RecipeHolder(@NonNull View itemView) {
            super(itemView);

            itemLayout = itemView.findViewById(R.id.itemLayout);
            recipeImage = itemView.findViewById(R.id.recipeImage);
            recipeTitle = itemView.findViewById(R.id.recipeTitle);
            recipeTags = itemView.findViewById(R.id.recipeTags);

        }

        public static RecipeHolder inflate(ViewGroup parent){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipes_list_item, parent, false);
            return new RecipeHolder(view);
        }

        public void bind(Bitmap image, String title, List<String> tagsList){
            if(image != null) {
                recipeImage.setImageBitmap(image);
            }

            if(title != null) {
                recipeTitle.setText(title);
            }

            if(tagsList != null) {
                String tags = "";
                for (int i = 0; i < tagsList.size(); i++) {
                    tags += tagsList.get(i);
                    if (i != tagsList.size() - 1) {
                        tags += ", ";
                    }
                }
                recipeTags.setText(tags);
            }
        }
    }
}
