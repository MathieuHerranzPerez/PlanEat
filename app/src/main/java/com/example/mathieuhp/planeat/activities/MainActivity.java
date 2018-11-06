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
import com.example.mathieuhp.planeat.fragments.PlanningFragment;
import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.fragments.RecipesListFragment;
import com.example.mathieuhp.planeat.fragments.ShoppingFragment;
import com.example.mathieuhp.planeat.fragments.UserFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private TextView mTitleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitleToolbar = (TextView) findViewById(R.id.toolbar_title);
        mTitleToolbar.setText(R.string.recipes);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // we are on the first fragment when run
        loadFragment(new RecipesListFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Fragment fragment = null;
        switch(menuItem.getItemId()) {
            case R.id.navigation_recipes:
                fragment = new RecipesListFragment();
                mTitleToolbar.setText(R.string.recipes);
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
}