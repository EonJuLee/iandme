package com.example.a2020ap2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdaptor extends RecyclerView.Adapter<CustomAdaptor.CustomViewHolder> {
    private ArrayList<Parent> arrayList;
    private Context context;
    public interface  MyRecyclerViewClickListener1{
        void onItemClicked(int position);
        void onGoDownClicked(int position);
        void onDeleteClicked(int position);
    }
    private CustomAdaptor.MyRecyclerViewClickListener1 mListener=null;
    public void setOnClickListener1(CustomAdaptor.MyRecyclerViewClickListener1 listener)
    {
        mListener=listener;
    }

    public CustomAdaptor(ArrayList<Parent> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_parent,parent,false);
        CustomViewHolder holder=new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
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
            holder.bt_go_down.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    mListener.onGoDownClicked(pos);
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

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_id,tv_pn;
        Button bt_go_down,bt_delete;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_name=itemView.findViewById(R.id.parent_name);
            this.tv_id=itemView.findViewById(R.id.parent_id);
            this.tv_pn=itemView.findViewById(R.id.parent_pn);
            this.bt_go_down=itemView.findViewById(R.id.bt_parent_go_down);
            this.bt_delete=itemView.findViewById(R.id.bt_parent_delete);

        }
    }
}
