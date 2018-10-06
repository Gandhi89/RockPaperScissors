package com.example.shivamgandhi.rockpaperscissors.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shivamgandhi.rockpaperscissors.R;

import java.util.ArrayList;

public class Adapter_NearByGames_Games extends BaseAdapter {

    Context context;
    ArrayList<String> games;

    public Adapter_NearByGames_Games(Context context, ArrayList<String> games){
        this.context = context;
        this.games = games;
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View V = view;
        if (V == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            V = vi.inflate(R.layout.adapter_nbg_games, null);
        }
        String game = games.get(i);
        Log.d("gameID",game);
        TextView gID = V.findViewById(R.id.adapter_gID);
        gID.setText(game);

        return V;
    }
}
