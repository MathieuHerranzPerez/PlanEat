package com.example.mathieuhp.planeat.models;

public enum QuantityUnit {

    /* ATTRIBUTES */

    unite("unite"),
    g("g"),
    kg("kg"),
    ml("ml"),
    cl("cl"),
    L("L"),
    cup("cup"),
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
