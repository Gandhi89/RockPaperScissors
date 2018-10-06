package com.example.shivamgandhi.rockpaperscissors.Activities;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shivamgandhi.rockpaperscissors.R;
import com.example.shivamgandhi.rockpaperscissors.Utils.Player;
import com.example.shivamgandhi.rockpaperscissors.Utils.Vars;
import com.squareup.seismic.ShakeDetector;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity implements ShakeDetector.Listener {

    TextView timerTv;
    ImageView rpsIv;
    private int shakeCount = 0;
    GameDatabase mGameDatabase;
    Vars mVars;
    Player mPlayers;
    private static final String FORMAT = "%02d:%02d:%02d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        InitializeAll();

        /**
         * timer
         */
        new CountDownTimer(10000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                timerTv.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                timerTv.setText("done!");
                Intent i = new Intent(HomeActivity.this, CalculateResultActivity.class);
                startActivity(i);
            }
        }.start();


    }// end of onCreate()

    private void InitializeAll() {

        timerTv = findViewById(R.id.HomeActivity_timer);
        rpsIv = findViewById(R.id.HomeActivity_imageView);
        mGameDatabase = new GameDatabase();
        mVars = Vars.getInstance();
        mPlayers = new Player();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);
    }

    public void hearShake() {
        shakeCount++;

        if (shakeCount < 6) {
            Toast.makeText(this, "Shake Count :- " + shakeCount, Toast.LENGTH_SHORT).show();
        }
        /**
         *  update RPS value of user
         */
        else if (shakeCount == 6) {
            Random rm = new Random();
            int numberGenerated = rm.nextInt(3);
            if (numberGenerated == 0) {
                rpsIv.setImageResource(R.drawable.rock);
                // set RPSvalue to ROCK in database
                mGameDatabase.setRPSvalue(mVars.getPlayerName(), "rock");
                mVars.setRPSvalue("rock");
                //btn.setVisibility(View.VISIBLE);
            } else if (numberGenerated == 1) {
                rpsIv.setImageResource(R.drawable.paper);
                // set RPSvalue to PAPER in database
                mGameDatabase.setRPSvalue(mVars.getPlayerName(), "paper");
                mVars.setRPSvalue("paper"); ;
                //btn.setVisibility(View.VISIBLE);
            } else if (numberGenerated == 2) {
                rpsIv.setImageResource(R.drawable.scissors);
                // set RPSvalue to SCISSORS in database
                mGameDatabase.setRPSvalue(mVars.getPlayerName(), "scissors");
                mVars.setRPSvalue("scissors");
                //btn.setVisibility(View.VISIBLE);
            }
            shakeCount = 0;
        }
    }
}
