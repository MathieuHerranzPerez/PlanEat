package com.example.mathieuhp.planeat.models.comparator;

import com.example.mathieuhp.planeat.models.Recipe;

public class PreparationTimeComparator implements RecipeComparator {

    @Override
    public int compare(Recipe r1, Recipe r2) {
        return r2.getPreparationTime() - r1.getPreparationTime();
    }
}
