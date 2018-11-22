package com.example.mathieuhp.planeat.models;

import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mathieuhp.planeat.fragments.UserFragment;
import com.example.mathieuhp.planeat.models.comparator.RecipeComparator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class User implements Parcelable{

    private FirebaseDataRetriever firebaseDataRetriever; //usually the activity or the model that needs its info

    private String id;
    private String birthDate;
    private String email;
    private String firstName;
    private String lastName;
    private ArrayList<Recipe> personnalRecipes;
    private ArrayList<Recipe> followedRecipes;
    private RecipeCalendar recipeCalendar;
    private RecipeComparator comparator;

    private DatabaseReference firebaseReference;





    private Fridge fridge;
//    private ShoppingList shoppingList; //TODO

    public User() {
        // default constructor required for calls to DataSnapshot.getValue(xxx.class)
    }


    public User(String id, String email, FirebaseDataRetriever firebaseDataRetriever) {
        this.id = id;
        this.email = email;
        this.birthDate = "";
        this.firstName = "";
        this.lastName = "";

        this.personnalRecipes = new ArrayList<Recipe>();
        this.followedRecipes = new ArrayList<Recipe>();

        this.firebaseDataRetriever = firebaseDataRetriever;

        // get data if stored in firebase
        // if not, create a user, a link between data and the connection
        firebaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseReference.addListenerForSingleValueEvent(new ValueEventListenerUserConstruct(this, firebaseReference));

        //get data dealing with recipes
        RecipeCatalogValueListener recipeCatalogValueListener = new RecipeCatalogValueListener(this);
        firebaseReference.child("recipeCatalogs").child(this.id).addListenerForSingleValueEvent(recipeCatalogValueListener);
    }

    /* ---- GETTERS ----*/

    public FirebaseDataRetriever getFirebaseDataRetriever() {
        return firebaseDataRetriever;
    }

    public String getId() {
        return id;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public ArrayList<Recipe> getFollowedRecipes() {
        return followedRecipes;
    }

    public ArrayList<Recipe> getPersonnalRecipes() {
        return personnalRecipes;
    }

    public ArrayList<Recipe> getAllRecipes() {
        ArrayList<Recipe> allRecipes = new ArrayList<Recipe>();
        if(this.followedRecipes != null) {
            allRecipes.addAll(this.followedRecipes);
        }
        if(this.personnalRecipes != null) {
            allRecipes.addAll(this.personnalRecipes);
        }
        return allRecipes;
    }

    public RecipeCalendar getRecipeCalendar() {
        return recipeCalendar;
    }
    public Fridge getFridge() {
        return fridge;
    }





    /* ---- SETTERS ---- */


    public void setFirebaseDataRetriever(FirebaseDataRetriever firebaseDataRetriever) {
        this.firebaseDataRetriever = firebaseDataRetriever;
    }

    private void setId(String id) {
        this.id = id;
    }

    private void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPersonnalRecipes(ArrayList<Recipe> personnalRecipes){ this.personnalRecipes = personnalRecipes; }

    public void setFollowedRecipes(ArrayList<Recipe> followedRecipes){ this.followedRecipes = followedRecipes; }

    public void setFridge(Fridge fridge) {
        this.fridge = fridge;
    }



    /* ---- FUNCTIONS ---- */
    /**
     * sort the recipes list
     */
    public void sortList() {
        Collections.sort(this.getAllRecipes(), comparator);


//    /**
//     * sort the recipes list
//     */
//    public void sortList() {
//        Collections.sort(listPersonnalAndFollowedRecipe, comparator);
   }

    public void addPersonnalRecipe(Recipe recipe){
        this.personnalRecipes.add(recipe);
    }

    public void addFollowedRecipe(Recipe recipe){
        this.followedRecipes.add(recipe);
    }

    @Override
    public String toString() {
        return "User{\n" +
                " id='" + id + "\'\n" +
                " birthDate='" + birthDate + "\'\n" +
                " email='" + email + "\'\n" +
                " firstName='" + firstName + "\'\n" +
                " lastName='" + lastName + "\'\n" +
                '}';
    }



    /**
     * link the the current instance of the user of the connection with our user
     */
    private class ValueEventListenerUserConstruct implements ValueEventListener {

        private User u;
        private DatabaseReference firebaseRef;

        private ValueEventListenerUserConstruct(User u, DatabaseReference fr) {
            this.u = u;
            this.firebaseRef = fr;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            boolean isInDB = false;
            try {
                DataSnapshot ds = dataSnapshot.child("users");
                if(ds.child(u.getId()).exists()) {
                    u.setFirstName(ds.child(u.getId()).getValue(User.class).getFirstName());
                    u.setLastName(ds.child(u.getId()).getValue(User.class).getLastName());
                    u.setBirthDate(ds.child(u.getId()).getValue(User.class).getBirthDate());
                    isInDB = true;
                }

                // get the recipe list todo
                ds = dataSnapshot.child("recipes");
                for(DataSnapshot dataSnapshot1 : ds.getChildren()) {
                    if(u.getId().equals(dataSnapshot1.child("owner").getValue())) {
                        u.addPersonnalRecipe(new Recipe(dataSnapshot1.getKey()));
                    }
                }

                for(Recipe recipe : u.getAllRecipes())    // affD
                    Log.d("TABLEAU RECIPE : ", recipe.toString());   // affD


                // get the calendar
                recipeCalendar = new RecipeCalendar(this.u.id);


                // notify the observers
                if(UserFragment.getUserFragment() != null)
                    UserFragment.getUserFragment().updateView();

            }  catch(Exception e) {
                e.printStackTrace();
            }
            if(!isInDB) {
                // create the user in firebase
                try {
                    firebaseRef.child("users").child(u.getId()).child("email").setValue(u.getEmail());
                    firebaseRef.child("users").child(u.getId()).child("firstName").setValue(u.getFirstName());
                    firebaseRef.child("users").child(u.getId()).child("lastName").setValue(u.getLastName());
                    firebaseRef.child("users").child(u.getId()).child("birthDate").setValue(u.getBirthDate());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    public void updateData() {
        try {
           // this.firebaseReference.child("users").child(this.id).setValue(this);
            this.firebaseReference.child("users").child(this.id).child("email").setValue(this.email);
            this.firebaseReference.child("users").child(this.id).child("firstName").setValue(this.firstName);
            this.firebaseReference.child("users").child(this.id).child("lastName").setValue(this.lastName);
            this.firebaseReference.child("users").child(this.id).child("birthDate").setValue(this.birthDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteData() {
        try {
            this.firebaseReference.child("users").child(this.id).removeValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    public void updateRecipes(){
        this.firebaseReference.child("recipeCatalogs").child(this.id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child : children){

                    Recipe newRecipe = new Recipe();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    /* ------------- PARCELABLE ------------- */

    /**
     * Constructs a User from a Parcel
     * @param in
     */
    private User(Parcel in) {
        id = in.readString();
        birthDate = in.readString();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(birthDate);
        parcel.writeString(email);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
    }


    public class RecipeCatalogValueListener implements ValueEventListener, FirebaseDataRetriever{

        private User user;
        private int nbLoadedRecipes;
        private int nbTotalRecipes;

        public RecipeCatalogValueListener(User user){
            this.user = user;
            this.nbLoadedRecipes = 0;
            this.nbTotalRecipes = 0;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("Lets explore recipes! ", "Lets go!");
            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
            for(DataSnapshot recipesPerTypeSnapshot : children){
                String recipeType = recipesPerTypeSnapshot.getKey().toString();
                Log.d("recipe type", recipeType);
                Iterable<DataSnapshot> recipes = recipesPerTypeSnapshot.getChildren();
                this.nbTotalRecipes += recipesPerTypeSnapshot.getChildrenCount();
                for(DataSnapshot recipeSnapshot : recipes){
                    String recipeId = recipeSnapshot.getKey();
                    Recipe newRecipe = new Recipe(this, recipeId);
                    if(recipeType.equals("followed")){
                        this.user.addFollowedRecipe(newRecipe);
                        Log.d("recipe added to ", recipeType);
                        Log.d("followed recipes contains:", this.user.getFollowedRecipes().get(0).toString());
                    }
                    else if(recipeType.equals("personnal")){
                        this.user.addPersonnalRecipe(newRecipe);
                        Log.d("recipe added to ", recipeType);
                        Log.d("personnal recipes contains:", this.user.getPersonnalRecipes().get(0).toString());
                    }
                    Log.d("recipes loaded: ", this.nbLoadedRecipes + "/" + this.nbTotalRecipes);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            System.out.println("FAILED");
        }


        @Override
        public void retrieveData() {
            this.nbLoadedRecipes += 1;
            //notify observer when recipes are all loaded
            if(this.nbLoadedRecipes == nbTotalRecipes){
                this.user.getFirebaseDataRetriever().retrieveData();
            }
        }

        @Override
        public void updateData() {

        }

        public User getUser() {
            return user;
        }
    }
}
