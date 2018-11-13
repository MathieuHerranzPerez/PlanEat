package com.example.mathieuhp.planeat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.models.Ingredient;

public class RecipesListFragment extends Fragment implements Updatable {

    // give an access for the models to call the update methode
    private static RecipesListFragment recipesListFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipes_list, null);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarSearch);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.search));

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // allow toolbar
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(
            new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    // Perform the final search
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    // Text has changed, apply filtering
                    return false;
                }
            }
        );
    }

    @Override
    public void updateView() {
        // TODO
    }

    // give an access for the models to call the update methode
    public static RecipesListFragment getRecipesListFragment() {
        return recipesListFragment;
    }
}
