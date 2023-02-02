package com.example.theproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class PickHelperAdapter extends ArrayAdapter<PickHelper> {

    Context context;
    List<PickHelper> listPick;

    public PickHelperAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<PickHelper> objects) {
        super(context, R.layout.picklayout , objects);
        this.context = context;
        this.listPick  = objects;
    }


    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PickHelper m = getItem(position);
        String guessMsg;
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.picklayout , parent, false);
        if (m.iscorrect)
        {
            view.setBackgroundColor(Color.GREEN);

        }
        else
            {  view.setBackgroundColor(Color.RED);

            }


        TextView homeT = view.findViewById(R.id.PickHomeTeam);
        TextView awayT = view.findViewById(R.id.PickAwayTeam);

        TextView goals = view.findViewById(R.id.PickGoals);
        TextView guess = view.findViewById(R.id.pickGuess);

        //ShapeableImageView homeP = view.findViewById(R.id.homePhoto);
        //ShapeableImageView awayP = view.findViewById(R.id.awayPhoto);

        TextView result = view.findViewById(R.id.pickResult);
        TextView date = view.findViewById(R.id.PickDate);

        //////setting

        homeT.setText(m.getHometeam());
        awayT.setText(m.getAwayteam());
        goals.setText(m.getHomegoal() + ":" + m.getAwaygoal());




        int homeG   =  Integer.parseInt(m.getHomegoal());
        int awayG   =  Integer.parseInt(m.getAwaygoal());
        date.setText(m.getDate());
        if (homeG > awayG)
        {
            result.setText("game result: "  + m.getHometeam() + " won!");
        }
        else if (homeG < awayG)
        {
            result.setText("game result: "  + m.getAwayteam() + " won!");
        }
        else
            {
                result.setText("game result: draw!"  );

            }


        if (m.getGuess().equals("1"))
        {
            guess.setText("your guess: " + m.getHometeam() + " to win");
        }
        else if(m.getGuess().equals("2"))
        {
            guess.setText("your guess: " + m.getAwayteam() + " to win");

        }
        else
        {
            guess.setText("your guess: draw");

        }




        return view;


    }




}


