package com.example.googlemaps;

import android.graphics.Color;
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
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener {
    GoogleMap mapa;
    public RequestQueue cola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        cola = Volley.newRequestQueue(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        /*LatLng latitud_longitud = latLng;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latitud_longitud, SELECTED_LOCATION_ZOOM_LEVEL);
        mapa.animateCamera(cameraUpdate);*/
        this.mapa.clear();
        /*CircleOptions circulo = new CircleOptions();
        circulo.center(latLng);
        circulo.radius(1500);
        mapa.addCircle(circulo);*/
        this.rectangulo(latLng, 1500);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(latLng.toString());
        this.mapa.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        Marker marker = this.mapa.addMarker(markerOptions);
        //marker.showInfoWindow();
        this.AddMarker(latLng);
        //mapa.setInfoWindowAdapter(new InfoWindow(getApplicationContext(), "prueba"));

    }
    private void rectangulo(LatLng center, double radius) {
        // Crear un objeto LatLngBounds.Builder
        //LatLngBounds.Builder builder = new LatLngBounds.Builder();

        // Calcular las ubicaciones de los cuatro vértices
        LatLng northeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 45);
        LatLng northwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 315);
        LatLng southeast = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 135);
        LatLng southwest = SphericalUtil.computeOffset(center, radius * Math.sqrt(2.0), 225);

        // Crear un objeto PolygonOptions para el rectángulo
        PolygonOptions rectOptions = new PolygonOptions()
                .add(southeast) // Añadir el vértice inferior-izquierdo
                .add(northeast) // Añadir el vértice superior-izquierdo
                .add(northwest) // Añadir el vértice superior-derecho
                .add(southwest) // Añadir el vértice inferior-derecho
                .strokeColor(Color.BLUE) // Establecer el color del borde del rectángulo
                .fillColor(Color.TRANSPARENT); // Establecer el color de relleno del rectángulo (transparente)

// Añadir el polígono al mapa
        mapa.addPolygon(rectOptions);
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
        //String url = "https://my-json-server.typicode.com/RobertoSuarez/mymap/sitios";
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
                            System.out.println("Elemntos de array"+Lugars.length);
                            for (int i = 0; i < Lugars.length; i++) {
                                System.out.println("ENTRO EN RESPONSE");
                                System.out.println("Ubicación LAT: "+ String.valueOf(Lugars[i].geometry.location.lat));
                                System.out.println("Ubicación LOG: "+ String.valueOf(Lugars[i].geometry.location.lng));
                                LatLng posicion = new LatLng(Lugars[i].geometry.location.lat, Lugars[i].geometry.location.lng);
                                /*mapa.addMarker(new MarkerOptions()
                                        .position(new LatLng(Lugars[i].geometry.location.lat, Lugars[i].geometry.location.lng))
                                        .title(Lugars[i].name)).setTag(Lugars[i]);*/

                                mapa.addMarker(new MarkerOptions().position(posicion));
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