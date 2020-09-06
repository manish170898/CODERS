package com.example.tinderdev.Matches;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tinderdev.R;

import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesViewHolder> {

    private List<Matches> matchesList;
    private Context context;

    public MatchesAdapter(List<Matches> matchesList, Context context) {
        this.matchesList = matchesList;
        this.context = context;
    }

    @NonNull
    @Override
    public MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messegeitem,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        MatchesViewHolder rcv = new MatchesViewHolder(view);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull MatchesViewHolder holder, int position) {
        holder.mName.setText(matchesList.get(position).getmName());
        holder.mUserID.setText(matchesList.get(position).getmUserId());
        holder.mLastMessege.setText(matchesList.get(position).getmLastMessege());
        String profileIMage = matchesList.get(position).getmProfileImage();
        if(profileIMage!=null) {
            switch (profileIMage) {
                case "default":
                    Glide.with(context).load(R.drawable.codee).into(holder.mImage);
                    break;
                case "":
                    Glide.with(context).load(R.drawable.codee).into(holder.mImage);
                    break;
                default:
                    Glide.with(context).load(profileIMage).into(holder.mImage);
                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}
