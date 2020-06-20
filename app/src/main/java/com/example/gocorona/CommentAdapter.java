package com.example.gocorona;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row= LayoutInflater.from(mContext).inflate(R.layout.card_holder,parent,false);

        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        holder.ShopName.setText(mData.get(position).getTextContent());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        TextView ShopName,disc;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            ShopName=itemView.findViewById(R.id.dealId);
            disc=itemView.findViewById(R.id.descripition);
        }
    }
}