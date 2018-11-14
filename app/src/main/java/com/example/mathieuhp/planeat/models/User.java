package com.example.mathieuhp.planeat.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

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

    private DatabaseReference firebaseReference;

    // TODO
//    private RecipeCalendar calendar;
//    private Fridge fridge;
//    private ShoppingList shoppingList;

    public User() {
        // default constructor required for calls to DataSnapshot.getValue(xxx.class)
    }

    public User(String id, String email) {
        this.id = id;
        this.email = email;
        this.birthDate = "";
        this.firstName = "";
        this.lastName = "";

        // get data if stored in firebase
        // if not, create a user, a link between data and the connection
        firebaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseReference.addListenerForSingleValueEvent(new ValueEventListenerUserConstruct(this, firebaseReference));
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
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
                if (ds.child(u.getId()).exists()) {
                    u.setFirstName(ds.child(u.getId()).getValue(User.class).getFirstName());
                    u.setLastName(ds.child(u.getId()).getValue(User.class).getLastName());
                    u.setBirthDate(ds.child(u.getId()).getValue(User.class).getBirthDate());
                    isInDB = true;
                }

                Log.d("USER : ", u.toString());
                // get the recipe list


            }  catch (Exception e) {
                e.printStackTrace();
            }
            if(!isInDB) {
                // create the user in firebase
                try {
                    //firebaseRef.child("users").child(u.getId()).setValue(u);
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
        listPersonnalRecipe = new ArrayList<>();
        in.readList(listPersonnalRecipe, null);
        listFollowedRecipe = new ArrayList<>();
        in.readList(listFollowedRecipe, null);
        listPersonnalAndFollowedRecipe = new ArrayList<>();
        in.readList(listPersonnalAndFollowedRecipe, null);
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
        parcel.writeList(listPersonnalRecipe);
        parcel.writeList(listFollowedRecipe);
        parcel.writeList(listPersonnalAndFollowedRecipe);
    }
}
