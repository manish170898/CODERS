package com.example.tinderdev.OneNotification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tinderdev.R;

import java.util.List;

public class OneNotificationAdapter extends RecyclerView.Adapter<OneNotificationAdapter.MyViewHolder> {

    private List<OneNotification> oneNotificationList;
    private Context context;
    public OnItemClickListner mListner;

    public interface OnItemClickListner {
        void onItemAccept(int position, String key);
        void onItemDecline(int position, String key);
        void onViewProfile(int positoin, String key);
    }

    public void setOnItemCLickListner(OnItemClickListner listner){
        mListner = listner;
    }

    public OneNotificationAdapter(Context context,List<OneNotification> oneNotificationList) {
        this.oneNotificationList = oneNotificationList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profileitem,parent,false);
        return new MyViewHolder(view,mListner);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        OneNotification oneNotification = oneNotificationList.get(position);

        holder.mName.setText(oneNotification.getmName());
        holder.mAge.setText(oneNotification.getmAge());
        holder.mState.setText(oneNotification.getmState());
        holder.mCountry.setText(oneNotification.getmCountry());
        holder.mCodingL.setText(oneNotification.getmCodingL());
        holder.mInterest.setText(oneNotification.getmInterest());

        String profileIMage = oneNotification.getmProfileImage();
        if(profileIMage!=null) {
            switch (profileIMage) {
                case "default":
                    Glide.with(context).load(R.drawable.codee).into(holder.mProfileImage);
                    break;
                case "":
                    Glide.with(context).load(R.drawable.codee).into(holder.mProfileImage);
                    break;
                default:
                    Glide.with(context).load(profileIMage).into(holder.mProfileImage);
                    break;
            }
        }
        boolean isExpandable = oneNotificationList.get(position).isExpandable();
        holder.expandableLayout.setVisibility(isExpandable ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return oneNotificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView mName,mAge,mState,mCountry,mCodingL,mInterest;
        ImageView mProfileImage;
        LinearLayout linearLayout;
        RelativeLayout expandableLayout;
        Button mAccept,mDecline, mViewProfile;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListner listner) {
            super(itemView);

            mName = itemView.findViewById(R.id.mName);
            mAge = itemView.findViewById(R.id.mAge);
            mState = itemView.findViewById(R.id.mState);
            mCountry = itemView.findViewById(R.id.mCountry);
            mProfileImage = itemView.findViewById(R.id.mImage);
            mCodingL = itemView.findViewById(R.id.mCodingL);
            mInterest = itemView.findViewById(R.id.mInterest);
            mAccept = itemView.findViewById(R.id.accept);
            mDecline = itemView.findViewById(R.id.decline);
            mViewProfile = itemView.findViewById(R.id.viewprofile);

            linearLayout = itemView.findViewById(R.id.linear_layout);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);

            mViewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listner!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            OneNotification oneNotification = oneNotificationList.get(position);
                            listner.onViewProfile(position,oneNotification.getmKey());
                        }
                    }
                }
            });

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OneNotification notification = oneNotificationList.get(getAdapterPosition());
                    notification.setExpandable(!notification.isExpandable());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            mAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listner!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            OneNotification oneNotification = oneNotificationList.get(position);
                            listner.onItemAccept(position,oneNotification.getmKey());
                        }
                    }
                }
            });

            mDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listner!=null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            OneNotification oneNotification = oneNotificationList.get(position);
                            listner.onItemDecline(position,oneNotification.getmKey());
                        }
                    }
                }
            });


        }
    }
}
