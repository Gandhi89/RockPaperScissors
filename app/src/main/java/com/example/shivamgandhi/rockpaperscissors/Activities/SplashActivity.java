package com.example.shivamgandhi.rockpaperscissors.Activities;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.shivamgandhi.rockpaperscissors.R;
import com.example.shivamgandhi.rockpaperscissors.Utils.User;
import com.example.shivamgandhi.rockpaperscissors.Utils.Vars;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    LocationCallback mLocationCallback;
    private PermissionListener mPermissionListener;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    GameDatabase mGameDatabase;
    Vars mVars;
    HashMap<String,String> userCredential;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initializeAll();
        getCredential();
        getCurrent();
        new CountDownTimer(7000,1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                mVars.setUserCredential(userCredential);
                Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }.start();
        
    }// end of onCreate()

    private void getCredential() {
        mDatabaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapsot:dataSnapshot.getChildren()){
                    String pk = postSnapsot.getKey();
                    User mUser = postSnapsot.getValue(User.class);
                    String email = mUser.Email;
                    userCredential.put(pk,email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initializeAll() {

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mVars = Vars.getInstance();
        mGameDatabase = new GameDatabase();
        userCredential = new HashMap<>();
    }

    private void getCurrent() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // set up the location request to
        // ask for new location every 10 seconds
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000); // 10 second interval
        mLocationRequest.setFastestInterval(10000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        // setup permission listener
        createPermissionListener();

        // request permissions
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(mPermissionListener)
                .check();
        update_location();

    }
    public void createPermissionListener() {
        mPermissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Log.d("SplashScn/lctn is:-","PERMISSION GRANTED!");
                createLocationCallback();
                getLocation();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
            }
        };
    }

    public void update_location() {
        Log.d("SplashScn/lctn is:-", "location updates pressed");
        createLocationCallback();
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }
        catch (SecurityException e) {
            Log.d("SplashScn/lctn is:-", "Exception during loc updates: " + e.toString());
            Log.d("SplashScn/lctn is:-", "Exception during loc updates: " + e.toString());
        }
    }

    public void createLocationCallback() {
        if (mLocationCallback == null) {

            mLocationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        Log.d("SplashScn/location", "Location callback - location is null, exiting");
                        return;
                    }

                    for (Location location : locationResult.getLocations()) {
                        Log.d("SplashScn/location","Location callback - found locations");
                        Log.d("SplashScn/lat is:- ", location.getLatitude()+"");
                        Log.d("SplashScn/log is:- ", location.getLongitude()+"");
                        //mGameDatabase.updateCurrentLocation(mVars.getLat(),mVars.getLog());
                        double lat = location.getLatitude();
                        double log = location.getLongitude();
                        mVars.setLatitude(lat);
                        mVars.setLongitude(log);
                        Log.d("SplashScn/database", "location updated");
                        mGameDatabase.updateLocation(mVars.getLongitude(),mVars.getLongitude());
                    }
                };
            };
        }
    }
    public void getLocation() {
        Log.d("SplashScn/location", "trying to get location");
        //Log.d("SplashScn/location", "trying to get location");
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                Log.d("SplashScn/lctn is:-", "getting last known location");
                                Log.d("SplashScn/lctn is:-", "Lat:- " + location.getLatitude());
                                Log.d("SplashScn/lctn is:-", "Long:- " + location.getLongitude());
                                double lat = location.getLatitude();
                                double log = location.getLongitude();
                                mVars.setLatitude(lat);
                                mVars.setLongitude(log);
                                mGameDatabase.updateLocation(mVars.getLatitude(),mVars.getLongitude());

                            }
                            else {
                                Log.d("SplashScn/lctn:-", "last locaiton is null");
                                mVars.setLatitude(00.00);
                                mVars.setLongitude(00.00);
                            }
                        }
                    });
        }
        catch (SecurityException e) {
            Log.d("SplashScn/lctn is:-","CATCH IS NOW");
            Log.d("SplashScn/lctn is:-",e.toString());
        }
    }
}
