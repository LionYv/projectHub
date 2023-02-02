package com.example.theproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.JsonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;

// TODO: I THINK THAT AFTER GOING TO SHOWGAME ACTIVITY IT DOESNT UPDATE THE GAMELIST WHEN WE GO BACK BUT LIKE NEVER AND IF I COME BACK TO GAMELIST AFTER A LOT OF TIME IN SHOWGAME THAN IN SHOWGAME COULD BE 45 MIN BUT IN GAMELIST 20
//  SO MAYBE PEULA THAT UPDATES WHENEVER YOU GO BACK so i need to identify going back(on resume)
public class showgame extends AppCompatActivity implements Serializable, View.OnClickListener {
    TextView home, away, status, goals, league, resultGuess;
    ImageView homeImg;
    ImageView awayImg;
     int counterOfPicks = -100;
    BroadCastTime broadCastTime;
    Intent intent;
    String user;
    static boolean pickdraw, pickaway, pickhome;
    static int cnt = 0;
    Match [] TodayMatches;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_showgame);
        user = getIntent().getStringExtra("username");

        broadCastTime = new BroadCastTime();

        Gson gson = new Gson(); // this is converting json to Match class
        Match gamePassed = gson.fromJson(getIntent().getStringExtra("GameDetails"), Match.class);
         TodayMatches = (Match[]) getIntent().getExtras().getSerializable("allgames");
        Thread t1 = new Thread() {
            @Override
            public void run() {
            }


        };
        t1.start();
        //String bitmaps =    getIntent().getStringExtra("images"); ///bitmap1 + bitmap2 -- need to be split
        //String imghome =     bitmaps.substring(0 , bitmaps.indexOf("+"));
        //String imgaway =    bitmaps.substring(bitmaps.indexOf("+")+1 , bitmaps.length()-1);
        //System.out.println(imghome + " (+) " + imgaway);
        System.out.println("game passed : " + gamePassed.toString());

        AsyncTask asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                DatabaseReference reference =  database.getReference().child("users").child(user).child("points").child("overall");
                reference.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        counterOfPicks =   Integer.parseInt( snapshot.getValue().toString());
                        System.out.println("counter :"  + counterOfPicks);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return counterOfPicks;
            }

        };
        try {
            counterOfPicks = (int )asyncTask.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("number : " + counterOfPicks);
        IntentFilter filter = new IntentFilter("timeIntent");
        IntentFilter updateMinFilter = new IntentFilter(gamePassed.getHomeTeam()); // get intent get extras all games and than postion and than .getHomeTeam
        registerReceiver(broadCastTime, filter);
        registerReceiver(broadCastTime, updateMinFilter);




        resultGuess = findViewById(R.id.resultPopup);
        resultGuess.setOnClickListener(this);
        homeImg = findViewById(R.id.IhomePhoto);
        awayImg = findViewById(R.id.Iawayphoto);
        home = findViewById(R.id.Ihometeam);
        away = findViewById(R.id.Iawayteam);
        status = findViewById(R.id.Istatus);
        goals = findViewById(R.id.Igoals);
        league = findViewById(R.id.Ileague);
        league.setPaintFlags(league.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);



        /*
        home.setText( getIntent().getStringExtra("nameHome"));
        away.setText( getIntent().getStringExtra("nameAway"));
        status.setText( getIntent().getStringExtra("status"));
        goals.setText( getIntent().getStringExtra("goals"));
        league.setText( getIntent().getStringExtra("league"));
        homeImg.setImageBitmap( (Bitmap) getIntent().getExtras().get("imgHome"));
        awayImg.setImageBitmap( (Bitmap) getIntent().getExtras().get("imgAway"));

         */

        /////
        byte[] byteshome = (byte[]) getIntent().getExtras().get("imagehome");
        byte[] bytesaway = (byte[]) getIntent().getExtras().get("imageaway");

        homeImg.setImageBitmap(convert(byteshome));
        awayImg.setImageBitmap(convert(bytesaway));

        home.setText(gamePassed.getHomeTeam());
        away.setText(gamePassed.getAwayTeam());
        status.setText(gamePassed.getStatus());
        goals.setText(gamePassed.getHomeGoals() + ":" + gamePassed.getAwayGoals());
        league.setText(gamePassed.getLeague());

        if (status.getText().equals("FT")) {

            if (Integer.parseInt(gamePassed.getHomeGoals()) > Integer.parseInt(gamePassed.getAwayGoals())) {
                home.setTextColor(Color.parseColor("#2aff00"));
                away.setTextColor(Color.parseColor("#ff3a00"));
            } else if (Integer.parseInt(gamePassed.getHomeGoals()) < Integer.parseInt(gamePassed.getAwayGoals())) {
                away.setTextColor(Color.parseColor("#2aff00"));
                home.setTextColor(Color.parseColor("#ff3a00"));

            } else {
                home.setTextColor(Color.parseColor("#fed119"));
                away.setTextColor(Color.parseColor("#fed119"));

            }

        }
        if (!(goals.getText().toString().equals(":"))) {
            resultGuess.setText("");
            resultGuess.setClickable(false);
            resultGuess.setVisibility(View.INVISIBLE);
        }


        checkIsPicked();

        intent = new Intent(this, UpdateService.class);
        intent.putExtra("status", status.getText());
        intent.putExtra("timeOfStart", gamePassed.getTimeOfStart()); // gamePassed.getTimeOfStart()
        startService(intent);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your winner: ");
        String[] options = {home.getText().toString(), away.getText().toString(), "Draw"};
        builder.setItems(options, actionListener);
        builder.setNegativeButton("Cancel", null);
        AlertDialog actions = builder.create();
        actions.show();

    }
    public Bitmap convert(byte[] bytes){
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bmp;
    }


    DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == 0) // home team
            {
                Toast.makeText(showgame.this, "you chose " + home.getText().toString() + " to win", Toast.LENGTH_SHORT).show();
                resultGuess.setClickable(false);
                resultGuess.setText("you picked: " + home.getText().toString());
            } else if (which == 1) { // away team
                Toast.makeText(showgame.this, "you chose " + away.getText().toString() + " to win", Toast.LENGTH_SHORT).show();
                resultGuess.setClickable(false);

                resultGuess.setText("you picked: " + away.getText().toString());
            } else {
                System.out.println(which);
                Toast.makeText(showgame.this, "you chose draw", Toast.LENGTH_SHORT).show();
                resultGuess.setClickable(false);
                resultGuess.setText("you picked: draw");

            }
            if (!isForegroundServiceRunning()) {
                Intent foregroundIntent = new Intent(showgame.this, ForeGroundService.class);
                foregroundIntent.putExtra("username", getIntent().getStringExtra("username"));
                foregroundIntent.putExtra("allgames", TodayMatches);
                startForegroundService(foregroundIntent);
            }
            countPicks(which);


        }


    };
    public boolean isForegroundServiceRunning()
    {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE))
        {
            if (ForeGroundService.class.getName().equals(service.service.getClassName())){return true;}
        }
        return false;
    }

    public void checkIsPicked() // checks if this game's score had already been predicted in the db
    {
        DatabaseReference reference = database.getReference().child("users").child(user).child("picks").child("ongoing");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String s = ds.getValue().toString();
                    System.out.println("SSSSSSSSS: " + s);
                    // snapshot.getvalue.toString could be either home team away team or both and draw
                    if (s.contains("draw")) {
                        if (s.contains(home.getText().toString()) || s.contains(away.getText().toString())) {
                            System.out.println("A DRAW HAS BEEN PICKED");
                            resultGuess.setText("you picked: DRAW");
                            resultGuess.setClickable(false);
                            pickdraw = true;
                        } else {
                            System.out.println("SSUUSUS");
                        }
                    } else {
                        if (s.contains(home.getText().toString())) {
                            pickhome = true;
                            resultGuess.setText("you picked: " + home.getText().toString());
                            resultGuess.setClickable(false);
                            System.out.println("BEEN PICKED.... HOME: " + home.getText().toString());
                        } else if (s.contains(away.getText().toString())) {
                            pickaway = true;
                            resultGuess.setText("you picked: " + away.getText().toString());
                            resultGuess.setClickable(false);
                            System.out.println("BEEN PICKED.... AWAY: " + away.getText().toString());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void RegisterPickDB(int which) throws ExecutionException, InterruptedException // registers a pick in the database
    { // 0 is home 1 is away 2 is draw

        LocalDate date = LocalDate.now(); // Gets the current date




        System.out.println("----");
        System.out.println("cnt: " + cnt);


        System.out.println("cnt now : " + cnt);
        DatabaseReference reference = database.getReference().child("users").child(user).child("points").child("ongoing").child("pick " + "15");
        System.out.println(reference.getParent());
        System.out.println(reference.get());
        System.out.println(reference.getKey());
        System.out.println(reference.getPath());


        // if (reference != null)
        System.out.println("reference: " + reference);

        System.out.println("----");
        if (which == 0) {
            counterOfPicks++;
            database.getReference().child("users").child(user).child("picks").child("ongoing").child("pick " + cnt).child("details").setValue(home.getText());
            database.getReference().child("users").child(user).child("points").child("overall").setValue(counterOfPicks);
        } else if (which == 1) {
            counterOfPicks++;

            database.getReference().child("users").child(user).child("picks").child("ongoing").child("pick " + cnt).child("details").setValue(away.getText());
            database.getReference().child("users").child(user).child("points").child("overall").setValue(counterOfPicks);

        } else {
            counterOfPicks++;
            database.getReference().child("users").child(user).child("picks").child("ongoing").child("pick " + cnt).child("details").setValue(home.getText() + " " + away.getText() + " draw");


            database.getReference().child("users").child(user).child("points").child("overall").setValue(counterOfPicks);


        }
        database.getReference().child("users").child(user).child("picks").child("ongoing").child("pick " + cnt).child("Date").setValue(String.valueOf( date));

    }



    public void countPicks(int which) {
        DatabaseReference ref = database.getReference().child("users").child(user).child("points").child("overall");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            int counter = 1;

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 counter = (int) (long) snapshot.getValue();



                cnt = counter + 1;
                try {
                    RegisterPickDB(which); // this is here so it only starts *after* cnt has been set to counter +1
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }



        private class BroadCastTime extends BroadcastReceiver {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (!(intent.hasExtra("newStatus"))) { // means update is from updateservice to update the countdown
                    status.setText(intent.getStringExtra("time"));
                } else { // means update is from gamelist to update min of current game
                    if (!(intent.getStringExtra("newStatus").equals("hasnt started"))) // means game has started
                    {
                        System.out.println(intent.getStringExtra("newStatus"));
                        status.setText(intent.getStringExtra("newStatus"));

                    }
                    goals.setText(intent.getStringExtra("newGoals"));

                    if (!(goals.getText().toString().equals(":"))) {

                        resultGuess.setText("");
                        resultGuess.setClickable(false);
                        resultGuess.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }


    @Override
    protected void onPause() {
        stopService(new Intent(showgame.this, UpdateService.class));
        super.onPause();
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left , R.anim.slide_out_right);
    }



}
class Pick
{
    String home;
    String away;
    String pick;
    public Pick(String h , String a  ,String p)
    {
        home = h;
        away = a;
        pick = p;
    }
}