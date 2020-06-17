package com.example.gocorona;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.MyViewHolder> {

    ArrayList<Deal> list;
    Context mContext;

    public AdapterClass(ArrayList<Deal> list) {
        this.list = list;
    }

    public AdapterClass(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext=parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_holder,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.id.setText(list.get(position).getName());
        holder.des.setText(list.get(position).getQuantity());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id , des;
        DatabaseReference refe;
        //TextView area,City;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.dealId);
            des= itemView.findViewById(R.id.descripition);
            refe=FirebaseDatabase.getInstance().getReference("").child("");


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String key=refe.getKey();

                    Intent detailActivity =new Intent(mContext,DetailedActivity.class);
                    int position = getAdapterPosition();
                    //list.get(position).getDealID();
                    detailActivity.putExtra("Name",list.get(position).getName());
                    detailActivity.putExtra("Quantity",list.get(position).getQuantity());
                    detailActivity.putExtra("postKey",list.get(position).getPostKey());

                    mContext.startActivity(detailActivity);

                }
            });

        }
    }


}