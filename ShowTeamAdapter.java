package com.example.theproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ShowTeamAdapter extends ArrayAdapter<pickteam.TeamShow>
{
    private Context context;
    private int resource;


    public ShowTeamAdapter(@NonNull Context context, int resource, @NonNull ArrayList<pickteam.TeamShow> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        convertView = layoutInflater.inflate(resource  , parent , false);
        ImageView img = convertView.findViewById(R.id.TeamImage);
        TextView tv = convertView.findViewById(R.id.TeamName);
        img.setImageBitmap(getItem(position).img);
        tv.setText(getItem(position).teamName);
        return convertView;
    }
}
