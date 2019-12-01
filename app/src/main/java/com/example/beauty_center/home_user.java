package com.example.beauty_center;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;



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


@SuppressWarnings("unchecked")
public class home_user extends FragmentActivity implements OnMapReadyCallback {

    SearchView search;
    private ListView beautyList;
    private ImageView filter;
    private ImageView offer;
    private TextView test;
    private userAdapter adapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    ArrayList<User> userList=new ArrayList<User>();

     double currentLatitude ;
     double currentLongitude ;





    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this,"Map is ready",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;


        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            mMap.setMyLocationEnabled(true);
        }

        // here was the code of this is for retrive data from firebase and display it on listview

    }

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private  static final float Deafult_ZOOM=15f;



    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final BitmapDescriptor bitmap;
        bitmap=bitmapDescriptorFromVector(this, R.drawable.ic_woman_head_silhouette_with_short_hair_map);
        beautyList = (ListView) findViewById(R.id.Beauty_List);


        getLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //retrive the users to the map
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    float distance=5000;
                    double currentLatitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("latitude").getValue(Double.class);
                    double currentLongitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("longitude").getValue(Double.class);

                    User user = postSnapshot.getValue(User.class);
                    //this to
                    double latitude = user.getLatitude();
                    double longitude =user.getLongitude();
                    String type = user.getType();

                    float[] results = new float[1];

                    Location.distanceBetween(currentLatitude,currentLongitude,latitude,longitude,results);


                    if (type.equals("salonOwner")&& results[0]<distance) {

                        userList.add(user);
                        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).icon(bitmap).title("salon");
                        mMap.addMarker(marker);
                    }

                    // TODO: handle the post
                }

                adapter=new userAdapter(home_user.this,userList );
                beautyList.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //end retrive data



        //this is for filter data the last phase of the project

         // test=(TextView)findViewById(R.id.test);
       filter = (ImageView) findViewById(R.id.filter);



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] HasOffer = new int[1];
                HasOffer[0]=0;
                final int[] Wait = new int[1];
                Wait[0]=0;
                final int[] Distance = new int[1];
                Distance[0]=0;
                AlertDialog.Builder builder=new AlertDialog.Builder(home_user.this);
                String[] SortFilter=new String[]{"Less Distance","Less Waiting","Has offer"};
                final boolean[][] checkedItems = {new boolean[]{
                        false, false, false
                }};

                final List <String> sortFilterList= Arrays.asList(SortFilter);
                builder.setTitle("Sort and Filter");
                builder.setMultiChoiceItems(SortFilter, checkedItems[0], new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[0][which]=isChecked;
                        String item=sortFilterList.get(which);
                        Toast.makeText(home_user.this,item+" "+ isChecked,Toast.LENGTH_SHORT).show();
                    }
                });


                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                       // test.setText("your selected item \n");
                        for(int i = 0; i< checkedItems[0].length; i++){
                            boolean checked= checkedItems[0][i];
                            if (checked){
                              //  test.setText(test.getText()+sortFilterList.get(i)+"\n");
//here just filter by has offer
                               if (sortFilterList.get(i).equals("Has offer")){HasOffer[0]=1;}

                               if(sortFilterList.get(i).equals("Less Waiting")){Wait[0]=1;}

                               if(sortFilterList.get(i).equals("Less Distance")){Distance[0]=1;}




                            } }
// start filter by offer
                        if (HasOffer[0]==1){

                            if(Wait[0]==1){
                                Toast.makeText(home_user.this,"has offer condition and  wait",Toast.LENGTH_SHORT).show();

                                userList.clear();
                                mMap.clear();
                                adapter.notifyDataSetChanged();

                                mDatabase.child("users").orderByChild("NumberOfReservation").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            // TODO: handle the post

                                            if(  postSnapshot.hasChild("offer")){

                                                float distance=5000;
                                                double currentLatitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("latitude").getValue(Double.class);
                                                double currentLongitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("longitude").getValue(Double.class);

                                                User user = postSnapshot.getValue(User.class);
                                                double latitude = user.getLatitude();
                                                double longitude =user.getLongitude();
                                                String type = user.getType();

                                                float[] results = new float[1];

                                                Location.distanceBetween(currentLatitude,currentLongitude,latitude,longitude,results);


                                                if (type.equals("salonOwner")&& results[0]<distance) {

                                                    userList.add(user);
                                                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).icon(bitmap).title("salon");
                                                    mMap.addMarker(marker);
                                                }
                                            }
                                        }

                                        adapter=new userAdapter(home_user.this,userList );
                                        beautyList.setAdapter(adapter);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else if(Distance[0]==1){
                                Toast.makeText(home_user.this,"has offer condition and distance",Toast.LENGTH_SHORT).show();
                                userList.clear();
                                mMap.clear();
                                adapter.notifyDataSetChanged();

                                mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            // TODO: handle the post

                                            if(  postSnapshot.hasChild("offer")){

                                                float distance=5000;
                                                double currentLatitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("latitude").getValue(Double.class);
                                                double currentLongitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("longitude").getValue(Double.class);

                                                User user = postSnapshot.getValue(User.class);
                                                double latitude = user.getLatitude();
                                                double longitude =user.getLongitude();
                                                String type = user.getType();

                                                float[] results = new float[1];

                                                Location.distanceBetween(currentLatitude,currentLongitude,latitude,longitude,results);


                                                if (type.equals("salonOwner")&& results[0]<distance) {

                                                    userList.add(user);
                                                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).icon(bitmap).title("salon");
                                                    mMap.addMarker(marker);
                                                }
                                            }
                                        }

                                        adapter=new userAdapter(home_user.this,userList );
                                        beautyList.setAdapter(adapter);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else{
                                //offer condition only
                                Toast.makeText(home_user.this,"has offer condition",Toast.LENGTH_SHORT).show();

                                userList.clear();
                                mMap.clear();
                                adapter.notifyDataSetChanged();

                                mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            // TODO: handle the post

                                            if(  postSnapshot.hasChild("offer")){

                                                float distance=5000;
                                                double currentLatitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("latitude").getValue(Double.class);
                                                double currentLongitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("longitude").getValue(Double.class);

                                                User user = postSnapshot.getValue(User.class);
                                                double latitude = user.getLatitude();
                                                double longitude =user.getLongitude();
                                                String type = user.getType();

                                                float[] results = new float[1];

                                                Location.distanceBetween(currentLatitude,currentLongitude,latitude,longitude,results);


                                                if (type.equals("salonOwner")&& results[0]<distance) {

                                                    userList.add(user);
                                                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).icon(bitmap).title("salon");
                                                    mMap.addMarker(marker);
                                                }
                                            }
                                        }

                                        adapter=new userAdapter(home_user.this,userList );
                                        beautyList.setAdapter(adapter);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });}

                        }

                        else if (Wait[0]==1){
                            Toast.makeText(home_user.this,"wait only condition",Toast.LENGTH_SHORT).show();

                            userList.clear();
                            mMap.clear();
                            adapter.notifyDataSetChanged();

                            mDatabase.child("users").orderByChild("NumberOfReservation").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        // TODO: handle the post

                                            float distance=5000;
                                            double currentLatitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("latitude").getValue(Double.class);
                                            double currentLongitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("longitude").getValue(Double.class);

                                            User user = postSnapshot.getValue(User.class);
                                            double latitude = user.getLatitude();
                                            double longitude =user.getLongitude();
                                            String type = user.getType();

                                            float[] results = new float[1];

                                            Location.distanceBetween(currentLatitude,currentLongitude,latitude,longitude,results);


                                            if (type.equals("salonOwner")&& results[0]<distance) {

                                                userList.add(user);
                                                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).icon(bitmap).title("salon");
                                                mMap.addMarker(marker);
                                            }

                                    }

                                    adapter=new userAdapter(home_user.this,userList );
                                    beautyList.setAdapter(adapter);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        else if (Distance[0]==1){
                            Toast.makeText(home_user.this,"distance only condition",Toast.LENGTH_SHORT).show();

                            userList.clear();
                            mMap.clear();
                            adapter.notifyDataSetChanged();

                            mDatabase.child("users").orderByChild("Distance").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                        // TODO: handle the post

                                            float distance=5000;
                                            double currentLatitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("latitude").getValue(Double.class);
                                            double currentLongitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("longitude").getValue(Double.class);

                                            User user = postSnapshot.getValue(User.class);
                                            double latitude = user.getLatitude();
                                            double longitude =user.getLongitude();
                                            String type = user.getType();

                                            float[] results = new float[1];

                                            Location.distanceBetween(currentLatitude,currentLongitude,latitude,longitude,results);


                                            if (type.equals("salonOwner")&& results[0]<distance) {

                                                userList.add(user);
                                                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).icon(bitmap).title("salon");
                                                mMap.addMarker(marker);
                                            }

                                    }

                                    adapter=new userAdapter(home_user.this,userList );
                                    beautyList.setAdapter(adapter);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        //end of filter by offer

                        //start less waiting
                       /* if (Wait[0]==1){

                            if (HasOffer[0]==1){

                                Toast.makeText(home_user.this,"wait and offer",Toast.LENGTH_SHORT).show();

                                userList.clear();
                                mMap.clear();
                                adapter.notifyDataSetChanged();

                                mDatabase.child("users").orderByChild("NumberOfReservation").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            // TODO: handle the post

                                            if(  postSnapshot.hasChild("offer")){

                                                float distance=5000;
                                                double currentLatitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("latitude").getValue(Double.class);
                                                double currentLongitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("longitude").getValue(Double.class);

                                                User user = postSnapshot.getValue(User.class);
                                                double latitude = user.getLatitude();
                                                double longitude =user.getLongitude();
                                                String type = user.getType();

                                                float[] results = new float[1];

                                                Location.distanceBetween(currentLatitude,currentLongitude,latitude,longitude,results);


                                                if (type.equals("salonOwner")&& results[0]<distance) {

                                                    userList.add(user);
                                                    MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).icon(bitmap).title("salon");
                                                    mMap.addMarker(marker);
                                                }
                                            }
                                        }

                                        adapter=new userAdapter(home_user.this,userList );
                                        beautyList.setAdapter(adapter);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                        */
                       /* else {
                                Toast.makeText(home_user.this,"wait only",Toast.LENGTH_SHORT).show();

                                userList.clear();
                                    mMap.clear();
                                    adapter.notifyDataSetChanged();

                                    mDatabase.child("users").orderByChild("NumberOfReservation").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                                // TODO: handle the post

                                                    float distance=5000;
                                                    double currentLatitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("latitude").getValue(Double.class);
                                                    double currentLongitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("longitude").getValue(Double.class);

                                                    User user = postSnapshot.getValue(User.class);
                                                    double latitude = user.getLatitude();
                                                    double longitude =user.getLongitude();
                                                    String type = user.getType();

                                                    float[] results = new float[1];

                                                    Location.distanceBetween(currentLatitude,currentLongitude,latitude,longitude,results);


                                                    if (type.equals("salonOwner")&& results[0]<distance) {

                                                        userList.add(user);
                                                        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).icon(bitmap).title("salon");
                                                        mMap.addMarker(marker);
                                                    }

                                            }

                                            adapter=new userAdapter(home_user.this,userList );
                                            beautyList.setAdapter(adapter);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        //end less waiting

*/

                    }
                });


                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // cancel the sort and filter
                        userList.clear();
                        mMap.clear();
                        adapter.notifyDataSetChanged();


                        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //retrive the users to the map
                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                                    float distance=5000;
                                    double currentLatitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("latitude").getValue(Double.class);
                                    double currentLongitude = dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("longitude").getValue(Double.class);

                                    User user = postSnapshot.getValue(User.class);
                                    double latitude = user.getLatitude();
                                    double longitude =user.getLongitude();
                                    String type = user.getType();

                                    float[] results = new float[1];

                                    Location.distanceBetween(currentLatitude,currentLongitude,latitude,longitude,results);


                                    if (type.equals("salonOwner")&& results[0]<distance) {

                                        userList.add(user);
                                        MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).icon(bitmap).title("salon");
                                        mMap.addMarker(marker);
                                    }

                                    // TODO: handle the post
                                }

                                adapter=new userAdapter(home_user.this,userList );
                                beautyList.setAdapter(adapter);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        //end retrive data


                    }
                });

                AlertDialog dialog=builder.create();

                dialog.show();
            }
        });

       /* filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                final PopupMenu popup = new PopupMenu(home_user.this, filter);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());



                //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {


                        Toast.makeText(
                                home_user.this,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                    }
                });
               popup.show(); //showing popup menu
            }
        }); //closing


*/

        //end filter data

        // offer=(ImageView)findViewById(R.id.offer);


        //   Query Q=mDatabase.orderByChild("NumberOfReservation");


// this is for search
        search=(SearchView) findViewById(R.id.search);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                userList.clear();
                mMap.clear();
                adapter.notifyDataSetChanged();

                final String text=newText;


                beautyList = (ListView) findViewById(R.id.Beauty_List);
                //  offer=(ImageView)findViewById(R.id.offer);

                mDatabase.child("users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            // TODO: handle the post

                            User user = postSnapshot.getValue(User.class);

                            String name=user.getBeauty_Center_name();
                            double latitude=user.getLatitude();
                            double longitude=user.getLongitude();
                            String type = user.getType();

                            if (type.equals("salonOwner")&& name.contains(text)) {


                                userList.add(user);
                                MarkerOptions marker = new MarkerOptions().position(new LatLng(latitude,longitude)).icon(bitmap).title("salon");
                                mMap.addMarker(marker);

                            }

                        }


                        adapter=new userAdapter(home_user.this,userList );
                        beautyList.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                return false;
            }
        });

        //end search here











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
                           final  Location currentLocation=(Location)task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), Deafult_ZOOM);

                            currentLatitude=currentLocation.getLatitude();
                            currentLongitude=currentLocation.getLongitude();

                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("latitude").setValue(currentLocation.getLatitude());
                            mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).child("longitude").setValue(currentLocation.getLongitude());


                        }else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(home_user.this,"unable to get current location ",Toast.LENGTH_SHORT).show();
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

        mapFragment.getMapAsync(home_user.this);

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


    private BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes  int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_placeholder);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }



    public void Filter(View view){


    }


}
