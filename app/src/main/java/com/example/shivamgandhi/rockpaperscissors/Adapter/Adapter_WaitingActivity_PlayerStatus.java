package com.example.shivamgandhi.rockpaperscissors.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shivamgandhi.rockpaperscissors.R;

import java.util.ArrayList;
import java.util.HashMap;

public class Adapter_WaitingActivity_PlayerStatus extends BaseAdapter{

    Context context;
    ArrayList<String> players;
    ArrayList<String> status;


    public Adapter_WaitingActivity_PlayerStatus(Context context, ArrayList<String> players, ArrayList<String> status){
        this.context = context;
        this.players = players;
        this.status = status;
    }

    @Override
    public int getCount() {
        return players.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        View V = view;
        if (V == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(context);
            V = vi.inflate(R.layout.adapter_ws_playerstatus, null);
        }
        String playerName = players.get(position);
        String stat = status.get(position);
        TextView tv1 = V.findViewById(R.id.temp_name);
        TextView tv2 = V.findViewById(R.id.temp_status);

        tv1.setText(playerName);
        tv2.setText(stat);

        return V;
    }
}
