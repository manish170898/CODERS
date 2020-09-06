package com.example.tinderdev.Chat;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tinderdev.R;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private List<Chat> chatList;
    private Context context;

    public ChatAdapter(List<Chat> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatitem,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        ChatViewHolder rcv = new ChatViewHolder(view);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.mMessege.setText(chatList.get(position).getMessege());
        if(chatList.get(position).getCurrentUser()){
            holder.mAcont.setGravity(Gravity.END);
            holder.mMessege.setTextColor(Color.parseColor("#000000"));
            holder.mContainer.setBackgroundResource(R.drawable.your_messege);
        }else {
            holder.mAcont.setGravity(Gravity.START);
            holder.mMessege.setTextColor(Color.parseColor("#000000"));
            holder.mContainer.setBackgroundResource(R.drawable.match_messege);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
}
