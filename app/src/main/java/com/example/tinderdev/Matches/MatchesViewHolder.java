package com.example.tinderdev.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tinderdev.Chat.ChatActivity;
import com.example.tinderdev.R;

public class MatchesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mName, mUserID, mLastMessege;
    public ImageView mImage;

    public MatchesViewHolder(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mImage = itemView.findViewById(R.id.UserImage);
        mName = itemView.findViewById(R.id.UserName);
        mUserID = itemView.findViewById(R.id.UserID);
        mLastMessege = itemView.findViewById(R.id.last_messege);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(view.getContext(), ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("mUserID",mUserID.getText().toString());
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);

    }
}
