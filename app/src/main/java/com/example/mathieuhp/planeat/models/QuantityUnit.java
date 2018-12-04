package com.example.mathieuhp.planeat.models;

public enum QuantityUnit {

    /* ATTRIBUTES */

    unite("-"),
    gramme("g."),
    kilogramme("kg"),
    millilitres("ml"),
    centilitres("cl"),
    litres("L"),
    cupes("cup"),
    cuillere("cuillere");

    private String unity;


    /* CONSTRUCTOR */
    QuantityUnit(String unity){
        this.unity = unity;
    }

    /* METHOD */
    @Override
    public String toString(){
        return unity;
    }
}
