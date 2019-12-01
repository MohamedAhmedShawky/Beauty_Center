package com.example.beauty_center;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static java.security.AccessController.getContext;

@SuppressWarnings("unchecked")
public class selectLocation extends FragmentActivity implements OnMapReadyCallback {

    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"Map is ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;


        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
        }

// add marker when touch a screen
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            Marker marker;
            @Override
            public void onMapClick(final LatLng point) {

                if (marker != null) {
                    marker.remove();
                }

                marker =mMap.addMarker(new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("your Salon Location "));
               // mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latitude").setValue(point.latitude);
                //mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("longitude").setValue(point.longitude);

                mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("latitude").setValue(point.latitude);
                        mDatabase.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("longitude").setValue(point.longitude);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //  MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("New Marker");
              //  mMap.addMarker(marker);
               // System.out.println(point.latitude+"---"+ point.longitude);
            }
        });

        }

private static final String TAG = "MapActivity";

private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
private  static final float Deafult_ZOOM=15f;
private DatabaseReference mDatabase;


//vars
private Boolean mLocationPermissionsGranted = false;
private GoogleMap mMap;
private FusedLocationProviderClient mFusedLocationProviderClient;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);






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



private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the device location");

        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);

        try {
        if(mLocationPermissionsGranted){
final Task location=mFusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
@Override
public void onComplete(@NonNull Task task) {
        if(task.isSuccessful()){
        Log.d(TAG, "onComplete: found location");
        Location currentLocation=(Location)task.getResult();
        moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),
        /*this is wew i stopped*/                     Deafult_ZOOM);



                       /* LatLng latLng = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                        MarkerOptions options=new MarkerOptions().position(latLng).title("test");
                        mMap.addMarker(options);*/

        }else {
        Log.d(TAG, "onComplete: current location is null");
        Toast.makeText(selectLocation.this,"unable to get current location ",Toast.LENGTH_SHORT).show();
        }
        }
        });
        }

        }catch (SecurityException e){
        Log.e(TAG, "getDeviceLocation:SecurityException "+e.getMessage() );
        }

        }

private void moveCamera(LatLng latLng,float zoom){
        Log.d(TAG, "moveCamera: moving the camera to "+latLng.latitude+",lng:"+latLng.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        }


private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(selectLocation.this);

        }




private void getLocationPermission() {

        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
        FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
        COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        mLocationPermissionsGranted = true;
        initMap();

        } else {
        ActivityCompat.requestPermissions(this,
        permissions,
        LOCATION_PERMISSION_REQUEST_CODE);
        }
        } else {
        ActivityCompat.requestPermissions(this,
        permissions,
        LOCATION_PERMISSION_REQUEST_CODE);
        }
        }

@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
        case LOCATION_PERMISSION_REQUEST_CODE:{
        if(grantResults.length > 0){
        for(int i = 0; i < grantResults.length; i++){
        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
        mLocationPermissionsGranted = false;
        Log.d(TAG, "onRequestPermissionsResult: permission failed");
        return;
        }
        }
        Log.d(TAG, "onRequestPermissionsResult: permission granted");
        mLocationPermissionsGranted = true;
        //initialize our map
        initMap();
        }
        }
        }
        }

        public void Done(View view){
            Intent login=new Intent(selectLocation.this,MainActivity.class);
            startActivity(login);
            finish();

        }

        }
