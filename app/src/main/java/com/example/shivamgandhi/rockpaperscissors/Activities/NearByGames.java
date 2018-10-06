package com.example.shivamgandhi.rockpaperscissors.Activities;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.shivamgandhi.rockpaperscissors.Adapter.Adapter_NearByGames_Games;
import com.example.shivamgandhi.rockpaperscissors.R;
import com.example.shivamgandhi.rockpaperscissors.Utils.Vars;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NearByGames extends AppCompatActivity {

    ListView nearByGamesLv;
    private String gameID;
    Vars mVars;
    GameDatabase mGameDatabase;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    ArrayList<String> games;
    Adapter_NearByGames_Games mAdapter_NearByGames_Games;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        initializeAll();
        mAdapter_NearByGames_Games = new Adapter_NearByGames_Games(NearByGames.this,games);
        nearByGamesLv.setAdapter(mAdapter_NearByGames_Games);

        // get nearby games [range 1km]
        getNeatByGames();

    }// end of onCreate()

    private void getNeatByGames() {

        mDatabaseReference.child("Games").addValueEventListener(new getGames());


    }

    private void initializeAll() {

        nearByGamesLv = findViewById(R.id.NearByGames_listview);
        mVars = Vars.getInstance();
        mGameDatabase = new GameDatabase();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        games = new ArrayList<>();
    }

    private class getGames implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for(DataSnapshot postData:dataSnapshot.getChildren())
            {
                Log.d("EnterId/allData",postData.toString());
                double lat = (double) postData.child("latitude").getValue();
                double log = (double) postData.child("longitude").getValue();

                LatLng latLngA = new LatLng(lat, log);
                LatLng latLngB = new LatLng(mVars.getLatitude(), mVars.getLongitude());

                Location locationA = new Location(LocationManager.GPS_PROVIDER);
                locationA.setLatitude(latLngA.latitude);
                locationA.setLongitude(latLngA.longitude);
                Location locationB = new Location(LocationManager.GPS_PROVIDER);
                locationB.setLatitude(latLngB.latitude);
                locationB.setLongitude(latLngB.longitude);

                double distance = locationA.distanceTo(locationB);

                if (distance < 1000) {
                    games.add(postData.getKey());
                    mAdapter_NearByGames_Games.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
