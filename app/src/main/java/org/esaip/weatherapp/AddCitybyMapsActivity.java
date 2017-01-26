package org.esaip.weatherapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.esaip.weatherapp.R.id.map;

public class AddCitybyMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }

    private LatLng position;
    private String city;
    private MarkerOptions markerOptions = new MarkerOptions();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cityb_maps);
        textView = (TextView)findViewById(R.id.editText);
        Button button=(Button)findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                  if (textView.getText().length() < 1) {
                      Toast.makeText(getApplicationContext(), "la recherche est vide", Toast.LENGTH_LONG).show();
                  } else {
                      Intent result = new Intent();
                      result.putExtra("city", city);
                      setResult(2, result);
                      finish();
                  }
              }
          });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    public void onMapSearch(View view) {
        textView = (EditText) findViewById(R.id.editText);
        String location = textView.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMapClickListener(this);

        position = new LatLng(47.47,-0.55);
        mMap.addMarker(markerOptions.position(position));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(position));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onMapClick(LatLng point){
        Toast.makeText(this, "press longer to place a marker", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        setPosition(latLng);
        mMap.clear();
        mMap.addMarker(markerOptions
                .position(position)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        setPosition(markerOptions.getPosition());
        getCityByLatLng(getPosition());
        if (city != ""){
            textView.setText(city);
        }
    }

    public void getCityByLatLng(LatLng position){
        double lat = position.latitude;
        double lng = position.longitude;
        //String city = "vide";

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            List<Address> addresses = geoCoder.getFromLocation(lat, lng , 1);
            if (addresses.size() > 0)
            {
                city = (addresses.get(0).getLocality());
                Log.d("city", ""+city);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //Toast.makeText(this, city, Toast.LENGTH_LONG).show();
    }
}