package com.example.theproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Explode;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theproject.databinding.ActivityGamelistBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.jsoup.nodes.Document;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.sql.SQLOutput;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;
import static org.jsoup.Jsoup.connect;
        ///TODO: I NEED A SERVICE THAT IDENTIFIES WHENEVER A GAME IS FINISHED AND THEN CHECKS IN FIREBASE IF THE USER HAS A PICK FOR THAT GAME , IF YES THEN NOTIFY HIM / ADD HIM POINTS ELSE DONT
public class gamelist extends AppCompatActivity implements AdapterView.OnItemClickListener , Serializable {
    //ActivityGamelistBinding binding;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    ArrayList<Match> matchListArray;
    ListAdapter listAdapter;
    ListView lv;
     Match[]  allgames , TodayMatches;
    ProgressDialog waitForGames;
    EditText filter;
    BottomNavigationView bottomNavigationView;
    LinearLayout mainlayout;
    boolean cameFromProfile = false;
    String UserName;
    int count = 0;
    String favTeam;
    static int counter = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamelist);
        overridePendingTransition(0, 0);

       


        UserName = getIntent().getStringExtra("username");
        favTeam = getIntent().getStringExtra("favorite");

        if (getIntent().hasExtra("gamelist") && getIntent().getExtras().get("gamelist") != null)
        {

            System.out.println("AMVOEAFE");
            cameFromProfile = true;
            matchListArray = (ArrayList<Match>) getIntent().getExtras().get("gamelist");
            System.out.println(Arrays.toString(matchListArray.toArray()));



            Bitmap [] home = null;
            Bitmap [] away = null;

            Gson gson = new Gson();
            home =  gson.fromJson(getIntent().getStringExtra("jsonhome") ,Bitmap[].class );
            away =  gson.fromJson(getIntent().getStringExtra("jsonaway") ,Bitmap[].class );

            for (int i = 0; i <home.length ; i++) {
                matchListArray.get(i).setImgHome( home[i]);
                matchListArray.get(i).setImgAway( away[i]);

            }
            // this order is very important

            TodayMatches = new Match[matchListArray.size()];

            for ( int i = 0 ; i < matchListArray.size(); i++)
            {
                TodayMatches[i] = matchListArray.get(i);
            }



            matchListArray = changeLeauge(matchListArray);
            // this order is very important

                    listAdapter = new ListAdapter(gamelist.this,0 , 0 ,matchListArray);
                    lv = findViewById(R.id.gameList);
                    lv.setOnItemClickListener(gamelist.this::onItemClick);
                    lv.setAdapter(listAdapter);




        }
        bottomNavigationView = (BottomNavigationView) this.findViewById(R.id.navbar);
        bottomNavigationView.setSelectedItemId(R.id.two);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bitmap [] home = new Bitmap[matchListArray.size()];
                Bitmap [] away = new Bitmap[matchListArray.size()];
                for (int i = 0; i <matchListArray.size() ; i++) {
                    home[i] =     matchListArray.get(i).getImgHome();
                    away[i] =     matchListArray.get(i).getImgAway();

                }
                Gson gson = new Gson();
                String jsonhome =    gson.toJson(home);
                String jsonaway =    gson.toJson(away);


                if (item.getItemId() == R.id.one)
                {
                    System.out.println(jsonhome);
                    System.out.println(jsonaway);
                    System.out.println(TodayMatches.toString());



                    Intent intent = new Intent(gamelist.this , profile.class);
                    intent.putExtra("username" , UserName);
                    intent.putExtra("jsonhome" , jsonhome);
                    intent.putExtra("jsonaway" , jsonaway);

                    intent.putExtra("gamelist" , TodayMatches);
                    startActivity(intent);
                    return true;

                }
                if (item.getItemId() == R.id.two)
                {
                    item.setChecked(true);
                }
                if (item.getItemId() == R.id.three)
                {
                    Intent intent = new Intent(gamelist.this , picks.class);
                    intent.putExtra("username" , UserName);
                    intent.putExtra("jsonhome" , jsonhome);
                    intent.putExtra("jsonaway" , jsonaway);
                    ArrayList matchArr = new ArrayList<>();
                    for (int i = 0; i < TodayMatches.length; i++) {
                        if (TodayMatches[i] != null)
                        matchArr.add(TodayMatches[i]);
                    }
                    intent.putExtra("gamelist" , matchArr);
                    startActivity(intent);
                }
                return false;
            }

        });

        registerReceiver(networkChangeListener , intentFilter);

        AsyncTask loadGames = new AsyncTask()
        {
            @Override
            protected void onPreExecute() {
                waitForGames = new ProgressDialog(gamelist.this);
                waitForGames.setCancelable(false);
                waitForGames.setMessage("Loading matches....");
                waitForGames.setTitle("please wait..");
                waitForGames.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                waitForGames.show();

                super.onPreExecute();
            }

            @Override
            protected Object doInBackground(Object[] objects) {
                if (favTeam == null)
                {
                    favTeam = getIntent().getStringExtra("favorite");
                }
                TodayMatches = GetGames(favTeam);
               
                Intent intent = new Intent("destroyService");
                sendBroadcast(intent);
                try {
                    matchListArray = MakeList (TodayMatches);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected void onPostExecute(Object o) {
                int cnt = 0;
                for (int i = 0; i < TodayMatches.length; i++) {
                    if (TodayMatches[i] != null)
                    {
                        cnt++;
                    }
                }
                Match [] TodayMatchesNoNull = new Match[cnt];

                cnt = 0;
                for (int i = 0; i <TodayMatches.length ; i++) {
                    if (TodayMatches[i]!= null)
                    {
                        TodayMatchesNoNull[cnt] = TodayMatches[i];
                        cnt++;
                    }
                }

                mainlayout = findViewById(R.id.mainLayout);
                mainlayout.setBackgroundResource(0);
                if (matchListArray.size() == 0)
                {

                    mainlayout.setBackground(getDrawable(R.drawable.ic_baseline_block_24));
                }

                listAdapter = new ListAdapter(gamelist.this,0 , 0 , matchListArray);
                lv = findViewById(R.id.gameList);
                lv.setOnItemClickListener(gamelist.this::onItemClick);
                lv.setAdapter(listAdapter);
                waitForGames.cancel();
                if (!isForegroundServiceRunning()) {
                    if (matchListArray.size() != 0) {
                        Intent foregroundIntent = new Intent(gamelist.this, ForeGroundService.class);
                        foregroundIntent.putExtra("username", UserName);
                        foregroundIntent.putExtra("allgames", TodayMatchesNoNull);
                        startForegroundService(foregroundIntent);
                    }
                    
                }
                super.onPostExecute(o);
            }


        };
        if (!cameFromProfile)
        {

            loadGames.execute();
        }

        final Handler handler = new Handler();

        final Runnable runnable = new Runnable() {
            public void run() {
                if (matchListArray.size() !=0) {
                   
                    try {
                       
                        if (allgames != null) {
                            UpdateMin();
                            listAdapter.notifyDataSetChanged();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                handler.postDelayed(this, 1000 * 30);


            }
        };

		// trigger first time
        handler.postDelayed(runnable , 1000*35);



}





            public Bitmap makeFromLink(String name)
            {

                URL url = null;

                Bitmap bmp = null;
                try {
                    url = new URL(name);


                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return bmp;
            }

            @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.filter_menu , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if (item.getItemId() == R.id.live) {
            ArrayList<Match> tmp = new ArrayList<>();
            tmp = onlyLive(TodayMatches); //onlyLive((GetGames()));
            if (tmp.isEmpty())
            {
                Toast.makeText(this , "there are no live games currently" , Toast.LENGTH_SHORT).show();
            }
            else {
                listAdapter = new ListAdapter(this, 0, 0, tmp);
                listAdapter.notifyDataSetChanged();
                lv.setAdapter(listAdapter);
                Log.w("AAAAAA", Arrays.toString(tmp.toArray()));
            }
        }
        else if(item.getItemId() == R.id.finished)
        {
            ArrayList<Match> tmp = new ArrayList<>();
            tmp = onlyFinished(TodayMatches); //onlyLive((GetGames()));
            if (tmp.isEmpty())
            {
                Toast.makeText(this , "there are no finished games currently" , Toast.LENGTH_SHORT).show();
            }
            else {
                ListAdapter temp = new ListAdapter(this , 0 , 0 ,tmp);
                

                lv.setAdapter(temp);
                Log.w("AAAAAA", Arrays.toString(tmp.toArray()));
            }

        }

        else
            {

                listAdapter = new ListAdapter(this, 0, 0, matchListArray);
                lv.setAdapter(listAdapter);

            }


        return true;
    }
            public boolean isForegroundServiceRunning()
            {
                ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

                for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE))
                {
                    if (ForeGroundService.class.getName().equals(service.service.getClassName())){return true;}
                }
                return false;
            }

            public void setBitmap(int position)
            {
                String Home =   matchListArray.get(position).getHomeTeam();

                 AsyncTask asyncTask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {
                        DatabaseReference ref = database.getReference("teams");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for ( DataSnapshot leagues : snapshot.getChildren())
                                {
                                    for (DataSnapshot teams  : leagues.getChildren())
                                    {
                                        if (teams.getKey().equals(Home))
                                        {
                                            matchListArray.get(position).setImgHome(makeFromLink(teams.getValue().toString()));
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        return false;
                    }
                };

                asyncTask.execute();
            }

    public ArrayList<Match> MakeList(Match[] TodayMatches) throws IOException {
        ArrayList<Match> matchList = new ArrayList<>();
        String league = "";
       
        for (int i = 0; i < TodayMatches.length; i++) {

            if (TodayMatches[i] != null) { // this is used to identify whether the league is one that hasnt been displayed yet(makes it so every league name only appears once at the list view)
                if (i == 0) {
                    TodayMatches[i].upMin(); // was : updateMin
                    league = TodayMatches[i].getLeague();
                    matchList.add(TodayMatches[i]);

                }
                else {
                    if (TodayMatches[i].getLeague().equals(league)) {
                        //TodayMatches[i].setLeague("");
                        Match tmp  = new Match(TodayMatches[i]);
                        System.out.println(TodayMatches[i]);
                        System.out.println("tmp: " + tmp);

                        matchList.add(tmp);

                    } else {
                        league = TodayMatches[i].getLeague();
                        matchList.add(TodayMatches[i]);


                    }
                }
            }
        }
        //Match[] allgames = (Match[]) getIntent().getExtras().get("games");

        System.out.println(matchList.toArray().toString());
        return matchList;
    }

    public ArrayList<Match> onlyLive(Match [ ] TodayMatches) // returns ArrayList of only live matches
    {
        ArrayList<Match> onlyLive = new ArrayList<>();


        for (int i = 0; i < TodayMatches.length; i++) {
            if (TodayMatches[i] != null )
            {
                if (TodayMatches[i].getStatus().equals("HT") || TodayMatches[i].getStatus().contains("play") || TodayMatches[i].getStatus().contains("min")) {
                    Log.w("yoav CHECK", TodayMatches[i].getMin() + "a " + TodayMatches[i].getStatus() + " a " + TodayMatches[i].getHomeTeam());
                    onlyLive.add(TodayMatches[i]);
                }
            }
        }
        return onlyLive;
    }
    public ArrayList<Match> onlyFinished(Match [ ] TodayMatches) // returns ArrayList of only live matches
    {
        ArrayList<Match> onlyFinished = new ArrayList<>();
        for (int i = 0; i < TodayMatches.length; i++) {
            if (TodayMatches[i] != null )
            {
                if (TodayMatches[i].getStatus().equals("FT")) {
                    Log.w("FINISHED GAME: ", TodayMatches[i].getMin() + "a " + TodayMatches[i].getStatus() + " a " + TodayMatches[i].getHomeTeam());
                    onlyFinished.add(TodayMatches[i]);
                }
            }
        }
        return onlyFinished;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    { 	// here is problem:::: when i click on the list after changing it the position is gonna be 1 but its gonna take one from all games and not only the finished one
        // i need something which isnt allgames[i]
        Match m = (Match) parent.getItemAtPosition(position);//!!
        String s = m.getHomeTeam();

        for (int i = 0; i <TodayMatches.length ; i++) {
            if (TodayMatches[i] != null && TodayMatches[i].getHomeTeam().equals(s))
            {
                m  = TodayMatches[i];
            }
        }




        int cnt = 0;

        Intent intent = new Intent(this, showgame.class);
        intent.putExtra("username" , getIntent().getStringExtra("username"));
        Bitmap [] bitmaps = new Bitmap[]{m.getImgHome() , m.getImgAway()};

        Gson gson = new Gson();
        String myJson = gson.toJson(m); // this is converting match class to json
        System.out.println("myJson : " + myJson);
        intent.putExtra("GameDetails", myJson);
        intent.putExtra("imagehome" ,convert(m.getImgHome()));
        intent.putExtra("imageaway" ,convert(m.getImgAway()));
        intent.putExtra("allgames" , TodayMatches);

        
        startActivityForResult(intent , 21);
        overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);
    }
    public byte[] convert(Bitmap bmp)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    public void UpdateCountdown()
    {
        for (int i = 0; i < allgames.length; i++) {
            if (allgames[i] != null && allgames[i].getCountdown() != "")
            {

                allgames[i].updateCountdown();
            }

        }
    }
    public void UpdateMin() throws IOException {
        
        ArrayList<Match> tmp = new ArrayList<Match>();

        for (int i = 0; i < allgames.length; i++) {
            if (allgames[i] != null )
            {
                if (!(allgames[i].getStatus().equals("") || allgames[i].getStatus().equals("FT") || allgames[i].getStatus().equals("hasnt started"))) {
                }
                if (allgames[i].getStatus().equals("FT"))
				{
                   
                }
                else {
                    allgames[i] = allgames[i].upMin();
                    Intent intent = new Intent(allgames[i].getHomeTeam());
                    intent.putExtra("newStatus", allgames[i].getStatus());
                    intent.putExtra("newGoals", allgames[i].getHomeGoals() + ":" + allgames[i].getAwayGoals());
                    sendBroadcast(intent);
                    if (!(allgames[i].getStatus().equals("") || allgames[i].getStatus().equals("FT") || allgames[i].getStatus().equals("hasnt started"))) {
                        //Log.d("AFTER UPDATE MIN: ", allgames[i].toString());

                        listAdapter.notifyDataSetChanged();
                    }
                }

                tmp.add(allgames[i]);
            }
            matchListArray.clear();
            matchListArray.addAll(changeLeauge(tmp));
            listAdapter.notifyDataSetChanged();


        }
        

        listAdapter.notifyDataSetChanged();
    }
        public ArrayList<Match> changeLeauge(ArrayList<Match> matches) {

            ArrayList<Match> tmp = new ArrayList<>();

            String league = "";
            for (int i = 0; i < matches.size(); i++) {


                if (i == 0) {
                    league = matches.get(i).getLeague();
                    tmp.add(matches.get(i));

                }
                else {
                    if (matches.get(i).getLeague().equals(league)) {
                        //TodayMatches[i].setLeague("");
                        Match abc = new Match(matches.get(i));

                        tmp.add(abc);

                    } else {
                        league = matches.get(i).getLeague();
                        tmp.add(matches.get(i));
                    }
                }
            }
            return tmp;
        }

    public void isIn(String team , int pos )
    {
        //System.out.println("team : " + team);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");

        DatabaseReference ref = database.getReference("users").child(getIntent().getStringExtra("username")).child("picks");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren())
                {
                    //System.out.println("_____" + ds.getValue().toString().toLowerCase() +"  " +  team);
                    if (team.contains(ds.getValue().toString()) || ds.getValue().toString().toLowerCase().contains(team))
                    {
                        if (ds.getValue().toString().contains("draw"))
                        {
                            checkThePick("draw" , team  , pos);

                            //counter++;

                        }
                        checkThePick("win" , team , pos);
                        ds.getRef().removeValue();

                        //counter++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public void checkThePick(String pick , String team , int position)
    {
        if (pick.equals("draw"))
        {
            if (allgames[position].getHomeGoals().equals(allgames[position].getAwayGoals())) {
                System.out.println(" CORRECT PICK : DRAW");
            }
            else{
                System.out.println("FALSE PICK of a draw");
            }

        }
        else // not a draw -- so the team he picked is the one he thinks will win
            {
                int homegoals = Integer.parseInt( allgames[position].getHomeGoals());
                int awaygoals = Integer.parseInt( allgames[position].getAwayGoals());

                if (allgames[position].getHomeTeam().equals(team)) // picked home team to win
                {
                    if (homegoals > awaygoals){
                        System.out.println("correct pick : :: " + team);
                    }
                    else{
                        System.out.println("false pick");
                    }
                }
                else // picked away team to win
                    {
                        System.out.println("team :: " + allgames[position].getAwayTeam());
                        if (awaygoals > homegoals){
                            System.out.println("coorect pick: " + team);
                        }else{
                            System.out.println("false pick");
                        }
                    }
            }
    }


            public Match[] GetGames(String favTeam)
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Matches matches = null;
                try {
                    matches = new Matches();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (favTeam == null)
                    {
                        System.out.println("getIntent : " + getIntent().getStringExtra("favorite"));

                        allgames = matches.running(getIntent().getStringExtra("favorite"));

                    }
                    else
                        {
                            allgames = matches.running(favTeam);

                        }

                } catch (IOException  e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        while (thread.isAlive()){} // this is to delay the actions that are supposed to happen after the thread is dead (should probs use synchronized)
        return allgames;
    }
 

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_out_left , R.anim.slide_in_right);
    }

            @Override
            protected void onStart() {
                super.onStart();

            }

            @Override
    protected void onStop() {

        //unregisterReceiver(networkChangeListener);

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println("req code : " + requestCode);
      
        super.onActivityResult(requestCode, resultCode, data);
    }
}