package com.example.a2020ap2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity{
    EditText ed_id,ed_pw;
    CheckBox cb_login;
    FirebaseDatabase db;
    DatabaseReference mdr;
    String cur_id,cur_pw;
    String saved_id,saved_pw;
    boolean isChecked_auto_login;
    SharedPreferences sf;
    private String changed_pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sf=getSharedPreferences("saved",0);
        String saved=sf.getString("save","false");
        db=FirebaseDatabase.getInstance();
        mdr=db.getReference();
        ed_id=(EditText)findViewById(R.id.id);
        ed_pw=(EditText)findViewById(R.id.password);
        Button bt_login=(Button)findViewById(R.id.bt_login);
        Button bt_signup=(Button)findViewById(R.id.bt_sign_up);
        Button bt_findid=(Button)findViewById(R.id.bt_find_id);
        Button bt_findpw=(Button)findViewById(R.id.bt_find_password);
        bt_findid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(LoginActivity.this,Find_id.class);
                startActivity(it);
            }
        });
        bt_findpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(LoginActivity.this,Find_pw.class);
                startActivity(it);
            }
        });
        cb_login=(CheckBox)findViewById(R.id.cb_login_state);
        cb_login.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked())
                {
                    isChecked_auto_login=true;
                }
                else isChecked_auto_login=false;
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                cur_id=ed_id.getText().toString();
                cur_pw=ed_pw.getText().toString();
                changed_pw= SHA256.getSHA256(cur_pw);

                if(cur_id.length()==0)
                {
                    Toast.makeText(LoginActivity.this,"아이디를 입력해 주세요",Toast.LENGTH_LONG).show();
                }
                else if(cur_pw.length()==0)
                {
                    Toast.makeText(LoginActivity.this,"비밀번호를 입력해 주세요",Toast.LENGTH_LONG).show();
                }
                else {
                    mdr.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(cur_id)) {
                                String pw=dataSnapshot.child(cur_id).child("password").getValue().toString();
                                String family=dataSnapshot.child(cur_id).child("family").getValue().toString();
                                String name=dataSnapshot.child(cur_id).child("name").getValue().toString();
                                String pn=dataSnapshot.child(cur_id).child("phone").getValue().toString();
                                if(pw.equals(changed_pw))
                                {
                                    SharedPreferences.Editor editor=sf.edit();
                                    editor.putString("save",""+isChecked_auto_login);
                                    editor.putString("id",cur_id);
                                    editor.putString("pw",cur_pw);
                                    editor.putString("family",family);
                                    editor.putString("name",name);
                                    editor.putString("pn",pn);
                                    editor.commit();
                                    if(family.equals("NULL"))
                                    {
                                        Intent it=new Intent(getApplicationContext(),Find_family.class);
                                        startActivity(it);
                                        finish();
                                    }
                                    else {
                                        Intent it = new Intent(getApplicationContext(), Main.class);
                                        startActivity(it);
                                        finish();
                                    }
                                }
                                else    Toast.makeText(LoginActivity.this,"존재 하지 않는 사용자입니다.",Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"존재 하지 않는 사용자입니다.",Toast.LENGTH_LONG).show();
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
            }
        });
        bt_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent it=new Intent(getApplicationContext(),Sign_up.class);
                startActivity(it);
                finish();
            }
        });
    }
}