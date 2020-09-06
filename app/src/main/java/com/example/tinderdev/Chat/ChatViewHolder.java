package com.example.tinderdev.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinderdev.R;

public class ChatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMessege;
    public LinearLayout mContainer, mAcont;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);

        mMessege = itemView.findViewById(R.id.messegea);
        mContainer = itemView.findViewById(R.id.container);
        mAcont = itemView.findViewById(R.id.acont);
    }

    @Override
    public void onClick(View view) {

    }
}
