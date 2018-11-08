package com.example.mathieuhp.planeat.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.mathieuhp.planeat.models.comparator.RecipeComparator;

import java.util.ArrayList;
import java.util.Collections;

public class User implements Parcelable {

    private int id;
    private String name;
    private String email;
    private ArrayList<Recipe> listPersonnalRecipe;
    private ArrayList<Recipe> listFollowedRecipe;
    private ArrayList<Recipe> listPersonnalAndFollowedRecipe;
    private RecipeComparator comparator;

    // TODO
//    private RecipeCalendar calendar;
//    private Fridge fridge;
//    private ShoppingList shoppingList;

    public User(String email) {
        this.email = email;

        // TODO recuperer données dans BD
    }

    /**
     * sort the recipes list
     */
    public void sortList() {
        Collections.sort(listPersonnalAndFollowedRecipe, comparator);
    }

    @Override
    public String toString() {
        return "USER : [ " + "email : " + email + " ]";
    }

    private User(Parcel in) {
        id = in.readInt();
        name = in.readString();
        email = in.readString();
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
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(email);
    }
}
