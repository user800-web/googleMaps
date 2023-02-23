package com.example.googlemaps;

public class Opening_hours {
    public boolean opening_hours;
    public CharSequence getOpening_hours(){
        String str = Boolean.toString(opening_hours);
        return str;
    }
}
