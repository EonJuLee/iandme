package com.example.a2020ap2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdaptor_c extends RecyclerView.Adapter<CustomAdaptor_c.CustomViewHolder_c> {
    private ArrayList<Parent> arrayList;
    private Context context;
    public interface  MyRecyclerViewClickListener2{
        void onItemClicked(int position);
        void onGoUpClicked(int position);
        void onDeleteClicked(int position);
    }
    private CustomAdaptor_c.MyRecyclerViewClickListener2 mListener=null;
    public void setOnClickListener2(CustomAdaptor_c.MyRecyclerViewClickListener2 listener)
    {
        mListener=listener;
    }
    public CustomAdaptor_c(ArrayList<Parent> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomAdaptor_c.CustomViewHolder_c onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_child,parent,false);
        CustomAdaptor_c.CustomViewHolder_c holder=new CustomAdaptor_c.CustomViewHolder_c(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdaptor_c.CustomViewHolder_c holder, int position) {
        holder.tv_id.setText(arrayList.get(position).getId());
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_pn.setText(arrayList.get(position).getPn());
        if(mListener!=null)
        {
            final int pos=position;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(pos);
                }
            });
            holder.bt_go_up.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mListener.onGoUpClicked(pos);
                }
            });
            holder.bt_delete.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mListener.onDeleteClicked(pos);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList!=null?arrayList.size():0);
    }

    public class CustomViewHolder_c extends RecyclerView.ViewHolder {
        TextView tv_name,tv_id,tv_pn;
        Button bt_go_up,bt_delete;
        public CustomViewHolder_c(@NonNull View itemView) {
            super(itemView);
            this.tv_name=itemView.findViewById(R.id.child_name);
            this.tv_id=itemView.findViewById(R.id.child_id);
            this.tv_pn=itemView.findViewById(R.id.child_pn);
            this.bt_go_up=itemView.findViewById(R.id.bt_child_go_up);
            this.bt_delete=itemView.findViewById(R.id.bt_child_delete);
        }
    }
}

