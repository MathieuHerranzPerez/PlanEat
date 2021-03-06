package com.example.mathieuhp.planeat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.fragments.FridgeFragment;
import com.example.mathieuhp.planeat.fragments.PlanningFragment;
import com.example.mathieuhp.planeat.fragments.RecipeFragment;
import com.example.mathieuhp.planeat.fragments.RecipesListFragment;
import com.example.mathieuhp.planeat.fragments.ShoppingFragment;
import com.example.mathieuhp.planeat.fragments.UserFragment;
import com.example.mathieuhp.planeat.models.Recipe;
import com.example.mathieuhp.planeat.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Set;

public class RecipeDisplayActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTitleToolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Recipe recipe;
    private RecipeFragment recipeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        recipe = null;
        //get the recipe to display
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if(extras != null) {
                // get the recipe from the previous activity
                recipe = (Recipe) extras.getParcelable("recipe");
            }
        }

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_display);

        // setup the toolbar
        mTitleToolbar = (TextView) findViewById(R.id.toolbar_title);
        mTitleToolbar.setText(recipe.getName());

        // setup the bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigationForRecipe);
        navigation.setOnNavigationItemSelectedListener(this);

        //create the fragment
        Bundle bundle = new Bundle();
        this.recipeFragment = new RecipeFragment();

        // and the bundle to pass it to the fragment
        bundle.putParcelable("recipe", this.recipe);

        // launch the main fragment
        recipeFragment.setArguments(bundle);
        loadFragment(recipeFragment);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        switch(menuItem.getItemId()) {
            case R.id.navigation_recipes:
                fragment = this.recipeFragment;
                break;

            case R.id.navigation_fridge:
                fragment = new FridgeFragment();
                mTitleToolbar.setText(R.string.fridge);
                break;

            case R.id.navigation_planning:
                fragment = new PlanningFragment();
                mTitleToolbar.setText(R.string.planning);
                break;

            case R.id.navigation_shopping:
                fragment = new ShoppingFragment();
                mTitleToolbar.setText(R.string.shopping);
                break;

            case R.id.navigation_user:
                fragment = new UserFragment();
                //fragment.setArguments(bundle);
                mTitleToolbar.setText(R.string.user);
                break;
        }
        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {

        if(fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    /*public void getIncomingIntent() {
        Intent intent;
        if((intent = getIntent()) != null) {
            if (intent.hasExtra("recipe")) {
                this.recipe = getIntent().getParcelableExtra("recipe");
            }
        }
    }*/
}
