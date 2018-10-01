package com.example.shivamgandhi.rockpaperscissors;

import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.seismic.ShakeDetector;

import java.util.Random;

public class HomeActivity extends AppCompatActivity implements ShakeDetector.Listener {

    TextView timerTv;
    ImageView rpsIv;
    private int shakeCount = 0;
    GameDatabase mGameDatabase;
    Vars mVars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        InitializeAll();

    }

    private void InitializeAll() {

        timerTv = findViewById(R.id.HomeActivity_timer);
        rpsIv = findViewById(R.id.HomeActivity_imageView);
        mGameDatabase = new GameDatabase();
        mVars = Vars.getInstance();

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
                mGameDatabase.setRPSvalue(mVars.getPlayerName(), "rock");
                //btn.setVisibility(View.VISIBLE);
            } else if (numberGenerated == 1) {
                rpsIv.setImageResource(R.drawable.paper);
                mGameDatabase.setRPSvalue(mVars.getPlayerName(), "paper");
                //btn.setVisibility(View.VISIBLE);
            } else if (numberGenerated == 2) {
                rpsIv.setImageResource(R.drawable.scissors);
                mGameDatabase.setRPSvalue(mVars.getPlayerName(), "scissors");
                //btn.setVisibility(View.VISIBLE);
            }
            shakeCount = 0;
        }
    }
}
