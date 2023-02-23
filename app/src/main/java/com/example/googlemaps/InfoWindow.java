package com.example.googlemaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindow implements GoogleMap.InfoWindowAdapter {
    Context context;
    LayoutInflater inflater;    
    String titulo;
    View view;
    public InfoWindow(Context context, String titulo) {
        this.context = context;
        this.titulo = titulo;

        view = LayoutInflater.from(context).inflate(R.layout.ly_windowinfo, null);
    }
    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        Lugar lugar;
        try {
            lugar = (Lugar) marker.getTag();
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return this.view;
        }
        ImageView image = (ImageView)view.findViewById(R.id.image);
        ((TextView) view.findViewById(R.id.lblnombre)).setText(lugar.name);
        ((TextView) view.findViewById(R.id.lblubicación)).setText(lugar.vicinity);
        ((TextView) view.findViewById(R.id.lblhorarios)).setText(lugar.opening_hours.getOpening_hours());
        Glide.with(view).load(lugar.icon).into(image);
        return view;
    }
}
