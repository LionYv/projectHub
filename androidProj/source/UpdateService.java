package com.example.theproject;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
public class UpdateService extends IntentService  {

    boolean isDestroy = false;


    public UpdateService() {

         super("updateservice");
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("SERVICE", "SERVICE WAS CALLED");


        String countdown = "";
        String time = intent.getStringExtra("timeOfStart");
        String status = intent.getStringExtra("status");

        if (!(time.isEmpty()) && !isDestroy ) { // isDestroy is true only if the ondestroy has been called(by request code on activity result)
            if (!(status.contains("min"))) {


                while (!(countdown.equals("-1"))) {
                    try {
                        if (isDestroy == true)
                        {
                            System.out.println("breakingggg");
                            break;
                        }
                        Thread.sleep(1000);
                        countdown = Matches.GenerateTimes(time);
                        sendBroadcast(countdown);
                        System.out.println("service running");



                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                sendBroadcast("game starting soon");
            }
        }
        onDestroy();
    }

        // TODO: SEND BROADCAST TO THE SHOWGAME CLASS EVERYTIME YOU UPDATE A GAME, IF THE SHOWGAME ACTIVITY IS CURRENTLY ON THAT IT WILL RECEIVE THE BROADCAST AND THAN UPDATE THE STATUS // SO I NEED TO REGISTER NEW RECEIVER IN SHOWGAME
        //  AND MAKE A SEND BROADCAST IN GAMELIST OR MAYBE EVEN IN MATCH
    @Override
    public void onDestroy() {
        isDestroy = true;

        System.out.println("on Destroy was called");
        stopSelf();

        super.onDestroy();
        //Toast.makeText(this,"destory service", Toast.LENGTH_LONG).show();

    }
    private void sendBroadcast(String s)
    {
        Intent intent = new Intent("timeIntent");
        intent.putExtra("time" , s);
        sendBroadcast(intent);

    }



}
