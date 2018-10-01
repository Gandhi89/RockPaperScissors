package com.example.shivamgandhi.rockpaperscissors;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class GameDatabase {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    Vars mVars;
    Player mPlayer;

    public void GameDatabase(){}

    public void initializeVariable(){

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mVars = Vars.getInstance();
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to create new game
     * @return gameID
     */
    public String createGame(){

        initializeVariable();
        DatabaseReference games = mDatabaseReference.child("Games");
        DatabaseReference game = games.child(generateRandomNumber());
        game.child("status").setValue("waiting");
        game.child("latitude").setValue("0");
        game.child("longitude").setValue("0");
        return game.getKey();
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to create random number
     * @return random number [6 digit]
     */
    public String generateRandomNumber(){

        String randomNumber = "";
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);
        randomNumber = String.valueOf(n);
        return randomNumber;
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to join(player) into the existing game
     * @return PlayerID
     */
    public String joinGame(){

        initializeVariable();
        mPlayer = new Player("player name", "default", "none", "not ready",0.0,0.0);
        DatabaseReference games = mDatabaseReference.child("Games");
        DatabaseReference game = games.child(mVars.getGameID());
        DatabaseReference players = game.child("Players");
        DatabaseReference player = players.push();
        player.setValue(mPlayer);

        return player.getKey();
    }

    // --------------------------------------------------------------------------------------------- //

    /**
     * Function to set RPSvalue of player
     * @param playerName
     * @param RPSvalue
     */
    public void setRPSvalue(String playerName, String RPSvalue){

        mVars = Vars.getInstance();
        mPlayer = new Player(playerName, mVars.getStatus(), RPSvalue, mVars.getReady(),mVars.getLatitude(),mVars.getLongitude());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.child("Games").child(mVars.getGameID()).child("Players").child(mVars.getPlayerID()).setValue(mPlayer);
    }

}
