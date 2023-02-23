package com.example.googlemaps;

import com.google.gson.Gson;

import java.util.ArrayList;

/*Nombre del Lugar
 Ubicación, Logo y Horarios
 */
public class Lugar {
    public transient String business_status;
    public Geometry geometry;
    public String icon;
    public transient String icon_background_color;
    public transient String icon_mask_base_uri;
    public String name;
    public Opening_hours opening_hours;
    public String place_id;
    public transient String reference;
    public transient String scope;
    public transient ArrayList<String> types;
    public String vicinity;
}
