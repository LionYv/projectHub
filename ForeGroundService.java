package com.example.theproject;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

// TODO: FOR SOME REASON WHEN THERE ARE A FEW FINISHED GAMES AT ONCE - SERVICE ONLY PUTS ONE IN THE CORRECT PIX AND ONLY ONE IN NOTIFICATION
public class ForeGroundService extends Service  implements Serializable {
    Match [] allgames;
    String user;
    int cntCorrectPix = 0;
    boolean nogames = false;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // System.out.println("adwad: " + intent.hasExtra("allgames"));
        //removeYesterdayPicks((Match[]) intent.getExtras().get("allgames"), intent.getStringExtra("username"));

        if (intent != null) {

            allgames = (Match[]) intent.getExtras().get("allgames");
            user = intent.getStringExtra("username");
            //CountPix("aa" , "vb" , "1" , "2" , String.valueOf(LocalDate.now()), "draw" , true);

            if (allgames != null &&  allgames[0] == null) {
                nogames = true;
            }
        }



       // CheckThePicks(allgames , intent.getStringExtra("username"));
        /*
        try {
            allgames =    OfflineUpdateOfGamelist(allgames);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

         */
        if (!nogames) {
            final Handler handler = new Handler();

            final Runnable runnable = new Runnable() {
                public void run() {
                    // need to do tasks on the UI thread
                    AsyncTask asyncTask = new AsyncTask() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected Object doInBackground(Object[] objects) {

                            System.out.println("aaaaad");

                            try {
                                if (allgames != null)

                                return UpdateMin(allgames);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return "error";
                            }
                            System.out.println("ALL GAMES: " + allgames);
                            return null;

                        }

                        @Override
                        protected void onPostExecute(Object o) {
                            Match[] matches = (Match[]) o;

                            if (matches!= null && matches[0] != null) {
                                Log.e("FOREGROUND", "Foreground allgames After update: " + (Arrays.toString(matches)));
                                CheckThePicks((Match[]) o, intent.getStringExtra("username"));
                            }

                            super.onPostExecute(o);
                        }
                    };

                    asyncTask.execute();
                    System.out.println("ForeGround service running");
                    handler.postDelayed(this, 20 * 1000); // EVERY 15 MIN


                }
            };

// trigger first time
            handler.postDelayed(runnable, 20 * 1000);
            String ChannelID = "ForeGround service";
            NotificationChannel channel = new NotificationChannel(
                    ChannelID, ChannelID, NotificationManager.IMPORTANCE_LOW

            );
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(this, ChannelID)
                    .setContentText("running service")
                    .setContentTitle("ABCBC")
                    .setSmallIcon(R.drawable.ic_launcher_background);

            startForeground(1000, notification.build());
        }


        return super.onStartCommand(intent, flags, startId);
    }

    


    public Match[] UpdateMin(Match [] allgames) throws IOException  {
            if (allgames == null){return null;}
        for (int i = 0; i < allgames.length; i++) {
            if (allgames[i] != null )
            {
                if (!(allgames[i].getStatus().equals("") || allgames[i].getStatus().equals("FT") || allgames[i].getStatus().equals("hasnt started"))) {
                    //Log.d("BEFORE UPDATE MIN: ", allgames[i].toString());
                }
                if (!(allgames[i].getStatus().equals("FT")))
                {
                    allgames[i] = allgames[i].upMin();

                }
            }


        }
        //Log.e(TAG , "}" );
        return allgames;
    }

    public void CheckThePicks(Match [] allgames,String user)
    {
        Match [] addition = new Match[allgames.length + 3];
        for (int i = 0; i < allgames.length; i++) {
            addition[i] = allgames[i];
        }
         new Match("TEST LEAGUE" , "team1" , "team2" , "1" , "0" , "" , "" , "" , "FT" , null  , null );
        addition[addition.length-3] = new Match("TEST LEAGUE" , "yoav" , "world" , "1" , "0" , "" , "" , "" , "FT" , null  , null );;
        addition[addition.length-2] = new Match("TEST LEAGUE" , "team1" , "team2" , "1" , "0" , "" , "" , "" , "FT" , null  , null );;
        addition[addition.length-1] = new Match("TEST LEAGUE" , "super strika" , "james" , "1" , "0" , "" , "" , "" , "FT" , null  , null );;

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference ref = database.getReference("users").child((user)).child("picks").child("ongoing");
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if (!snapshot.exists())
                {
                    System.out.println("no picks:L DESTROYING SERVICE ");
                    ForeGroundService.this.stopSelf(); // this works

                    ForeGroundService.this.onDestroy(); // this doesnt
                }
                for (DataSnapshot ds : snapshot.getChildren()) {



                    System.out.println(ds.getValue().toString());
                    if ((isInside(addition, ds.getValue().toString()))) {
                        String date = (String) ds.child("Date").getValue();
                        System.out.println("value is inside: " + ds.getValue().toString());
                        Object [] objects = Findgame(addition , ds.getValue().toString());
                        String team = (String) objects[0];
                        int pos = (int) objects[1];
                        if (addition[pos].getStatus().equals("FT"))
                        {
                            if (ds.getValue().toString().contains("draw")) {
                                checkThePick("draw", team, pos, addition , date);
                                System.out.println("removing value now..." + ds.getValue().toString());
                                ds.getRef().removeValue();
                            }
                            else
                                {
                                    checkThePick("win" , team , pos , addition , date);
                                    System.out.println("removing value now..." + ds.getValue().toString());
                                    ds.getRef().removeValue();
                                }

                        }
                        else{
                            System.out.println("GAME DIDNT FINISH: " + addition[pos] + " GAME DIDNT FINISH");
                        }
                        //ds.getRef().removeValue();

                    }
                    else
                        {
                            System.out.println("value isnt inside : " + ds.getValue().toString());
                            System.out.println("removing value now..." + ds.getValue().toString());
                            ds.getRef().removeValue();


                        }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {


            }
        });



    }
    public Object[] Findgame(Match [] allgames , String s)
    {
        Object[] objects = new Object[2];
        for (int i = 0; i < allgames.length; i++) {
            if (allgames[i] != null) {
                if (s.contains(allgames[i].getHomeTeam())) {
                    objects[0] = allgames[i].getHomeTeam().toString();
                    objects[1] = i;
                }
                if (s.contains(allgames[i].getAwayTeam())) {
                    objects[0] = allgames[i].getAwayTeam().toString();
                    objects[1] = i;
                }
            }
        }
        return objects;
    }
    public void CountPix(String homeT , String awayT , String goalHome , String goalAway, String date , String guess , boolean iscorrect)
    {
       DatabaseReference reference =   database.getReference("users").child((user)).child("picks").child("correctPix");
       reference.addListenerForSingleValueEvent(new ValueEventListener() {
           int counter = 0;

           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               for (DataSnapshot children : snapshot.getChildren())
               {
                   System.out.println(children.getValue());
                   counter++;
              }
               cntCorrectPix =(int)(long) counter + 1;
               System.out.println("cntCorrPix : " + cntCorrectPix);

               addPickDetails(homeT , awayT , goalHome , goalAway ,date ,  guess , iscorrect, cntCorrectPix);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

    }

    public void checkThePick(String pick , String team , int position , Match[] allgames , String date) { // checks if a pick is correct
        Random ran = new Random();

        if (pick.equals("draw")) {
            if (allgames[position].getHomeGoals().equals(allgames[position].getAwayGoals())) {
                Log.e("PICK ALERT: " , "CORRECT PICK : DRAW" + allgames[position].toString());
                addPoint();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("DELAYED: " + System.currentTimeMillis());
                        CountPix(allgames[position].getHomeTeam() , allgames[position].getAwayTeam() , allgames[position].getHomeGoals() , allgames[position].getAwayGoals() , date , "1" , true);
                    }
                },ran.nextInt(2000) + 500 );
            }
            else {
                CountPix(allgames[position].getHomeTeam() , allgames[position].getAwayTeam() , allgames[position].getHomeGoals() , allgames[position].getAwayGoals() ,date, pick , false);

                Log.e("PICK ALERT: " , "FALSE PICK : DRAW" + allgames[position].toString());
            }

        } else // not a draw -- so the team he picked is the one he thinks will win
        {
            int homegoals = Integer.parseInt(allgames[position].getHomeGoals());
            int awaygoals = Integer.parseInt(allgames[position].getAwayGoals());

            if (allgames[position].getHomeTeam().equals(team)) // picked home team to win
            {
                System.out.println("the team that was passed: " + team);
                System.out.println("home team was chosen : " + allgames[position].getHomeTeam());
                if (homegoals > awaygoals) {
                    addPoint();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("DELAYED: " + System.currentTimeMillis());
                            CountPix(allgames[position].getHomeTeam() , allgames[position].getAwayTeam() , allgames[position].getHomeGoals() , allgames[position].getAwayGoals() , date , "1" , true);
                        }
                    },ran.nextInt(2000) + 500);
                    Log.e("PICK ALERT: " , "CORRECT PICK : HOME" + allgames[position].toString());

                } else {
                    CountPix(allgames[position].getHomeTeam() , allgames[position].getAwayTeam() , allgames[position].getHomeGoals() , allgames[position].getAwayGoals(), date , "1" , false);

                    Log.e("PICK ALERT: " , "FALSE PICK: HOME" + allgames[position].toString());
                }
            } else // picked away team to win
            {
                System.out.println("team :: " + allgames[position].getAwayTeam());
                if (awaygoals > homegoals) {

                    addPoint();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("DELAYED: " + System.currentTimeMillis());
                            CountPix(allgames[position].getHomeTeam() , allgames[position].getAwayTeam() , allgames[position].getHomeGoals() , allgames[position].getAwayGoals() , date , "1" , true);
                        }
                    },ran.nextInt(2000) + 500);
                    Log.e("PICK ALERT: " , "CORRECT PICK : AWAY" + allgames[position].toString());
                } else {
                    CountPix(allgames[position].getHomeTeam() , allgames[position].getAwayTeam() , allgames[position].getHomeGoals() , allgames[position].getAwayGoals() ,date, "2" , false);

                    Log.e("PICK ALERT: " , "FALSE PICK: AWAY" + allgames[position].toString());
                }
            }
        }
    }

    public void addPickDetails(String homeT , String awayT , String homegoal , String awaygoal , String guess , String date , boolean iscorrect, int count)
    {
        PickHelper pickHelper = new PickHelper(homeT , awayT , homegoal , awaygoal , guess ,  date, iscorrect);
        database.getReference("users").child((user)).child("picks").child("correctPix").child("pick " + count).setValue(pickHelper);

        NotifyUserPick(pickHelper , count);

    }
    public void NotifyUserPick(PickHelper pickHelper , int UniqueID)
    {
        System.out.println("UNIQUE ID OF NOTIFICATIONNNNl: " + UniqueID);
        Intent intent=new Intent(getApplicationContext(),picks.class);
        intent.putExtra("username" ,user);
        intent.putExtra("cameFromNotif" , "true");
        String CHANNEL_ID=pickHelper.hometeam + " " +pickHelper.awayteam;
        NotificationChannel notificationChannel= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID,"name", NotificationManager.IMPORTANCE_LOW);
        }
        PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(),0,intent,0);
        Notification notification= null;
        Notification.Action action = new Notification.Action(android.R.drawable.sym_action_chat, "have a look!", pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(getApplicationContext(),CHANNEL_ID)
                    .setContentText("a game has ended")
                    .setContentTitle(CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .addAction(action)
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)
                    .build();
        }

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(UniqueID,notification);    }


    public void addPoint()
    {


        DatabaseReference ref = database.getReference("users").child((user)).child("points").child("correct");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                     String pnt =  snapshot.getValue().toString();
                     int points = Integer.parseInt(pnt);
                     points++;
                     snapshot.getRef().setValue(points);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean isInside(Match[] allgames , String valueOfDS)
    {
        for (int i = 0; i <allgames.length ; i++) {
            if (allgames[i] != null)
            {
                String home = allgames[i].getHomeTeam();
                String away = allgames[i].getAwayTeam();
                if (valueOfDS.contains(home) || valueOfDS.contains(away))
                {
                    return true;
                }


            }
        }
        return false;
    }

    public void removeYesterdayPicks(Match[] allgames , String user) // this Removes all picks who arent showing up in today games
    {

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference ref = database.getReference("users").child((user)).child("picks");
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot ds : snapshot.getChildren()) {


                    System.out.println(ds.getValue().toString());
                    if (!(isInside(allgames, ds.getValue().toString()))) {
                        System.out.println("value eradicated: " + ds.getValue().toString());
                        ds.getRef().removeValue();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {


            }
        });

    }
    public void checkIfPickCorrect(String user) /// !!!! only if game finished
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference ref = database.getReference("users").child((user)).child("picks");
        ref.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot ds : snapshot.getChildren())
                {
                   String pick =      ds.getValue().toString();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {


            }
        });
    }

    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();
    }
}








