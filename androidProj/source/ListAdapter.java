package com.example.theproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ListAdapter extends ArrayAdapter<Match> {
        Context context;
        List<Match> matches;

    public ListAdapter(@NonNull Context context,  int resource , int textViewResourceId,  List<Match> matchList) {
        super(context, R.layout.games_layout , matchList);
        this.context = context;
        this.matches = matchList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Match m = getItem(position);

            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.games_layout , parent, false);
            //convertView = LayoutInflater.from(getContext()).inflate(R.layout.games_layout,  parent , false);


        TextView league = view.findViewById(R.id.league);

        TextView homeT = view.findViewById(R.id.HomeTeam);
        TextView awayT = view.findViewById(R.id.AwayTeam);

        TextView goals = view.findViewById(R.id.goals);

        ShapeableImageView homeP = view.findViewById(R.id.homePhoto);
        ShapeableImageView awayP = view.findViewById(R.id.awayPhoto);

        TextView status = view.findViewById(R.id.status);


        //////setting
        homeP.setImageResource(R.drawable.ic_launcher_background);
        awayP.setImageResource(R.drawable.ic_launcher_background);


        league.setText(m.getLeague());
        league.setPaintFlags(league.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        homeT.setText(m.getHomeTeam());
        awayT.setText(m.getAwayTeam());

        goals.setText(m.getHomeGoals() + ":" + m.getAwayGoals());
        status.setText(m.getStatus());

        homeP.setImageBitmap(m.getImgHome());
        awayP.setImageBitmap(m.getImgAway());

        if (m.getStatus().equals("hasnt started")) {
            //m.setStatus(m.getTimeOfStart());
            status.setText(m.getTimeOfStart());

            goals.setText("V.S");
        }
        if (m.getStatus().equals("FT"))
            status.setText("full time");
        if (m.getMin() != "")
        {
            status.setText(m.getMin());
        }
        if (m.getStatus().equals("extra time"))
        {
            status.setText(m.getMin());
        }
        if (m.getStatus().equals("in play"))
        {
            //System.out.println("setting status of in play : " + m.getMin() + m.getHomeTeam());
            status.setText(m.getMin());
        }




        return view;
    }
}
