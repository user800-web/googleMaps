package com.example.googlemaps;

import com.google.gson.annotations.SerializedName;

public class Opening_hours {
    @SerializedName("open_now")
    public boolean open_now = false;

    public Opening_hours() {
    }
}
