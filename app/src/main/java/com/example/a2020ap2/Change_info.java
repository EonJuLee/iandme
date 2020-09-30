package com.example.a2020ap2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Change_info extends AppCompatActivity {
    EditText et_pw1,et_pw2,et_email,et_pn;
    FirebaseDatabase db;
    DatabaseReference mdr;
    SharedPreferences sf;
    String cur_id,pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        db=FirebaseDatabase.getInstance();
        mdr=db.getReference();
        sf=getSharedPreferences("saved",0);
        cur_id=sf.getString("id","0");
        Button bt_change_pw= (Button) findViewById(R.id.bt_change_pw);
        Button bt_change_email= (Button) findViewById(R.id.bt_change_email);
        Button bt_change_pn= (Button) findViewById(R.id.bt_change_pn);
        Button bt_change_info_go_back = (Button) findViewById(R.id.bt_change_information_go_back);
        et_pw1=(EditText)findViewById(R.id.et_change_pw1);
        et_pw2=(EditText)findViewById(R.id.et_change_pw2);
        et_email=(EditText)findViewById(R.id.et_change_email);
        et_pn=(EditText)findViewById(R.id.et_change_pn);
        bt_change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pw1,pw2;
                pw1=et_pw1.getText().toString();
                pw2=et_pw2.getText().toString();
                if(pw1.equals(pw2)) {
                    Toast.makeText(Change_info.this,"비밀번호가 변경되었습니다.",Toast.LENGTH_LONG).show();
                    pw1=SHA256.getSHA256(pw1);
                    mdr.child("user").child(cur_id).child("password").setValue(pw1);
                }
                else
                {
                    Toast.makeText(Change_info.this,"비밀번호가 동일하지 않습니다",Toast.LENGTH_LONG).show();
                }
            }
        });
        bt_change_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=et_email.getText().toString();
                Toast.makeText(Change_info.this,"이메일이 변경되었습니다.",Toast.LENGTH_LONG).show();
                mdr.child("user").child(cur_id).child("email").setValue(email);
            }
        });
        bt_change_pn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pn=et_pn.getText().toString();
                Toast.makeText(Change_info.this,"휴대전화 번호가 변경되었습니다.",Toast.LENGTH_LONG).show();
                mdr.child("user").child(cur_id).child("phone").setValue(pn);
            }
        });
        bt_change_info_go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Change_info.this, Main.class);
                startActivity(it);
                finish();
            }
        });
    }
}
