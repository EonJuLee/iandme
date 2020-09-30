package com.example.a2020ap2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class admin_family extends AppCompatActivity {
    Button bt_ok;
    RecyclerView precyclerView,arecyclerView,crecyclerView;
    CustomAdaptor padapter;
    CustomAdaptor_a aadapter;
    CustomAdaptor_c cadapter;
    RecyclerView.LayoutManager playoutManager,clayoutManager,alayoutManager;
    ArrayList<Parent> parrayList,aarrayList,carrayList;
    FirebaseDatabase db;
    DatabaseReference mdr;
    SharedPreferences sf;
    String saved_id="",saved_family="",temp_name,temp_id,temp_pn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_family);
        bt_ok=(Button)findViewById(R.id.bt_admin_family_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(admin_family.this, Main.class);
                startActivity(it);
                finish();
            }
        });
        sf=getSharedPreferences("saved",0);
        db=FirebaseDatabase.getInstance();
        mdr=db.getReference();
        saved_id=sf.getString("id","0");
        saved_family=sf.getString("family","0");
        Log.d("로그",saved_family);
        precyclerView =findViewById(R.id.rv_family);
        precyclerView.setHasFixedSize(true);
        playoutManager=new LinearLayoutManager(this);
        precyclerView.setLayoutManager(playoutManager);
        parrayList=new ArrayList<>();

        mdr.child("family").child("member").child(saved_family).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("로그","hi");
                parrayList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    String name,id,pn;
                    name=snapshot.child("name").getValue().toString();
                    id=snapshot.child("id").getValue().toString();
                    pn=snapshot.child("pn").getValue().toString();
                    Parent parent=new Parent(name,id,pn);
                    Log.d("로그",id+pn+name);
                    parrayList.add(parent);
                }
                padapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        padapter=new CustomAdaptor(parrayList,this);
        precyclerView.setAdapter(padapter);
        padapter.setOnClickListener1(new CustomAdaptor.MyRecyclerViewClickListener1() {
            @Override
            public void onItemClicked(int position) {
            }

            @Override
            public void onGoDownClicked(int position) {
                temp_name = parrayList.get(position).getName();
                temp_id = parrayList.get(position).getId();
                temp_pn = parrayList.get(position).getPn();
                Parent p = new Parent(temp_name, temp_id, temp_pn);
                mdr.child("family").child("member").child(saved_family).child("child").push().setValue(p);
                mdr.child("family").child("member").child(saved_family).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("로그", "hi");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String name, id, pn;
                            name = snapshot.child("name").getValue().toString();
                            id = snapshot.child("id").getValue().toString();
                            pn = snapshot.child("pn").getValue().toString();
                            if (name.equals(temp_name) && id.equals(temp_id) && pn.equals(temp_pn)) {
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mdr.child("user").child(temp_id).child("level").setValue("child");
                carrayList.add(p);
                cadapter.notifyDataSetChanged();
                parrayList.remove(position);
                padapter.notifyItemRemoved(position);

            }

            @Override
            public void onDeleteClicked(int position) {
                temp_name = parrayList.get(position).getName();
                temp_id = parrayList.get(position).getId();
                temp_pn = parrayList.get(position).getPn();
                mdr.child("family").child("member").child(saved_family).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            String name,id,pn;
                            name=snapshot.child("name").getValue().toString();
                            id=snapshot.child("id").getValue().toString();
                            pn=snapshot.child("pn").getValue().toString();
                            if(name.equals(temp_name)&&id.equals(temp_id)&&pn.equals(temp_pn))
                            {
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mdr.child("user").child(temp_id).child("family").setValue("NULL");
                mdr.child("user").child(temp_id).child("level").removeValue();
                parrayList.remove(position);
                padapter.notifyItemRemoved(position);
            }
        });

        crecyclerView =findViewById(R.id.rv_child);
        crecyclerView.setHasFixedSize(true);
        clayoutManager=new LinearLayoutManager(this);
        crecyclerView.setLayoutManager(clayoutManager);
        carrayList=new ArrayList<>();

        mdr.child("family").child("member").child(saved_family).child("child").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                carrayList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    String name,id,pn;
                    name=snapshot.child("name").getValue().toString();
                    id=snapshot.child("id").getValue().toString();
                    pn=snapshot.child("pn").getValue().toString();
                    Parent parent=new Parent(name,id,pn);
                    carrayList.add(parent);
                }
                cadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        cadapter=new CustomAdaptor_c(carrayList,this);
        crecyclerView.setAdapter(cadapter);
        cadapter.setOnClickListener2(new CustomAdaptor_c.MyRecyclerViewClickListener2() {
            @Override
            public void onItemClicked(int position) {
            }

            @Override
            public void onGoUpClicked(int position) {
                temp_name=carrayList.get(position).getName();
                temp_id=carrayList.get(position).getId();
                temp_pn=carrayList.get(position).getPn();
                Parent p=new Parent(temp_name,temp_id,temp_pn);
                mdr.child("family").child("member").child(saved_family).child("admin").push().setValue(p);
                mdr.child("family").child("member").child(saved_family).child("child").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("로그","hi");
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            String name,id,pn;
                            name=snapshot.child("name").getValue().toString();
                            id=snapshot.child("id").getValue().toString();
                            pn=snapshot.child("pn").getValue().toString();
                            if(name.equals(temp_name)&&id.equals(temp_id)&&pn.equals(temp_pn))
                            {
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mdr.child("user").child(temp_id).child("level").setValue("admin");
                parrayList.add(p);
                padapter.notifyDataSetChanged();
                carrayList.remove(position);
                cadapter.notifyItemRemoved(position);

            }
            @Override
            public void onDeleteClicked(int position) {
                temp_name = carrayList.get(position).getName();
                temp_id = carrayList.get(position).getId();
                temp_pn = carrayList.get(position).getPn();
                mdr.child("family").child("member").child(saved_family).child("child").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            String name,id,pn;
                            name=snapshot.child("name").getValue().toString();
                            id=snapshot.child("id").getValue().toString();
                            pn=snapshot.child("pn").getValue().toString();
                            if(name.equals(temp_name)&&id.equals(temp_id)&&pn.equals(temp_pn))
                            {
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mdr.child("user").child(temp_id).child("family").setValue("NULL");
                mdr.child("user").child(temp_id).child("level").removeValue();
                carrayList.remove(position);
                cadapter.notifyItemRemoved(position);
            }
        });

        arecyclerView =findViewById(R.id.rv_applied);
        arecyclerView.setHasFixedSize(true);
        alayoutManager=new LinearLayoutManager(this);
        arecyclerView.setLayoutManager(alayoutManager);
        aarrayList=new ArrayList<>();

        mdr.child("family").child("member").child(saved_family).child("The_wait").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                aarrayList.clear();
                for(DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    String name,id,pn;
                    name=snapshot.child("name").getValue().toString();
                    id=snapshot.child("id").getValue().toString();
                    pn=snapshot.child("pn").getValue().toString();
                    Parent parent=new Parent(name,id,pn);
                    aarrayList.add(parent);
                }
                aadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        aadapter=new CustomAdaptor_a(aarrayList,this);
        arecyclerView.setAdapter(aadapter);
        aadapter.setOnClickListener(new CustomAdaptor_a.MyRecyclerViewClickListener() {
            @Override
            public void onItemClicked(int position) {
            }

            @Override
            public void onGoUpClicked(int position) {
                temp_name=aarrayList.get(position).getName();
                temp_id=aarrayList.get(position).getId();
                temp_pn=aarrayList.get(position).getPn();
                Parent p=new Parent(temp_name,temp_id,temp_pn);
                mdr.child("family").child("member").child(saved_family).child("admin").push().setValue(p);
                mdr.child("family").child("member").child(saved_family).child("The_wait").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            String name,id,pn;
                            name=snapshot.child("name").getValue().toString();
                            id=snapshot.child("id").getValue().toString();
                            pn=snapshot.child("pn").getValue().toString();
                            if(name.equals(temp_name)&&id.equals(temp_id)&&pn.equals(temp_pn))
                            {
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mdr.child("user").child(temp_id).child("applied").removeValue();
                mdr.child("user").child(temp_id).child("level").setValue("admin");
                mdr.child("user").child(temp_id).child("family").setValue(saved_family);
                parrayList.add(p);
                padapter.notifyDataSetChanged();
                aarrayList.remove(position);
                aadapter.notifyItemRemoved(position);

            }

            @Override
            public void onGoDownClicked(int position) {
                temp_name = aarrayList.get(position).getName();
                temp_id = aarrayList.get(position).getId();
                temp_pn = aarrayList.get(position).getPn();
                Parent p = new Parent(temp_name, temp_id, temp_pn);
                mdr.child("family").child("member").child(saved_family).child("child").push().setValue(p);
                mdr.child("family").child("member").child(saved_family).child("The_wait").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String name, id, pn;
                            name = snapshot.child("name").getValue().toString();
                            id = snapshot.child("id").getValue().toString();
                            pn = snapshot.child("pn").getValue().toString();
                            if (name.equals(temp_name) && id.equals(temp_id) && pn.equals(temp_pn)) {
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mdr.child("user").child(temp_id).child("applied").removeValue();
                mdr.child("user").child(temp_id).child("level").setValue("child");
                mdr.child("user").child(temp_id).child("family").setValue(saved_family);
                carrayList.add(p);
                cadapter.notifyDataSetChanged();
                aarrayList.remove(position);
                aadapter.notifyItemRemoved(position);

            }

            @Override
            public void onDeleteClicked(int position) {
                temp_name = aarrayList.get(position).getName();
                temp_id = aarrayList.get(position).getId();
                temp_pn = aarrayList.get(position).getPn();
                Toast.makeText(admin_family.this,"삭제",Toast.LENGTH_LONG).show();
                mdr.child("family").child("member").child(saved_family).child("The_wait").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren())
                        {
                            String name,id,pn;
                            name=snapshot.child("name").getValue().toString();
                            id=snapshot.child("id").getValue().toString();
                            pn=snapshot.child("pn").getValue().toString();
                            if(name.equals(temp_name)&&id.equals(temp_id)&&pn.equals(temp_pn))
                            {
                                snapshot.getRef().removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                mdr.child("user").child(temp_id).child("applied").removeValue();
                aarrayList.remove(position);
                aadapter.notifyItemRemoved(position);
            }
        });
    }


}
