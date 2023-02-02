package com.example.theproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class picks extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String userName;
    FirebaseDatabase database = FirebaseDatabase.getInstance("https://theproject-1f2b4-default-rtdb.europe-west1.firebasedatabase.app/");
    ListView lv;
    boolean flagNotif = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picks);
        overridePendingTransition(0, 0);
        userName = getIntent().getStringExtra("username");
        if (getIntent().getStringExtra("jsonhome") == null)
        {
            System.out.println("CAME FROM NOTIFICATION");
            flagNotif = true;

        }
        bottomNavigationView = findViewById(R.id.picksNavBar);
        bottomNavigationView.setSelectedItemId(R.id.three);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.one)
                {
                    Intent intent;
                    if (!flagNotif) {
                        System.out.println("FLAG NOTIF FALSE");

                        getIntent().getStringExtra("jsonhome");
                         intent = new Intent(picks.this, profile.class);
                        intent.putExtra("username", userName);
                        intent.putExtra("jsonhome", getIntent().getStringExtra("jsonhome"));
                        intent.putExtra("jsonaway", getIntent().getStringExtra("jsonaway"));
                        intent.putExtra("gamelist", (ArrayList) getIntent().getExtras().get("gamelist"));
                    }
                    else
                        {
                            System.out.println("FLAG NOTIF TRUE AKDMWODMAOWAMOWAMWAOIAMOIMOIWA");
                            intent = new Intent(picks.this, profile.class);
                            intent.putExtra("username" , userName);

                            intent.putExtra("cameFromNotif", "true");


                        }
                    startActivity(intent);


                }
                if (item.getItemId() == R.id.two)
                {
                    if (!flagNotif) {
                        System.out.println("FLAG NOTIF FALSE");

                        getIntent().getStringExtra("jsonhome");
                        Intent intent = new Intent(picks.this, gamelist.class);
                        intent.putExtra("username", userName);
                        intent.putExtra("jsonhome", getIntent().getStringExtra("jsonhome"));
                        intent.putExtra("jsonaway", getIntent().getStringExtra("jsonaway"));
                        intent.putExtra("gamelist", (ArrayList) getIntent().getExtras().get("gamelist"));
                        startActivity(intent);
                    }
                    else
                        {
                            DatabaseReference reference = database.getReference("users").child(userName).child("fav");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String favo =(String) snapshot.getValue();
                                    Intent intent = new Intent(picks.this, gamelist.class);
                                    intent.putExtra("favorite" , favo);
                                    intent.putExtra("username" , userName);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            System.out.println("FLAG NOTIF TRUE");


                        }

                }
                if (item.getItemId() == R.id.three)
                {
                    /*
                    Intent intent = new Intent(picks.this , picks.class);
                    intent.putExtra("username" , userName);
                    intent.putExtra("jsonhome" , getIntent().getStringExtra("jsonhome"));
                    intent.putExtra("jsonaway" , getIntent().getStringExtra("jsonaway"));
                    intent.putExtra("gamelist" ,(ArrayList) getIntent().getExtras().get("gamelist"));
                    startActivity(intent);

                     */
                    item.setChecked(true);


                }
                return false;
            }


        });
        makePickList();

    }
    public void makePickList()
    {

        DatabaseReference reference = database.getReference("users").child(userName).child("picks").child("correctPix");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            ArrayList<PickHelper> helperArrayList = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds  : snapshot.getChildren())
                {


                    PickHelper pickHelper = new PickHelper ((String) ds.child("hometeam").getValue() ,(String) ds.child("awayteam").getValue() ,(String)ds.child("homegoal").getValue() ,(String)ds.child("awaygoal").getValue(),(String) ds.child("date").getValue() ,(String)ds.child("guess").getValue() ,(Boolean) ds.child("iscorrect").getValue());
                    helperArrayList.add(pickHelper);

                }
                // they run when they hear that the ting goes bang
                System.out.println(Arrays.toString(helperArrayList.toArray()));
                setList(helperArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setList(ArrayList<PickHelper> arraylist)
    {
        PickHelperAdapter pickHelperAdapter = new PickHelperAdapter(picks.this ,0 , 0 ,arraylist);
        lv = findViewById(R.id.pickListview);
        //lv.setOnItemClickListener(gamelist.this::onItemClick);
        lv.setAdapter(pickHelperAdapter);

        //ListAdapter adapter = new ListAdapter()
    }

}