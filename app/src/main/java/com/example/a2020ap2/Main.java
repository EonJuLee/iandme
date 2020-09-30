package com.example.a2020ap2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;


public class Main extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private MapFragment fragmentMap;
    private mpcFragment fragmentmpc;
    private mppFragment fragmentmpp;
    private TodopFragment fragmentTodop;
    private TodocFragment fragmentTodoc;
    private hwpFragment fragmenthwp;
    private hwcFragment fragmenthwc;
    private FragmentTransaction transaction;
    SharedPreferences sf;
    FirebaseDatabase db;
    DatabaseReference mdr;
    String level,cur_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=FirebaseDatabase.getInstance();
        mdr=db.getReference();
        fragmentManager = getSupportFragmentManager();
        fragmentMap=new MapFragment();
        fragmentmpc=new mpcFragment();
        fragmentmpp=new mppFragment();
        fragmentTodop=new TodopFragment();
        fragmentTodoc=new TodocFragment();
        fragmenthwp=new hwpFragment();
        fragmenthwc=new hwcFragment();
        sf=getSharedPreferences("saved",0);
        level="";
        cur_id=sf.getString("id","0");

        mdr.child("user").child(cur_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                level=dataSnapshot.child("level").getValue().toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentMap).commitAllowingStateLoss();
    }

    public void clickHandler(View view)
    {
        transaction = fragmentManager.beginTransaction();

        switch(view.getId())
        {
            case R.id.btn_fragmentMap:
                transaction.replace(R.id.frameLayout, fragmentMap).commitAllowingStateLoss();
                break;
            case R.id.btn_fragmentTodo:
                if(level.equals("admin"))transaction.replace(R.id.frameLayout, fragmentTodop).commitAllowingStateLoss();
                else transaction.replace(R.id.frameLayout, fragmentTodoc).commitAllowingStateLoss();
                break;
            case R.id.btn_fragmenthw:
                if(level.equals("admin"))transaction.replace(R.id.frameLayout, fragmenthwp).commitAllowingStateLoss();
                else transaction.replace(R.id.frameLayout, fragmenthwc).commitAllowingStateLoss();
                break;
            case R.id.btn_fragmentmympp:
                if(level.equals("admin"))transaction.replace(R.id.frameLayout, fragmentmpp).commitAllowingStateLoss();
                else transaction.replace(R.id.frameLayout, fragmentmpc).commitAllowingStateLoss();
                break;
        }
    }

}
