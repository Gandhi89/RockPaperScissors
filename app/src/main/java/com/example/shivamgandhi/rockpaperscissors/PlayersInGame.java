package com.example.shivamgandhi.rockpaperscissors;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class PlayersInGame extends AppCompatActivity implements View.OnClickListener {

    TextView gameID,showPlayers;
    Button start;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    Vars mVars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_in_game);

        initializeAll();
        onClickEvents();

        gameID.setText(mVars.getGameID());
        mDatabaseReference.child("Games").child(mVars.getGameID()).child("Players").addChildEventListener(new playerAddedListner());


    }// end of onCreate()

    private void onClickEvents() {
        start.setOnClickListener(this);
    }

    private void initializeAll() {

        gameID = findViewById(R.id.PlayerInGame_gameId);
        showPlayers = findViewById(R.id.PlayerInGame_showPlayer);
        start = findViewById(R.id.PlayerInGame_start);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mVars = Vars.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.PlayerInGame_start:
                Intent intent = new Intent(PlayersInGame.this,HomeActivity.class);
                startActivity(intent);
                break;

        }
    }

    private class playerAddedListner implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            Log.d("datasnap",dataSnapshot.toString());

            Player mPlayer = dataSnapshot.getValue(Player.class);
            showPlayers.append("\n");
            showPlayers.append(mPlayer.name);
            showPlayers.append("\n");

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    private class watchChild implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("data",dataSnapshot.toString());
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
