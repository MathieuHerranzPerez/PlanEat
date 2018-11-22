package com.example.mathieuhp.planeat.utils;

public class Utils {

    public static boolean isInteger(String str) {
        if(str == null || str.trim().isEmpty()) {
            return false;
        }
        Boolean res = true;
        for (int i = 0; i < str.length(); ++i) {
            if(!Character.isDigit(str.charAt(i))) {
                res = false;
            }
        }
        return res;
    }
}
