package com.example.theproject;

public class PickHelper {


    public String hometeam;
    public String awayteam;
    public String homegoal;
    public String awaygoal;
    public String date;
    public String guess;
    public boolean iscorrect;



    @Override
    public String toString() {
        return "PickHelper{" +
                "hometeam='" + hometeam + '\'' +
                ", awayteam='" + awayteam + '\'' +
                ", homegoal='" + homegoal + '\'' +
                ", awaygoal='" + awaygoal + '\'' +
                ", guess='" + guess + '\'' +
                ", iscorrect=" + iscorrect +
                '}';
    }

    public PickHelper(String hometeam, String awayteam, String homegoal, String awaygoal, String date, String guess, boolean iscorrect) {
        this.hometeam = hometeam;
        this.awayteam = awayteam;
        this.homegoal = homegoal;
        this.awaygoal = awaygoal;
        this.date = date;
        this.guess = guess;
        this.iscorrect = iscorrect;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHometeam() {
        return hometeam;
    }

    public void setHometeam(String hometeam) {
        this.hometeam = hometeam;
    }

    public String getAwayteam() {
        return awayteam;
    }

    public void setAwayteam(String awayteam) {
        this.awayteam = awayteam;
    }

    public String getHomegoal() {
        return homegoal;
    }

    public void setHomegoal(String homegoal) {
        this.homegoal = homegoal;
    }

    public String getAwaygoal() {
        return awaygoal;
    }

    public void setAwaygoal(String awaygoal) {
        this.awaygoal = awaygoal;
    }

    public String getGuess() {
        return guess;
    }

    public void setGuess(String guess) {
        this.guess = guess;
    }

    public boolean isIscorrect() {
        return iscorrect;
    }

    public void setIscorrect(boolean iscorrect) {
        this.iscorrect = iscorrect;
    }
}
