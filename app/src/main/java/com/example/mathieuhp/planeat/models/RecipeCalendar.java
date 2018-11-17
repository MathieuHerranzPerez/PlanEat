package com.example.mathieuhp.planeat.models;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.example.mathieuhp.planeat.fragments.PlanningFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.TreeMap;

public class RecipeCalendar {

    //              yyyywwd      Recipe   Recipe
    private TreeMap<Integer, Pair<Recipe, Recipe> > recipesListPerDay;

    private DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference().child("recipeSchedule");


    public RecipeCalendar() {
        User user = User.getUserInstance();

        firebaseReference.addValueEventListener(new ValueEventListenerRecipeCalendarConstruct(this, user.getId(), firebaseReference));
    }


    /* ---- GETTERS ----*/
    public TreeMap<Integer, Pair<Recipe, Recipe> > getRecipesListPerDay() {
        return recipesListPerDay;
    }


    /* ---- SETTERS ----*/
    private void setRecipesListPerDay(TreeMap<Integer, Pair<Recipe, Recipe>> recipesListPerDay) {
        this.recipesListPerDay = recipesListPerDay;
    }


    public void saveRecipeForDate(Recipe recipe, float date) {

        int year = (int) (date / 1000f);
        int week = ((int) (date / 10f)) % 100;
        int dayOfWeek = (int) date % 10;
        int dayPart = (int) ((date - ((int) date)) * 10);

        // update the TreeMap
        int key = (int) date;
        if(dayPart == 1) {
            Recipe recipePart2 = recipesListPerDay.get(key).second;
            // update the TreeMap
            recipesListPerDay.put(key, new Pair<>(recipe, recipePart2));

            // update the DB
            firebaseReference.child(User.getUserInstance().getId()).child(Integer.toString(year)).child(Integer.toString(week))
                    .child(Integer.toString(dayOfWeek)).child("dinner").setValue(recipe.getId());
        }
        else {
            Recipe recipePart1 = recipesListPerDay.get(key).first;
            // update the TreeMap
            recipesListPerDay.put(key, new Pair<>(recipePart1, recipe));

            // update the DB
            firebaseReference.child(User.getUserInstance().getId()).child(Integer.toString(year)).child(Integer.toString(week))
                    .child(Integer.toString(dayOfWeek)).child("lunch").setValue(recipe.getId());
        }

        // notify the observers
        if(PlanningFragment.getPlanningFragment() != null)
            PlanningFragment.getPlanningFragment().updateView();
    }

    private class ValueEventListenerRecipeCalendarConstruct implements ValueEventListener {

        private String userId;
        private RecipeCalendar recipeCal;
        private DatabaseReference firebase;

        private ValueEventListenerRecipeCalendarConstruct(RecipeCalendar recipeCal, String userId, DatabaseReference fr) {
            this.userId = userId;
            this.recipeCal = recipeCal;
            this.firebase = fr;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            try {
                Calendar calendar = Calendar.getInstance();
                // the current year
                int year = calendar.get(Calendar.YEAR);
                // the current week
                int week = calendar.get(Calendar.WEEK_OF_YEAR);
                // the current day (indexed starting at 1)
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                // insert the calendar

                // if the user doesn't have any calendar with the current week, create it
                if(!dataSnapshot.child(userId).child(Integer.toString(year)).child(Integer.toString(week)).exists()) {
                    for(int i = 1; i < 8; ++i) {
                        firebase.child(userId).child(Integer.toString(year)).child(Integer.toString(week)).child(Integer.toString(i)).child("dinner").setValue(-1);
                        firebase.child(userId).child(Integer.toString(year)).child(Integer.toString(week)).child(Integer.toString(i)).child("lunch").setValue(-1);
                    }
                }

                // load the user calendar in the TreeMap
                TreeMap<Integer, Pair<Recipe, Recipe> >recipesList = new TreeMap<>();
                int treeMapKey = year * 1000 + week * 10;    // yyyy ww d
                for(int i = 1; i < 8; ++i) {
                    String idRecipe1 = (String) dataSnapshot.child(userId).child(Integer.toString(year)).child(Integer.toString(week)).child(Integer.toString(i)).child("dinner").getValue();
                    Recipe r1;
                    if(!("-1").equals(idRecipe1))
                        r1 = new Recipe((String) dataSnapshot.child(userId).child(Integer.toString(year)).child(Integer.toString(week)).child(Integer.toString(i)).child("dinner").getValue());
                    else
                        r1 = null;
                    String idRecipe2 = (String) dataSnapshot.child(userId).child(Integer.toString(year)).child(Integer.toString(week)).child(Integer.toString(i)).child("lunch").getValue();
                    Recipe r2;
                    if(!("-1").equals(idRecipe2))
                        r2 = new Recipe((String) dataSnapshot.child(userId).child(Integer.toString(year)).child(Integer.toString(week)).child(Integer.toString(i)).child("lunch").getValue());
                    else
                        r2 = null;

                    recipesList.put(treeMapKey + i, new Pair<>(r1, r2));
                }

                recipeCal.setRecipesListPerDay(recipesList);

                // notify the observers
                if(PlanningFragment.getPlanningFragment() != null)
                    PlanningFragment.getPlanningFragment().updateView();

            }  catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

    public void updateData() {

    }
}
