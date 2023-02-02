package com.example.theproject;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.theproject.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btn;
    boolean flagRegister =false;
    TextInputEditText user , pass;
    TextInputLayout userLayout , passLayout;
    LinearLayout mainLayout;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");

    TextView register;
    Match [] TodayMatches;
    NetworkChangeListener networkChangeListener = new NetworkChangeListener();
    IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btnLogin);
        btn.setOnClickListener(this);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        userLayout = findViewById(R.id.usernameLayout);
        passLayout = findViewById(R.id.passwordLayout);
        mainLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable)  mainLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(3500);
        animationDrawable.start();
        user.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                    userLayout.setErrorEnabled(false);
                    userLayout.setBoxStrokeColor(Color.WHITE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }


        });
        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passLayout.setErrorEnabled(false);
                passLayout.setBoxStrokeColor(Color.WHITE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        registerReceiver(networkChangeListener , intentFilter);









    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onClick(View v)
    {
        if (v == btn)
        {
            if (!flagRegister) {
                if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    Toast.makeText(this, "please fill in the fields.. ", Toast.LENGTH_SHORT).show();
                } else {
                    DoesUserExist(user.getText().toString(), pass.getText().toString() , false);
                }
            }
            else
                {
                    DoesUserExist(user.getText().toString() ,pass.getText().toString() ,true);
                }

           }
           else
               {
                   if (!flagRegister) {
                       flagRegister = true;

                       user.setText("");
                       pass.setText("");
                       register.setText("have an account? Login instead");
                       btn.setText("Register!");
                   }
                   else
                       {
                           flagRegister = false;

                           user.setText("");
                           pass.setText("");
                           register.setText("dont have an account? click here to register");
                           btn.setText("login!");
                       }
               }

            //registerUser(user.getText().toString() , pass.getText().toString());
    }


    public void DoesUserExist(String userCheck , String password , boolean checkRegister)
    {
        FirebaseDatabase db =  FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference reference =     db.getReference().child("users").child(userCheck);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if (checkRegister)
                    {
                        userLayout.setError("account exists!");
                        NotifyUser("accExists");
                    }
                    else {
                        System.out.println("AAAAAAAA: " + snapshot.getValue());
                        if (snapshot.child("password").getValue().equals(password)) {


                            NotifyUser("good");
                        } else {
                            passLayout.setError("Password Incorrect!");

                            NotifyUser("passbad");
                        }
                    }
                }
                else
                    {
                        if (checkRegister)
                        {
                            Toast.makeText(MainActivity.this  , " registerd" , Toast.LENGTH_SHORT).show();
                            registerUser(userCheck , password);
                            openPicks();

                        }
                        else {
                            userLayout.setError("No such User");
                            NotifyUser("");
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void NotifyUser(String status)
    {
        if (status.equals("good"))
        {

            Toast.makeText(this,  "Successfully logged in" , Toast.LENGTH_SHORT).show();
            openGames();
            return;
        }
        else if(status.equals("passbad"))
        {
            makeSnackbar("Password incorrect..." );
        }
        else if (status.equals("accExists"))
            {
                Toast.makeText(this , "user already exists" , Toast.LENGTH_SHORT).show();
            }
        else{
            makeSnackbar("No such user..." );

        }


    }
    public void makeSnackbar(String text )
    {
        if (text.equals("No such user...")) {
            Snackbar.make(mainLayout, text, Snackbar.LENGTH_LONG)
                    .setText("No user found, Register instead")
                    .setAction("Got it!", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    })
                    .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                    .show();
        }
        else
            {
                Snackbar.make(mainLayout, text, Snackbar.LENGTH_LONG)
                        .setText("password incorrect, try again")
                        .setAction("Got it!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
    }

    public void registerUser(String user, String pass)
    {
        FirebaseDatabase db =  FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");
        db.getReference().child("users").child(user).child("password").setValue(pass);
        db.getReference().child("users").child(user).child("points").child("correct").setValue(0);
        db.getReference().child("users").child(user).child("points").child("overall").setValue(0);


    }

    public void openPicks()
    {
        Intent intent = new Intent(this, pickteam.class);
        intent.putExtra("username" , user.getText().toString());
        DatabaseReference reference = database.getReference("users").child(user.getText().toString()).child("fav");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                intent.putExtra("favorite",    (String) snapshot.getValue());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //intent.putExtra("games" , TodayMatches);
    }
    public void openGames()
    {
        Intent intent = new Intent(this, gamelist.class);
        intent.putExtra("username" , user.getText().toString());
        DatabaseReference reference = database.getReference("users").child(user.getText().toString()).child("fav");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                intent.putExtra("favorite",    (String) snapshot.getValue());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right , R.anim.slide_out_left);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //intent.putExtra("games" , TodayMatches);

    }


    public void RequestPerms() // can be used to request whatever dangerous perms i need (currently dont have any )
    {
       if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){
           System.out.println("internet granted");
       }
       else
           {
              if ( ActivityCompat.shouldShowRequestPermissionRationale(this  , Manifest.permission.INTERNET)) // this means it isnt the first time user wants to use permission but he has denied the last time
                  // therefore we should explain why we need internet
              {
                new AlertDialog.Builder(this)
                        .setTitle("permission needed")
                        .setMessage("this app needs internet")
                        .setPositiveButton("alright!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this  , new String[] {Manifest.permission.INTERNET} , 21);

                            }
                        })
                        .setNegativeButton("no thanks", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();

              }
              else
                  {
                        ActivityCompat.requestPermissions(this  , new String[] {Manifest.permission.INTERNET} , 21);
                  }
           }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 21) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "maybe next time..", Toast.LENGTH_SHORT).show();

            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
