package com.example.mathieuhp.planeat.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.mathieuhp.planeat.models.comparator.RecipeComparator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class User implements Parcelable {

    private String id;
    private String birthDate;
    private String email;
    private String firstName;
    private String lastName;
    private ArrayList<Recipe> listPersonnalRecipe;
    private ArrayList<Recipe> listFollowedRecipe;
    private ArrayList<Recipe> listPersonnalAndFollowedRecipe;
    private RecipeComparator comparator;

    // TODO
//    private RecipeCalendar calendar;
//    private Fridge fridge;
//    private ShoppingList shoppingList;

    public User() {
        // default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String id, String email) {
        this.id = id;
        this.email = email;
        this.birthDate = "";
        this.firstName = "";
        this.lastName = "";

        // get data if stored in firebase
        // if not, create a user, a link between data and the connection
        DatabaseReference firebaseTable = FirebaseDatabase.getInstance().getReference();
        firebaseTable.addListenerForSingleValueEvent(new MyValueEventListener(this, firebaseTable));
    }
    /* ---- GETTERS ----*/
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

    /* ---- SETTERS ---- */
    private void setId(String id) {
        this.id = id;
    }

    private void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * sort the recipes list
     */
    public void sortList() {
        Collections.sort(listPersonnalAndFollowedRecipe, comparator);
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
     * we need the the current instance of user
     */
    private class MyValueEventListener implements ValueEventListener {

        private User u;
        private DatabaseReference firebaseRef;

        private MyValueEventListener(User u, DatabaseReference fr) {
            this.u = u;
            this.firebaseRef = fr;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            boolean isInDB = false;

            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                if(ds.child(u.getId()).exists()) {
                    u.setFirstName(ds.child(u.getId()).getValue(User.class).getFirstName());
                    u.setLastName(ds.child(u.getId()).getValue(User.class).getLastName());
                    u.setBirthDate(ds.child(u.getId()).getValue(User.class).getBirthDate());
                    isInDB = true;
                }
            }
            if(!isInDB) {
                // create the user in firebase
                firebaseRef.child("users").child(u.getId()).setValue(u);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    /* ------------- PARCELABLE ------------- */

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
}
