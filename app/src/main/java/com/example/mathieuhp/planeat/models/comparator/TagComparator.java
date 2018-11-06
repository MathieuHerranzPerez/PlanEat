package com.example.mathieuhp.planeat.models.comparator;

import com.example.mathieuhp.planeat.models.Recipe;

public class TagComparator implements RecipeComparator {

    @Override
    public int compare(Recipe r1, Recipe r2) {
        return r1.getTag().compareTo(r2.getTag());
    }
}