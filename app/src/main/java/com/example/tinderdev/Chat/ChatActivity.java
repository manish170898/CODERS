package com.example.tinderdev.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tinderdev.Fragments.MessegesFragment;
import com.example.tinderdev.Matches.Matches;
import com.example.tinderdev.Matches.MatchesAdapter;
import com.example.tinderdev.OneNotification.ViewProfile;
import com.example.tinderdev.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private String currentUserID, matchID, chatId;

    private TextInputEditText messege;
    private FloatingActionButton send;

    private ImageView more, mProfileImage;

    private NestedScrollView scrollView;

    private TextView fullname;


    DatabaseReference mDatabaseUser, mDatabaseChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mProfileImage = findViewById(R.id.profileImage);
        more = findViewById(R.id.more);
        fullname = findViewById(R.id.full_name);
        scrollView = findViewById(R.id.ScrollVIewChat);

        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        matchID = getIntent().getExtras().getString("mUserID");


        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("Matches").child(matchID).child("ChatId");
        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        messege = findViewById(R.id.messege);
        send = findViewById(R.id.send);

        getName();
        getChatId();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessege();
            }
        });

        chatRecyclerView = findViewById(R.id.chatRecycler);
        chatRecyclerView.setNestedScrollingEnabled(false);
        chatRecyclerView.setHasFixedSize(false);

        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        chatRecyclerView.setLayoutManager(mChatLayoutManager);

        mChatAdapter = new ChatAdapter(getDataSetChat(),ChatActivity.this);
        chatRecyclerView.setAdapter(mChatAdapter);

        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(ChatActivity.this, more);
                popupMenu.getMenu().add("View Profile");
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getTitle().toString()){
                            case "View Profile":
                                Intent intent = new Intent(ChatActivity.this, ViewProfile.class);
                                intent.putExtra("UserID",matchID);
                                startActivity(intent);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        messege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.fullScroll(NestedScrollView.FOCUS_DOWN);
            }
        });

    }

    private void getName() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(matchID);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>)snapshot.getValue();
                    if (map.get("Name")!=null){
                        map.get("Name").toString();
                        fullname.setText(map.get("Name").toString());
                    }
                    if (map.get("profileimageUrl")!=null){
                        String profileImageUrl = map.get("profileimageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(ChatActivity.this).load(R.drawable.codee).into(mProfileImage);
                                break;
                            default:
                                Glide.with(ChatActivity.this).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessege() {
        String sendMessege = messege.getText().toString();
        if(!sendMessege.isEmpty()){
            DatabaseReference newMessegeDb = mDatabaseChat.push();
            Map newMessege = new HashMap();
            newMessege.put("createdByUser",currentUserID);
            newMessege.put("text",sendMessege);

            newMessegeDb.setValue(newMessege);
        }
        messege.setText(null);
    }

    private void getChatId(){
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatId = snapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatId);
                    getChatMesseges();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getChatMesseges() {
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String messege =null;
                    String createdByUser = null;

                    if(snapshot.child("text").getValue()!=null){
                        messege = snapshot.child("text").getValue().toString();
                    }
                    if(snapshot.child("createdByUser").getValue()!=null){
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                    }

                    if (messege!=null && createdByUser!=null){
                        Boolean currenUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currenUserBoolean =true;
                        }
                        Chat newMessege = new Chat(messege,currenUserBoolean);
                        resultChat.add(newMessege);
                        mChatAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private ArrayList<Chat> resultChat = new ArrayList<Chat>();
    private List<Chat> getDataSetChat() {
        return resultChat;
    }
}
