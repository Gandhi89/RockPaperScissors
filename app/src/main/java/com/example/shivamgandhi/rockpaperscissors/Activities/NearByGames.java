package com.example.shivamgandhi.rockpaperscissors.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shivamgandhi.rockpaperscissors.R;
import com.example.shivamgandhi.rockpaperscissors.Utils.Vars;

public class NearByGames extends AppCompatActivity implements View.OnClickListener {

    EditText gameIDEdt;
    Button joinGameBtn;
    private String gameID;
    Vars mVars;
    GameDatabase mGameDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        initializeAll();
        onClickEvents();
    }

    private void onClickEvents() {
        joinGameBtn.setOnClickListener(this);
    }

    private void initializeAll() {

        gameIDEdt = findViewById(R.id.JoinGame_gameid);
        joinGameBtn = findViewById(R.id.JoinGame_joinGame);
        mVars = Vars.getInstance();
        mGameDatabase = new GameDatabase();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.JoinGame_joinGame:
                gameID = gameIDEdt.getText().toString();
                mVars.setGameID(gameID);
                mVars.setPlayerID(mGameDatabase.joinGame(mVars.getPlayerName(),mVars.getLatitude(),mVars.getLongitude()));
                Intent intent = new Intent(NearByGames.this,PlayersInGame.class);
                startActivity(intent);
                break;
        }
    }
}
