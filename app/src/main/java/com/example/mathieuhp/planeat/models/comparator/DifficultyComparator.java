package com.example.mathieuhp.planeat.models.comparator;

import com.example.mathieuhp.planeat.models.Recipe;

public class DifficultyComparator implements RecipeComparator {

    @Override
    public int compare(Recipe r1, Recipe r2) {
        int res = 0;
        float resDif = r1.getDifficulty() - r2.getDifficulty();
        if(resDif > 0)
            res = 1;
        else if(resDif < 0)
            res = -1;
            // if equal, compare with alphabetical order
        else {
            AlphabeticalComparator comp = new AlphabeticalComparator();
            res = comp.compare(r1, r2);
        }
        return res;
    }
}
