package com.example.mathieuhp.planeat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.RecipesListItemDecoration;
import com.example.mathieuhp.planeat.adapters.RecipesListItemAdapter;
import com.example.mathieuhp.planeat.models.FirebaseDataRetriever;
import com.example.mathieuhp.planeat.models.Ingredient;
import com.example.mathieuhp.planeat.models.Recipe;
import com.example.mathieuhp.planeat.models.User;

import java.util.ArrayList;

public class RecipesListFragment extends Fragment implements Updatable, FirebaseDataRetriever {

    // give an access for the models to call the update methode
    private User user;

    // graphic components
    private ProgressBar loadingBar;
    private RecyclerView recipesRecyclerView;
    private RecipesListItemAdapter recipeItemAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes_list, null);

        //setting searchbar
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbarSearch);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.search));

        //setting loadingbar
        loadingBar = (ProgressBar) view.findViewById(R.id.recipesLoadingBar);
        loadingBar.animate();


        //setting list of recipes
        recipesRecyclerView = (RecyclerView) view.findViewById(R.id.recipesRecyclerView);
        recipesRecyclerView.setHasFixedSize(true);

        //setting adapter for list items
        ArrayList<Recipe> recipes = user.getAllRecipes();

        if(user.getAllRecipes().size() > 0){
            loadingBar.setVisibility(View.GONE);
            recipesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            recipesRecyclerView.addItemDecoration(new RecipesListItemDecoration(30));
            recipeItemAdapter = new RecipesListItemAdapter(recipes, this.getContext());
            recipesRecyclerView.setAdapter(recipeItemAdapter);
        }
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // allow toolbar
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // get the user from the activity
            user = bundle.getParcelable("user");
        }
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

    @Override
    public void retrieveData() {
        //refreshing the layout of fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this);
        transaction.attach(this);
        transaction.commit();
    }

    @Override
    public void updateData() {

    }
}
