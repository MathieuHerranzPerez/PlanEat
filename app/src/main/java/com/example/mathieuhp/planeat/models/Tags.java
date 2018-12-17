package com.example.mathieuhp.planeat.models;

public enum Tags {

    /* ATTRIBUTES */

    tag1("Amuse-gueule"),
    tag2("Entrée"),
    tag3("Dessert"),
    tag4("Sauces"),
    tag5("Boisson"),
    tag6("Facile à préparer"),
    tag7("Végétarien");

    private String tag;


    /* CONSTRUCTOR */
    Tags(String tag){
        this.tag = tag;
    }

    /* METHOD */
    @Override
    public String toString(){
        return tag;
    }

}
