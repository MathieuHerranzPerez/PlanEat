package com.example.mathieuhp.planeat.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.mathieuhp.planeat.fragments.FridgeFragment;
import com.example.mathieuhp.planeat.fragments.NewRecipeFragment;
import com.example.mathieuhp.planeat.fragments.PlanningFragment;
import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.fragments.RecipesListFragment;
import com.example.mathieuhp.planeat.fragments.ShoppingFragment;
import com.example.mathieuhp.planeat.fragments.UserFragment;
import com.example.mathieuhp.planeat.models.FirebaseDataRetriever;
import com.example.mathieuhp.planeat.models.Ingredient;
import com.example.mathieuhp.planeat.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTitleToolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Bundle bundleUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup the toolbar
        mTitleToolbar = (TextView) findViewById(R.id.toolbar_title);
        mTitleToolbar.setText(R.string.recipes);

        // setup the bottom navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        //create the fragment
        RecipesListFragment recipesListFragment = new RecipesListFragment();

        // get the user connection
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        // create the user
        User user = new User(firebaseUser.getUid(), firebaseUser.getEmail(), recipesListFragment);
        // and the bundle to pass it to the fragment
        bundleUser = new Bundle();
        bundleUser.putParcelable("user", user);

        loadIngredientJsonFromAsset();

        // launch the main fragment
        recipesListFragment.setArguments(bundleUser);
        loadFragment(recipesListFragment);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        switch(menuItem.getItemId()) {
            case R.id.navigation_recipes:
                fragment = new RecipesListFragment();
                fragment.setArguments(bundleUser);
                mTitleToolbar.setText(R.string.recipes);
                break;

            case R.id.navigation_fridge:
                fragment = new FridgeFragment();
                fragment.setArguments(bundleUser);
                mTitleToolbar.setText(R.string.fridge);
                break;

            case R.id.navigation_planning:
                fragment = new PlanningFragment();
                fragment.setArguments(bundleUser);
                mTitleToolbar.setText(R.string.planning);
                break;

            case R.id.navigation_shopping:
                fragment = new ShoppingFragment();
                fragment.setArguments(bundleUser);
                mTitleToolbar.setText(R.string.shopping);
                break;

            case R.id.navigation_user:
                fragment = new UserFragment();
                fragment.setArguments(bundleUser);
                mTitleToolbar.setText(R.string.user);
                break;
        }
        return loadFragment(fragment);
    }


    /**
     * Instantiate the ingredients by checking the JSON file
     */
    public void loadIngredientJsonFromAsset() {

        if(!(Ingredient.ingredientList.size() > 0)) {
            try {
                InputStream is = getAssets().open("ingredientsCalories.json");

                int size = is.available();
                byte[] buffer = new byte[size];

                is.read(buffer);
                is.close();

                String json = new String(buffer, "UTF-8");
                JSONArray jsonArray = new JSONArray(json);

                for (int i = 0; i < jsonArray.length(); ++i) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    // fill the ingredient static list by calling the constructor ...
                    Ingredient ingr = new Ingredient(obj.getString("id"), obj.getString("name"),
                            obj.getString("kiloCaloriesPerHundredGrams"), obj.getString("type"));
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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

}