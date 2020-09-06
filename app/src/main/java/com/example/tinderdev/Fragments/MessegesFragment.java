package com.example.tinderdev.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tinderdev.Chat.Chat;
import com.example.tinderdev.Matches.Matches;
import com.example.tinderdev.Matches.MatchesAdapter;
import com.example.tinderdev.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessegesFragment extends Fragment {

    private RecyclerView messegeRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;

    private String currentUserID, mLastMessege = "No message", mChatID;


    DatabaseReference mDatabaseUser, mDatabaseChat;

    public MessegesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_messeges, container, false);

        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");


        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        messegeRecyclerView = root.findViewById(R.id.messegeRecycler);
        messegeRecyclerView.setNestedScrollingEnabled(false);
        messegeRecyclerView.setHasFixedSize(true);

        mMatchesLayoutManager = new LinearLayoutManager(getContext());
        messegeRecyclerView.setLayoutManager(mMatchesLayoutManager);

        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(),getContext());
        messegeRecyclerView.setAdapter(mMatchesAdapter);

        getUserMatchId();

        return root;
    }

    private ArrayList<Matches> resultMatches = new ArrayList<Matches>();
    private List<Matches> getDataSetMatches() {
        return resultMatches;
    }


    private void getUserMatchId() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot match : snapshot.getChildren()){
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String userId = snapshot.getKey();
                    String name = "";
                    String profileImage = "";
                    if(snapshot.child("Name").getValue()!=null){
                        name = snapshot.child("Name").getValue().toString();
                    }
                    if(snapshot.child("profileimageUrl").getValue()!=null){
                        profileImage = snapshot.child("profileimageUrl").getValue().toString();
                    }


                    Matches matches = new Matches(name,profileImage,userId,mLastMessege);
                    resultMatches.add(matches);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
