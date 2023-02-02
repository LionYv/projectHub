package com.example.theproject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetworkChangeListener extends BroadcastReceiver {
    AlertDialog.Builder builder;
    AlertDialog NetAlert;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!InternetHelper.isConnectedToInternet(context))
        {
              builder = new AlertDialog.Builder(context)
                    .setTitle("Oops!")

                      .setIcon(R.drawable.ic_baseline_wifi_off_24)
                      .setCancelable(false)
                    .setMessage("seems like there is no internet");
                NetAlert = builder.create();
                NetAlert.show();
        }
        else
            {
                if (NetAlert!= null)
                if (NetAlert.isShowing())
                {
                    NetAlert.dismiss();
                }
            }
    }
}
