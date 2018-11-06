package com.example.mathieuhp.planeat.models.comparator;

import com.example.mathieuhp.planeat.models.Recipe;

import java.util.Comparator;

public interface RecipeComparator extends Comparator<Recipe> {

    @Override
    int compare(Recipe r1, Recipe r2);
}
