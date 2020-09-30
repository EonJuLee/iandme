package com.example.a2020ap2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends Activity {
    SharedPreferences sf;
    FirebaseDatabase db;
    DatabaseReference mdr;
    String saved_id,saved_pw,changed_pw,saved_family;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding);
        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sf=getSharedPreferences("saved",0);
                String saved=sf.getString("save","false");
                Log.d("new_start",saved);
                db=FirebaseDatabase.getInstance();
                mdr=db.getReference();
                if(!saved.equals("false"))
                {
                    saved_id=sf.getString("id","0");
                    saved_pw=sf.getString("pw","0");
                    saved_family=sf.getString("family","0");
                    changed_pw=SHA256.getSHA256(saved_pw);
                    mdr.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(saved_id)) {
                                String pw=dataSnapshot.child(saved_id).child("password").getValue().toString();
                                if(pw.equals(changed_pw))
                                {
                                    if(saved_family.equals("NULL"))
                                    {
                                        Intent it = new Intent(getApplicationContext(), Find_family.class);
                                        startActivity(it);
                                        finish();
                                    }
                                    else {
                                        Intent it = new Intent(getApplicationContext(), Main.class);
                                        startActivity(it);
                                        finish();
                                    }
                                }
                                else {
                                    Toast.makeText(MainActivity.this, "존재 하지 않는 사용자입니다. 로그인 창으로 이동하겠습니다.", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();

                                }

                            }
                            else
                            {
                                Toast.makeText(MainActivity.this,"존재 하지 않는 사용자입니다. 로그인 창으로 이동하겠습니다.",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }

}