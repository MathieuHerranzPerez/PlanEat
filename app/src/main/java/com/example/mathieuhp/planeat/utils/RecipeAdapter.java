package com.example.mathieuhp.planeat.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mathieuhp.planeat.models.Recipe;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {

    private Context context;

    private List<Recipe> recipeList;

    public RecipeAdapter(Context context, int textViewResourceId, List<Recipe> recipeList) {
        super(context, textViewResourceId, recipeList);

        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public int getCount() {
        return recipeList.size();
    }

    @Nullable
    @Override
    public Recipe getItem(int position) {
        return recipeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);

        label.setText(recipeList.get(position).getName());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(recipeList.get(position).getName());

        return label;
    }
}
