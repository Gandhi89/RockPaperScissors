package com.example.shivamgandhi.rockpaperscissors.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivamgandhi.rockpaperscissors.R;
import com.example.shivamgandhi.rockpaperscissors.Utils.Vars;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CalculateResultActivity extends AppCompatActivity {

    TextView resultTv;
    ArrayList<String> userChoice = new ArrayList<>();
    GameDatabase mGameDatabase;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    int count_r = 0, count_p = 0, count_s = 0, count_none = 0;
    Vars mVars;
    Boolean isChampion = false;
    ArrayList<String> winingStatus = new ArrayList<>();
    String getWiningStatus_r, getWiningStatus_p, getWiningStatus_s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_result);

        showProgressDialog();
        initializeAll();

        /**
         * get RPS of all players
         */
        userChoice = mGameDatabase.getRPSValue();

        /**
         *  wait for 4 sec, SYNC purpose
         */
        new CountDownTimer(4000,1000)
        {
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                calculateWinner();
                waitAwhile();
            }
        }.start();

    }// end of onCreate()

    private void waitAwhile() {
        new CountDownTimer(4000,1000){
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                nextRound();
            }
        }.start();
    }

    private void nextRound() {

        // if player wins or draw -> play again
        if (resultTv.getText().toString().equals("win"))
        {
            // SYNC [So that player with "lose" status can be removed from database]
            wait5sec();
        }
        else if(resultTv.getText().toString().equals("draw"))
        {
            Intent intent = new Intent(CalculateResultActivity.this,PlayersInGame.class);
            startActivity(intent);
        }

        // if player loses -> remove player from database
        else
        {
            mGameDatabase.removePlayerFromGame();
            Intent i1 = new Intent(CalculateResultActivity.this,MainActivity.class);
            startActivity(i1);

        }

    }

    private void wait5sec() {
        new CountDownTimer(5000,1000)
        {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                // check if only one player left in game?
                checkIfChampion();
                wait4sec();
            }
        }.start();
    }

    private void checkIfChampion() {
        Log.d("CalculateResult/ckchmp","inside CheckIfChampion method.");

        mDatabaseReference.child("Games").child(mVars.getGameID()).child("Players").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() == 1)
                {
                    isChampion = true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void wait4sec() {

        new CountDownTimer(4000,1000)
        {
            @Override
            public void onTick(long l) { }

            @Override
            public void onFinish() {
                Log.d("CalculateResult/isChamp",isChampion+"");
                if(isChampion){
                    resultTv.setText("You are ultimate winner. THE CHAMPION.");
                }
                else {
                    Intent i = new Intent(CalculateResultActivity.this, PlayersInGame.class);
                    startActivity(i);
                }
            }
        }.start();
    }

    private void calculateWinner() {

        Toast.makeText(CalculateResultActivity.this, userChoice.toString(), Toast.LENGTH_SHORT).show();
        mGameDatabase.setCount_rps(userChoice);

        // get values of number of R/P/S/None
        count_r = mGameDatabase.getCount_r();
        count_p = mGameDatabase.getCount_p();
        count_s = mGameDatabase.getCount_s();
        count_none = mGameDatabase.getCount_n();

        // calculate wining status for R/P/S/None
        winingStatus = mGameDatabase.calResult(count_r, count_p, count_s, count_none);
        Toast.makeText(CalculateResultActivity.this, winingStatus.toString(), Toast.LENGTH_SHORT).show();
        Log.d("Calcula/winingStatus:-", winingStatus.toString());

        // get individual wining status
        getWiningStatus_r = winingStatus.get(0);
        getWiningStatus_p = winingStatus.get(1);
        getWiningStatus_s = winingStatus.get(2);

        // update wining status for each player in database
        mGameDatabase.updatePlayerStatus(getWiningStatus_r, getWiningStatus_p, getWiningStatus_s);

        //show user result
        if (mVars.getRPSvalue().equals("rock")){
            resultTv.setText(getWiningStatus_r);
        }
        else if (mVars.getRPSvalue().equals("paper")){
            resultTv.setText(getWiningStatus_p);
        }
        else if (mVars.getRPSvalue().equals("scissors")){
            resultTv.setText(getWiningStatus_s);
        }
        else
        {
            resultTv.setText("lose");
        }

    }

    private void showProgressDialog() {

        final ProgressDialog progress = new ProgressDialog(this);
        progress.show();
        progress.setCancelable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.dismiss();
            }
        }, 5000);
    }

    private void initializeAll() {

        resultTv = findViewById(R.id.CalculateResultActivity_resulttext);
        mGameDatabase = new GameDatabase();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mVars = Vars.getInstance();
    }
}
