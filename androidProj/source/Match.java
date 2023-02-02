package com.example.theproject;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.io.Serializable; // serializable somehow makes it possible to intent.putExtra(Match [] ) and not get an Exception
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;

import static android.content.ContentValues.TAG;
import static org.jsoup.Jsoup.connect;

public class Match implements Serializable { // home away goals min timeOfStart status agg
    private String league;
    private String HomeTeam;
    private String AwayTeam;
    private String HomeGoals;
    private String AwayGoals;
    private String TimeOfStart;
    private String countdown;
    private String min;
    private String status;

    public transient Bitmap imgHome;
    public transient Bitmap imgAway;

    public Bitmap getImgHome() {
        return imgHome;
    }

    public void setImgHome(Bitmap imgHome) {
        this.imgHome = imgHome;
    }

    public Bitmap getImgAway() {
        return imgAway;
    }

    public void setImgAway(Bitmap imgAway) {
        this.imgAway = imgAway;
    }

    public Match(String league, String homeTeam, String awayTeam, String homeGoals, String awayGoals, String timeOfStart, String countdown, String min, String status, Bitmap imgHome , Bitmap imgAway) {
        this.league = league;
        HomeTeam = homeTeam;
        AwayTeam = awayTeam;
        HomeGoals = homeGoals;
        AwayGoals = awayGoals;
        TimeOfStart = timeOfStart;
        this.countdown = countdown;
        this.status = status;
        this.min = min;
        this.imgHome = imgHome;
        this.imgAway = imgAway;
    }
    public Match(Match m)
    {
        this.league = "";
        this.HomeTeam = m.getHomeTeam();
        this.AwayTeam = m.getAwayTeam();
        this.HomeGoals = m.getHomeGoals();
        this.AwayGoals = m.getAwayGoals();
        this.TimeOfStart = m.getTimeOfStart();
        this.countdown = m.getCountdown();
        this.status = m.getStatus();
        this.min = m.getMin();
        this.imgHome = m.getImgHome();
        this.imgAway = m.getImgAway();


    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getHomeTeam() {
        return HomeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        HomeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return AwayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        AwayTeam = awayTeam;
    }

    public String getHomeGoals() {
        return HomeGoals;
    }

    public void setHomeGoals(String homeGoals) {
        HomeGoals = homeGoals;
    }

    public String getAwayGoals() {
        return AwayGoals;
    }

    public void setAwayGoals(String awayGoals) {
        AwayGoals = awayGoals;
    }

    public String getTimeOfStart() {
        return TimeOfStart;
    }

    public void setTimeOfStart(String timeOfStart) {
        TimeOfStart = timeOfStart;
    }

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




    public String saveMin;

    public Match updateMian() throws IOException {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {

                String urla = "https://www.bbc.com/sport/football/scores-fixtures";
                Document Main = null;
                try {
                    Main = connect(urla).timeout(60*1000).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Element game = Main.selectFirst("div.qa-match-block:contains" + "(" + Match.this.HomeTeam + ")").selectFirst("li:contains" + "(" + Match.this.HomeTeam + ")");
                String HomeGoals = game.select("span.sp-c-fixture__number.sp-c-fixture__number--home").text();
                String AwayGoals = game.select("span.sp-c-fixture__number.sp-c-fixture__number--away").text();


                String minOfGame = game.select("span.sp-c-fixture__status:contains(mins)").text();
                String status = game.select("span.sp-c-fixture__status").text();
                if (!(Match.this.getMin().contains("+") || Match.this.getMin().contains("HT"))) {
                    // only if we didnt set this match mins to HT OR 45' + 3  (lines 153 ,172 )then we know that game is in play and min isnt empty
                    Match.this.setMin(minOfGame);
                }
                //Match.this.setStatus(status); //()()((()(()()


                // TODO : make games change score live (currently working on 45 mins converting to half time and 45' + 3) and HT converting to 46 min
                // !! games are created with either hasnt started status or with FT or with x mins as their status

                if ((game != null)) {
                    //System.out.println("-0------0-");
                    Log.e(TAG, "((((((((((");
                    Log.e(TAG, getHomeTeam() + "vs" + getAwayTeam() + "minute of the game: " + getMin() + "   status in game : " + getStatus());
                    Log.e(TAG, " -------- ----------- ------------- -------- min: " + minOfGame + " --- status: " + status);
                    Log.e(TAG, "))))))))))");
                    // Log.d(TAG , "line 123 in Matc : " + minOfGame);
                    // System.out.println("match.this : : :: " + Match.this.toString());
                    if (Match.this.getMin().equals("")) {
                        // System.out.println(" get min equals to nothing");
                        if (Match.this.getStatus().equals("hasnt started")) {

                            if (minOfGame.contains("min")) {
                                Log.e("STARTING FROM 0", minOfGame + getTimeOfStart());
                                Match.this.setMin(minOfGame);
                                Match.this.setStatus(status);
                            } else {
                                System.out.println("gaem hasnt started");
                                ;
                                Match.this.setMin(Match.this.getTimeOfStart());
                            }
                        } else {
                            // Match.this.setStatus(status);
                            //status = Match.this.getStatus();
                            if (status.equals("FT")) {
                                //System.out.println("game is full time");
                                Match.this.setMin("Full Time");

                            } else if (status.equals("HT")) {
                                Log.v("NOTICE", "game is half time");
                                //System.out.println("game is half time");

                                Match.this.setMin(status);

                            } else if (status.contains("min")) // AAA:  WAS: status.equals in play
                            {
                                //System.out.println("game is in play so setting the min");
                                Match.this.setMin(status); // here i changes to status
                            } else if (status.contains("extra time")) // i THINK THIS ONE IS USELESS
                            {
                                //System.out.println("game is in extra time ");
                                Match.this.setMin(getMin()); // maybe? here changed to status aswell
                                Match.this.setStatus(getMin()); //  AAA: THIS WASNT HERE
                            } else if (status.contains("+")) {
                                Log.v("NOTICE", "game is +");
                                Match.this.setMin(status);
                            } else {
                                //System.out.println("status: " + Match.this.getStatus() );
                                if (Match.this.getMin().equals("FT")) {
                                    //System.out.println("is full time");
                                    Match.this.setMin("Full time");
                                } else if (Match.this.getMin().equals("HT")) {
                                    //System.out.println("is half time");
                                    Match.this.setMin("Half time");
                                } else {
                                    Log.e("NOTICE", "else of else line 191: " + getHomeTeam() + ": status: " + getStatus() + " min: " + getMin());
                                    //System.out.println(Match.this.toString());
                                    Match.this.setMin(Match.this.getStatus());

                                }
                            }

                        }

                    } else {
                        System.out.println("all goood: " + getHomeTeam() + getAwayTeam() + getStatus() + minOfGame);
                        Log.e("MIN of game", minOfGame);
                        // this sets to HT not to "half time" ---- in Matches class it sets it back to half time after one update
                        Match.this.setMin(minOfGame);
                        Match.this.setStatus(minOfGame);
                    }
                    //System.out.println("-0------0-");

                    //System.out.println("match.after update : : :: " + Match.this.toString());

                    Match.this.setHomeGoals(HomeGoals);
                    Match.this.setAwayGoals(AwayGoals);
                } else {
                    saveMin = "5";
                }
            }

        });
        t1.start();

        return Match.this;
    }

    public void updateCountdown() // makes countdown using the method in Matches class --
    {
        countdown = Matches.GenerateTimes(this.TimeOfStart);
    }



    public void setImg(String imgURL) {

    }

    @Override
    public String toString() {
        if (status.equals("hasnt started")) {
            return "Match{" +
                    " league= '" + league + '\'' +
                    " , HomeTeam= '" + HomeTeam + '\'' +
                    " , AwayTeam= '" + AwayTeam + '\'' +
                    " , TimeOfStart= '" + TimeOfStart + '\'' +
                    " , countdown= '" + countdown + '\'' +
                    "  , min= '" + min + '\'' +
                    " , status= '" + status + '\'' +
                    '}' + "bitmaps : " + imgHome +" ********* " + imgAway;

        }
        if (status.equals("FT")) {
            return "Match(ENDED){" +
                    " league= '" + league + '\'' +
                    " , HomeTeam= '" + HomeTeam + '\'' +
                    " , AwayTeam= '" + AwayTeam + '\'' +
                    " , HomeGoals= '" + HomeGoals + '\'' +

                    " , AwayGoals= '" + AwayGoals + '\'' +

                    " , status= '" + status + '\'' +
                    '}';
        }
        if (status.equals("Match postponed - Other")) {
            return "Match(postponed){" +
                    " league= '" + league + '\'' +
                    " ,HomeTeam= '" + HomeTeam + '\'' +
                    ", AwayTeam= '" + AwayTeam + '\'' +
                    ", status= '" + status + '\'' +
                    '}';
        }

        return "Match(started){" +
                " league= '" + league + '\'' +
                " , HomeTeam= '" + HomeTeam + '\'' +
                " , AwayTeam= '" + AwayTeam + '\'' +
                " , HomeGoals= '" + HomeGoals + '\'' +
                " , AwayGoals= '" + AwayGoals + '\'' +

                " , min= '" + min + '\'' +
                " , status= '" + status + '\'' +
                '}';
    }

    public Match upMin() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String urla = "https://www.bbc.com/sport/football/scores-fixtures";
                Document Main = null;
                try {
                    Main = connect(urla).timeout(60*1000).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Element game = Main.selectFirst("div.qa-match-block:contains" + "(" + Match.this.HomeTeam + ")").selectFirst("li:contains" + "(" + Match.this.HomeTeam + ")");
                //System.out.println("AFDAIWHIUA :" + game.text());
                if (!(game == null)) {
                    String HomeGoals = game.select("span.sp-c-fixture__number.sp-c-fixture__number--home").text();
                    String AwayGoals = game.select("span.sp-c-fixture__number.sp-c-fixture__number--away").text();


                    String minOfGame = game.select("span.sp-c-fixture__status:contains(mins)").text();
                    String status = game.select("span.sp-c-fixture__status").text();
                    // set status(status)
                    setHomeGoals(HomeGoals);
                    setAwayGoals(AwayGoals);
                    //Match m = new Match(league , HomeTeam , AwayTeam , HomeGoals , AwayGoals , TimeOfStart , countdown , minOfGame , status , null , null);
                    //Log.e(TAG, "....{");
                    //System.out.println("  MATCH  RECIEVED FROM WEB: (minof game): " + minOfGame + " status: " + status);

                    //System.out.println();
                    if (getStatus().equals("hasnt started")) {

                        if (!(status.equals(""))) {
                            System.out.println("aaaaaaaadwadawd");
                            setMin(minOfGame);
                            setStatus(status);
                        }

                    }
                    if (getStatus().equals("FT")) {
                        setMin("full time");
                    }
                    if (getStatus().equals("HT")) {
                        setMin("half time");
                        if (status != null) {
                            setMin(minOfGame);
                            setStatus(status);
                        }
                    }

                    if (status.contains("min")) {
                        setMin(status);
                        setStatus(status);
                    }
                    if (status.contains("FT")) {
                        setStatus("FT");
                        setMin("full time");
                    }
                    if (status.contains("+")) {
                        setStatus(status);
                        setMin(status);
                    }
                    if (status.contains("HT")) {
                        setStatus(status);
                        setMin(status);
                    }

                    //Log.d("A","THE MATCH THAT WAS BUILT AND UPDATED: " +   Match.this.toString());
                    //Log.e(TAG, "}....");

                }
            }
        }) ;

        t1.start();
        return this;
    }
}


