package com.example.mathieuhp.planeat.models;

import com.example.mathieuhp.planeat.models.comparator.RecipeComparator;

import java.util.ArrayList;
import java.util.Collections;

public class User {

    private int id;
    private String name;
    private ArrayList<Recipe> listPersonnalRecipe;
    private ArrayList<Recipe> listFollowedRecipe;
    private ArrayList<Recipe> listPersonnalAndFollowedRecipe;
    private RecipeComparator comparator;

    // TODO
//    private RecipeCalendar calendar;
//    private Fridge fridge;
//    private ShoppingList shoppingList;


    /**
     * sort the recipes list
     */
    public void sortList() {
        Collections.sort(listPersonnalAndFollowedRecipe, comparator);
    }
}
