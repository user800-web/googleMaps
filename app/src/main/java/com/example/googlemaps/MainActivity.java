package com.example.googlemaps;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, OnMapsSdkInitializedCallback {
    GoogleMap mapa;
    Button btnAcercar;
    public RequestQueue cola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, this);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        cola = Volley.newRequestQueue(this);
        btnAcercar = (Button) findViewById(R.id.btnAcercar);
        /*btnAcercar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng latLng = new LatLng(0, 0);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 1);
                mapa.animateCamera(cameraUpdate);
            }
        });*/
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        /*LatLng latitud_longitud = latLng;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latitud_longitud, SELECTED_LOCATION_ZOOM_LEVEL);
        mapa.animateCamera(cameraUpdate);*/
        this.mapa.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.toString());
        this.mapa.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        Marker marker = this.mapa.addMarker(markerOptions);
        //marker.showInfoWindow();
        this.AddMarker(latLng);
        mapa.setInfoWindowAdapter(new InfoWindow(getApplicationContext(), "prueba"));
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setOnMapClickListener(this);
        //this.AddMarker();
    }

    public void AddMarker(LatLng latLng) {
        String latitud= String.valueOf(latLng.latitude);
        String longitud = String.valueOf(latLng.longitude);
        System.out.println("Latitud: "+latitud+" y longitud: "+longitud);
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?fields=name&location="+latitud+","+longitud+"&radius=1500&type=bar&key=AIzaSyB5MkIB5lNnQH1kC1tZ3ATeEsv7z66moKs";
        //String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?fields=name&location=-1.0113558476088707,-79.46938086135764&radius=1500&type=bar&key=AIzaSyB5MkIB5lNnQH1kC1tZ3ATeEsv7z66moKs";

        System.out.println("ENTRO A AddMarker");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject JSONlista = null;
                        try {
                            JSONlista = new JSONObject(response);
                            JSONArray JSONarray = JSONlista.getJSONArray("results");

                            Gson gson = new Gson();
                            Lugar[] Lugars = gson.fromJson(JSONarray.toString(), Lugar[].class);
                            for (int i = 0; i < Lugars.length; i++) {
                                System.out.println("Ubicación LAT: "+ String.valueOf(Lugars[i].geometry.location.lat));
                                System.out.println("Ubicación LOG: "+ String.valueOf(Lugars[i].geometry.location.lng));
                                LatLng posicion = new LatLng(Lugars[i].geometry.location.lat, Lugars[i].geometry.location.lng);
                                /*mapa.addMarker(new MarkerOptions()
                                        .position(new LatLng(Lugars[i].geometry.location.lat, Lugars[i].geometry.location.lng))
                                        .title(Lugars[i].name)).setTag(Lugars[i]);*/

                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(posicion);
                                markerOptions.title(posicion.toString());
                                //mapa.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                Marker marker = mapa.addMarker(markerOptions);

                                /*if (Lugars.length - 1 == i) {
                                    mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 18));
                                }*/
                            }

                        } catch (JSONException e) {
                            System.out.println("Error JSONARRAY: " + e.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("ERROR EN PARSEO");
                    }
                }
        );
        cola.add(stringRequest);
    }

    @Override
    public void onMapsSdkInitialized(@NonNull MapsInitializer.Renderer renderer) {
        switch (renderer) {
            case LATEST:
                Log.d("MapsDemo", "The latest version of the renderer is used.");
                break;
            case LEGACY:
                Log.d("MapsDemo", "The legacy version of the renderer is used.");
                break;
        }
    }

    public void aumentar(View view) {
        //CameraUpdateFactory.zoomIn();
        float maxZoom = mapa.getMaxZoomLevel();
        float currentZoom = mapa.getCameraPosition().zoom;
        if (currentZoom < maxZoom) {
            CameraUpdate zoomIn = CameraUpdateFactory.zoomIn();
            mapa.moveCamera(zoomIn);
        }
        System.out.println("EN AUMENTAR");
    }

    public void alejar(View view) {
        float minZoom = mapa.getMaxZoomLevel();
        float currentZoom = mapa.getCameraPosition().zoom;
        if (currentZoom < minZoom) {
            mapa.moveCamera(CameraUpdateFactory.zoomOut());
        }
        System.out.println("EN ALEJAR");
    }

    public void satelite(View view) {
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        System.out.println("SATELITE");
    }

    public void hibrido(View view) {
        mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        System.out.println("HIBRIDO");
    }

    public void normal(View view) {
        mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        System.out.println("NORMAL");
    }

    public void terreno(View view) {
        mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mapa.getUiSettings().setZoomControlsEnabled(true);
        System.out.println("TERRENO");
    }
}