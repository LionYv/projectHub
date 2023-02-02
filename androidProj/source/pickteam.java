package com.example.theproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class pickteam extends AppCompatActivity implements View.OnClickListener {
    ImageView laliga , bundesliga , ligue , premier , seria;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");
    HashMap<String , String> links;
    ProgressDialog waitForTeams;
    String teamPicked = "";
    ArrayList <TeamShow> arrayList = new ArrayList<>();
    ListView listView;
    String usernamePassed;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pickteam);
        laliga = findViewById(R.id.laliga);
        seria  = findViewById(R.id.seria);
        bundesliga = findViewById(R.id.bundesliga);
        premier = findViewById(R.id.premier);
        ligue = findViewById(R.id.ligue);
        laliga.setImageBitmap(makeFromLink("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6e/LaLiga_Santander_%282%29.svg/400px-LaLiga_Santander_%282%29.svg.png"));
        ligue.setImageBitmap(makeFromLink("https://upload.wikimedia.org/wikipedia/he/thumb/5/5a/Ligue_1_FR.png/640px-Ligue_1_FR.png"));
        bundesliga.setImageBitmap(makeFromLink("https://upload.wikimedia.org/wikipedia/he/thumb/d/df/Bundesliga_logo_%282017%29.svg/1200px-Bundesliga_logo_%282017%29.svg.png"));
        premier.setImageBitmap(makeFromLink("https://seeklogo.com/images/P/premier-league-logo-64B77E2F2E-seeklogo.com.png"));
        seria.setImageBitmap(makeFromLink("https://upload.wikimedia.org/wikipedia/en/6/6c/Serie_A_logo.png"));
        laliga.setOnClickListener(this);
        seria.setOnClickListener(this);
        premier.setOnClickListener(this);
        bundesliga.setOnClickListener(this);
        ligue.setOnClickListener(this);
        listView = findViewById(R.id.ListGames);
        usernamePassed = getIntent().getStringExtra("username");

    }

    @Override
    public void onClick(View v) {
        ArrayList<TeamShow>  teamShows= new ArrayList<>();
        if (v == premier)
        {
            teamShows = fillByLeague("premier league");

        }
        else if (v == laliga) {
            teamShows = fillByLeague("la liga");
        }
        else if(v == ligue)
        {
            teamShows = fillByLeague("ligue 1");
        }
        else if(v == bundesliga)
        {
            teamShows = fillByLeague("bundesliga");
        }
        else
            {
                teamShows = fillByLeague("seria a");
            }




    }
    public ArrayList<TeamShow> fillByLeague(String leagueName)
    {
        arrayList = new ArrayList<>();
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected void onPreExecute() {

                waitForTeams = new ProgressDialog(pickteam.this);
                waitForTeams.setCancelable(false);
                waitForTeams.setTitle("please wait..");

                waitForTeams.setMessage("loading teams....");
                waitForTeams.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                waitForTeams.show();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                DatabaseReference reference = database.getReference("teams").child(leagueName);
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        for (DataSnapshot s : snapshot.getChildren())
                        {
                            arrayList.add(new TeamShow(s.getKey() , makeFromLink((String)s.getValue())));
                        }
                        peula(arrayList);



                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                System.out.println(arrayList.size());
                for (int i = 0; i < arrayList.size(); i++) {
                    System.out.println(arrayList.get(i).teamName);
                }
                return arrayList;
            }

            

        };
        try {
            return (ArrayList<TeamShow>) asyncTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;

    }
    public void peula(ArrayList<TeamShow> arrayList)
    {
        waitForTeams.cancel();
        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        listView = new ListView(this);
        Button button = new Button(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (teamPicked != null)
                {
                    database.getReference("users").child(usernamePassed).child("fav").setValue(teamPicked);
                    openGames(teamPicked);
                }
            }
        });
        button.setText("CONFIRM");
        button.setTextColor(Color.BLACK);
        ShowTeamAdapter showTeamAdapter = new ShowTeamAdapter(this , R.layout.favteamsingle , arrayList);
        listView.setAdapter(showTeamAdapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               TeamShow teamClicked = ((TeamShow) parent.getItemAtPosition(position));
               teamPicked  = teamClicked.teamName;
                System.out.println(teamClicked.teamName);

                for (int i = 0; i < parent.getCount(); i++)
                {

                   View view1 =      parent.getChildAt(i);
                   if (view1 != null)
                   {
                       view1.setBackgroundColor(Color.WHITE);
                   }
                }
                        view.setBackgroundColor(Color.GREEN);



            }
        });
        linearLayout.addView(button);

        linearLayout.addView(listView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(linearLayout);
        final AlertDialog dialog = builder.create();
        dialog.show();

    }
    public class TeamShow
    {
        String teamName;
        Bitmap img;
        public TeamShow(String name,  Bitmap img)
        {
            this.teamName = name;
            this.img = img;
        }
    }








    public void openGames(String teamPicked)
    {
        Intent intent = new Intent(this, gamelist.class);
        intent.putExtra("username" , usernamePassed);
        if (teamPicked != null)
        {
            System.out.println("team pICKED: "  + teamPicked);
            intent.putExtra("favorite" , teamPicked);
        }

        //intent.putExtra("games" , TodayMatches);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
    }







    public Bitmap makeFromLink(String name)
    {
        Bitmap abc;
        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Bitmap bmp = null;
                URL url = null;
                try {
                    url = new URL( name);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;

            }


        };
        try {
            return  (Bitmap) asyncTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


}