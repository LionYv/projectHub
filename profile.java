package com.example.theproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class profile extends AppCompatActivity {
    RelativeLayout profileLayout;
    ImageView test;
    TextView usertv , progressTxt , pickStats , rankingTv;
    ProgressBar progressCircle;
    BottomNavigationView bottomNavigationView;
    ArrayList<Match> matchArrayList;
    Match [] TodayMatches;
    String userName;
    boolean flagNotif = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        overridePendingTransition(0, 0);
        if (getIntent().getExtras().get("gamelist") == null)
        {

            flagNotif = true;
        }


        makeRankings();
        matchArrayList = new ArrayList<>();
        userName = getIntent().getStringExtra("username");
        if (!flagNotif) {
            try {
                TodayMatches = (Match[]) getIntent().getExtras().get("gamelist");
                for (int i = 0; i < TodayMatches.length; i++) {
                    if (TodayMatches[i] != null) {
                        matchArrayList.add(TodayMatches[i]);
                    }
                }
            } catch (Exception e) {
                System.out.println("ISNT MATCH[] ::: IS ARRAYLIST");
                matchArrayList = (ArrayList) getIntent().getExtras().get("gamelist");
                TodayMatches = new Match[matchArrayList.size()];
                for (int i = 0; i < matchArrayList.size(); i++) {
                    TodayMatches[i] = matchArrayList.get(i);
                }
            }


        }


        pickStats = findViewById(R.id.pickStatsTv);
        progressTxt = findViewById(R.id.progressText);
        progressCircle = findViewById(R.id.progressCircle);
        progressCircle.setVisibility(View.INVISIBLE);
        rankingTv = findViewById(R.id.rankingTv);
        setProgress();

        bottomNavigationView = findViewById(R.id.profileNavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.one)
                {
                    item.setChecked(true);

                }
                if (item.getItemId() == R.id.two)
                {
                    if (!flagNotif) {




                        Intent intent = new Intent(profile.this, gamelist.class);
                        intent.putExtra("username", userName);
                        intent.putExtra("jsonhome", getIntent().getStringExtra("jsonhome"));
                        intent.putExtra("jsonaway", getIntent().getStringExtra("jsonaway"));
                        intent.putExtra("gamelist", matchArrayList);
                        startActivity(intent);
                    }
                    else
                        {
                            Intent intent = new Intent(profile.this, gamelist.class);
                            intent.putExtra("username", userName);

                            startActivity(intent);

                        }

                }
                if (item.getItemId() == R.id.three)
                {

                    Intent intent = new Intent(profile.this , picks.class);
                    intent.putExtra("username" , userName);
                    intent.putExtra("jsonhome" , getIntent().getStringExtra("jsonhome"));
                    intent.putExtra("jsonaway" , getIntent().getStringExtra("jsonaway"));
                    intent.putExtra("gamelist" , matchArrayList);
                    startActivity(intent);


                }
                return false;
            }


        });
        usertv = findViewById(R.id.userTv);
        usertv.setText("Hello " + userName);
         test = findViewById(R.id.test);

        DatabaseReference ref = database.getReference("users").child(userName).child("fav");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                findTeamLink(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void makeRankings()
    {
        HashMap<String , Long> rankings = new HashMap<>();

        DatabaseReference ref = database.getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot usernames : snapshot.getChildren())
                {
                    for (DataSnapshot points : usernames.getChildren()) {
                        if (points.hasChild("correct")) {
                            System.out.println("A :" + (long) points.child("correct").getValue());
                            rankings.put(usernames.getKey() , (long)  points.child("correct").getValue());
                        }
                    }

                }
                System.out.println("RANKINGS 2 : " + rankings);
                checkPosition(rankings);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void checkPosition(HashMap<String , Long> rankings)
    {
        DatabaseReference ref = database.getReference("users").child(userName).child("points").child("correct");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                setPosition((long)snapshot.getValue() , rankings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setPosition(long userScore , HashMap<String , Long> scoresMap)
    {
        int sum = 0;
        int smaller = 0;
        for (long score : scoresMap.values())
        {
            if (userScore> score )
            {
                smaller++;
            }
            sum++;
        }
        sum = sum-smaller;
        rankingTv.setText("your overall rank is: #" + sum);

    }





    public void setProgress() // sets progress of circle bar AND! sets text views
    {
        DatabaseReference ref = database.getReference("users").child(userName).child("points");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long correct = (long) snapshot.child("correct").getValue();
                long overall = (long) snapshot.child("overall").getValue();
                pickStats.setText("you have made " + correct + " correct picks\n out of " + overall +" overall picks");
                double prog =  (double) correct/overall;
                prog*=100;
                String progress = (int)prog + "%";
                progressTxt.setText(progress);
                progressCircle.setProgress((int )prog);
                progressCircle.setVisibility(View.VISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.settingsmenu , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.changepass)
        {
            Dialog passDialog = new Dialog(this);// add layout
            passDialog.setTitle("Title"); 
            passDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void findTeamLink(String team)
    {
        DatabaseReference ref = database.getReference("teams");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot leagues : snapshot.getChildren())
                {
                    for (DataSnapshot teams : leagues.getChildren())
                    {
                        if (teams.getKey().equals(team))
                        {

                           convertLink( teams.getValue().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void convertLink(String link) {
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                URL url = null;

                Bitmap bmp = null;
                try {
                    url = new URL(link);


                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;
            }

            @Override
            protected void onPostExecute(Object o) {
                Bitmap bitmap = (Bitmap) o;
                test.setImageBitmap(bitmap);

            }

        };
        asyncTask.execute();


    }


}