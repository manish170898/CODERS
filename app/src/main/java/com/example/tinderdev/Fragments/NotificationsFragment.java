package com.example.tinderdev.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tinderdev.OneNotification.OneNotification;
import com.example.tinderdev.OneNotification.OneNotificationAdapter;
import com.example.tinderdev.OneNotification.ViewProfile;
import com.example.tinderdev.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<OneNotification> oneNotificationList;
    private OneNotificationAdapter oneNotificationAdapter;

    public NotificationsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = root.findViewById(R.id.notiRecycler);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));
        oneNotificationList = new ArrayList<OneNotification>();

        getUsers();

        oneNotificationAdapter = new OneNotificationAdapter(getContext(), oneNotificationList);
        recyclerView.setAdapter(oneNotificationAdapter);
        recyclerView.setHasFixedSize(true);

        oneNotificationAdapter.setOnItemCLickListner(new OneNotificationAdapter.OnItemClickListner() {
            @Override
            public void onItemAccept(int position, String key) {

                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                String k =FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("connections").child("yes").child(key).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("Matches").child(key).child("ChatId").setValue(k);

                FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("connections").child("yes").child(UID).removeValue();
                FirebaseDatabase.getInstance().getReference().child("Users").child(key).child("Matches").child(UID).child("ChatId").setValue(k);

                oneNotificationList.remove(position);
                oneNotificationAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onItemDecline(int position, String key) {

                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("connections").child("yes").child(key).removeValue();
                oneNotificationList.remove(position);
                oneNotificationAdapter.notifyItemRemoved(position);
            }

            @Override
            public void onViewProfile(int positoin, String key) {
                Intent intent = new Intent(getContext(), ViewProfile.class);
                intent.putExtra("UserID",key);
                intent.putExtra("Position",positoin);
                startActivity(intent);
            }
        });

        return root;
    }

    private void getUsers() {
        String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Users").child(UID).child("connections").child("yes");
        dr.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot possibleMatch : snapshot.getChildren()){
                        FetchPossibleMatchInfo(possibleMatch.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchPossibleMatchInfo(final String key) {
        DatabaseReference usedb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        usedb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name="",age="",state="",country="",codingl="",interest="",image="";
                    if(snapshot.child("Name").getValue()!=null){
                        name = snapshot.child("Name").getValue().toString();
                    }
                    if(snapshot.child("City").getValue()!=null){
                        state = snapshot.child("City").getValue().toString();
                    }
                    if(snapshot.child("CodingLanguages").getValue()!=null){
                        codingl = snapshot.child("CodingLanguages").getValue().toString();
                    }
                    if(snapshot.child("Country").getValue()!=null){
                        country = snapshot.child("Country").getValue().toString();
                    }
                    if(snapshot.child("Interest").getValue()!=null){
                        interest = snapshot.child("Interest").getValue().toString();
                    }
                    if(snapshot.child("Age").getValue()!=null){
                        age = snapshot.child("Age").getValue().toString();
                    }
                    if(snapshot.child("profileimageUrl").getValue()!=null){
                        image = snapshot.child("profileimageUrl").getValue().toString();
                    }
                    OneNotification oneNotification =  new OneNotification(name,age,state,country,image,codingl,interest,key);
                    oneNotificationList.add(oneNotification);
                    oneNotificationAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
