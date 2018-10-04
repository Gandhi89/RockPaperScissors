package com.example.shivamgandhi.rockpaperscissors.Activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivamgandhi.rockpaperscissors.Adapter.Adapter_WaitingActivity_PlayerStatus;
import com.example.shivamgandhi.rockpaperscissors.R;
import com.example.shivamgandhi.rockpaperscissors.Utils.Player;
import com.example.shivamgandhi.rockpaperscissors.Utils.Vars;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WaitingActivity extends AppCompatActivity implements View.OnClickListener {

    TextView gameIDTv, cntdwnTxt, cntdwnTxt1;
    Button readyBtn, cancelBtn;
    ListView playersListView;
    GameDatabase mGameDatabase;
    Vars mVars;
    private static final String FORMAT = "%02d:%02d:%02d";
    FirebaseDatabase mFirebaseDatabase;
    ArrayList<String> players;
    ArrayList<String> status;
    DatabaseReference mDatabaseReference;
    CountDownTimer mCountDownTimer;
    Adapter_WaitingActivity_PlayerStatus adapter_waitingActivity_playerStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        initializeAll();
        onClickEvents();
        adapter_waitingActivity_playerStatus = new Adapter_WaitingActivity_PlayerStatus(WaitingActivity.this,players,status);
        playersListView.setAdapter(adapter_waitingActivity_playerStatus);

        // watch for new player in game AND status change of player[Ready/notReady] in same game
        mDatabaseReference.child("Games").child(mVars.getGameID()).child("Players").addChildEventListener(new PlayerAddedListener());
        // watch for status change of game
        mDatabaseReference.child("Games").child(mVars.getGameID()).child("status").addValueEventListener(new StatusChangeListner());
        // start game in default time[60sec]
        // TODO:- FIND A WAY TO DEAL WITH AUTO START GAME PROBLEM
        startGame();


    } // end of onCreate()

    private void startGame() {
    }

    private void onClickEvents() {
        readyBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    private void initializeAll() {

        gameIDTv = findViewById(R.id.waiting_gID);
        cancelBtn = findViewById(R.id.waiting_btnCancel);
        readyBtn = findViewById(R.id.waiting_btnReady);
        cntdwnTxt = findViewById(R.id.waiting_countDown);
        cntdwnTxt1 = findViewById(R.id.waiting_countDown1);
        playersListView = findViewById(R.id.waiting_listView);

        mGameDatabase = new GameDatabase();
        mVars = Vars.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();

        players = new ArrayList<>();
        status = new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.waiting_btnReady:

                // if player is "READY"
                if (readyBtn.getText().toString().equals("ready")) {
                    mGameDatabase.updateReadyValue("ready");
                    readyBtn.setText("not ready");
                    // cancel countdown
                    if (cntdwnTxt1.getVisibility() == view.VISIBLE) {
                        mCountDownTimer.cancel();
                    }

                } else {

                // if player is "NOT READY"
                    mGameDatabase.updateReadyValue("not ready");
                    readyBtn.setText("ready");
                }
                break;
            case R.id.waiting_btnCancel:

                mGameDatabase.removePlayerFromGame();

                Intent intent = new Intent(WaitingActivity.this, MainActivity.class);
                startActivity(intent);
                break;

        }
    }

    private class PlayerAddedListener implements ChildEventListener {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            /**
             * Called when New player joins game
             */
            Player player = dataSnapshot.getValue(Player.class);
            players.add(player.name);
            status.add(player.ready);
            adapter_waitingActivity_playerStatus.notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            /**
             * Called when player changes ready status
             */
            Player player = dataSnapshot.getValue(Player.class);
            String pName = player.name;
            String pStat = player.ready;

            for(int i=0;i<players.size();i++)
            {
                if (pName.equals(players.get(i)))
                {
                    status.set(i,pStat);
                    adapter_waitingActivity_playerStatus.notifyDataSetChanged();
                }
            }

            if(checkAllReady(status))
            {
                startTimerForTen();
            }
            else {
                cntdwnTxt1.setVisibility(View.INVISIBLE);
                // change status of game to "waiting"
                mGameDatabase.changeGameStatus("waiting");
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            /**
             * Called when Player leaves game
             */
            Player player = dataSnapshot.getValue(Player.class);
            String pName = player.name;

            for (int i = 0; i < players.size(); i++) {
                if (pName.equals(players.get(i))) {
                    players.remove(i);
                    status.remove(i);
                    adapter_waitingActivity_playerStatus.notifyDataSetChanged();
                }
            }

            Toast.makeText(WaitingActivity.this, ""+pName+" left game", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }

    // all players are ready -> show 10 sec timer
    private void startTimerForTen() {

        cntdwnTxt.setVisibility(View.INVISIBLE);
        cntdwnTxt1.setVisibility(View.VISIBLE);
        // change status of game to "All Ready"
        mGameDatabase.changeGameStatus("All Ready");
        mCountDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                cntdwnTxt1.setText("starting in:- " + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {

                mDatabaseReference.child("Games").child(mVars.getGameID()).child("status").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String stat = dataSnapshot.getValue().toString();
                        if (stat.equals("All Ready")) {
                            // change status of game to PLAY
                            mDatabaseReference.child("Games").child(mVars.getGameID()).child("status").setValue("play");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }.start();
    }

    private boolean checkAllReady(ArrayList<String> status) {

        Boolean b = true;

        for(int i = 0; i<status.size();i++)
        {
            if (status.get(i).equals("ready"))
            {}
            else{
               b = false;
            }
        }
        return b;
    }

    private class StatusChangeListner implements ValueEventListener {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Toast.makeText(getApplicationContext(),"game status: "+dataSnapshot.getChildren().toString(),Toast.LENGTH_SHORT);
            if (dataSnapshot.getValue().equals("play"))
            {
                Intent intent = new Intent(WaitingActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
