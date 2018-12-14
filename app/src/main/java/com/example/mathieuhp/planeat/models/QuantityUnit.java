package com.example.mathieuhp.planeat.models;

public enum QuantityUnit {
    g, cups, ml, unit;

    public static QuantityUnit getUnit(String unit) {
        switch(unit){
            case "grammes":
            case "g":
            case "g.":
            case "grams" :
                return QuantityUnit.g;
            case "cups":
            case "cps":
                return QuantityUnit.cups;
            case "ml":
            case "millilitres":
                return QuantityUnit.ml;
            case "unit":
            case "":
            case "none":
                return QuantityUnit.unit;
        }
        return null;
    }
}
