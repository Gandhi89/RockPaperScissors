package com.example.shivamgandhi.rockpaperscissors;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class GameDatabase {

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    public void GameDatabase(){}

    public void initializeVariable(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
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



}
