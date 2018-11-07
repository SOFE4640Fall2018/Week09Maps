package com.example.sofe4640.myapplication;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<LatLng> listPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        listPoints = new ArrayList<LatLng>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                        if (listPoints.size()>1){
                            listPoints.clear();
                            mMap.clear();
                        }else{
                            listPoints.add(latLng);
                            mMap.addMarker(new MarkerOptions().position(latLng).title(""));
                            double x= latLng.longitude;
                            double y= latLng.latitude;

                            //todo  show the address of the click
                            // Address newAddress = new Address(new LatLng(x,y));
                            //newAddress.getCountryCode();


                        }
            }
        });

    }

    public void getLocation(View v) throws IOException {
        hideSoftKeyboard(v);
        EditText et = (EditText)findViewById(R.id.txtAddress);
        String inputAddress = et.getText().toString();
        mMap.getUiSettings().setZoomControlsEnabled(true);

        Geocoder gc= new Geocoder(this);
        List<Address> address = gc.getFromLocationName(inputAddress,1);
        Address add = address.get(0);
        double lat = add.getLatitude();
        double log = add.getLongitude();

        if (lat!=0 && log !=0) {
            Toast.makeText(this,"Address Found"+ lat + " " + log,Toast.LENGTH_LONG).show();

            gotoLocation(lat,log);
        }
            else
            Toast.makeText(this,"Address not Found",Toast.LENGTH_LONG).show();


    }

    private void gotoLocation(double lat, double log) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .tilt(40)
                .target(new LatLng(lat,log))
                .bearing(90)
                .zoom(17)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //add a marker
        mMap.addMarker(new MarkerOptions().position(new LatLng(lat,log))); //
    }


    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(),0);
    }
}
