package com.example.tinderdev.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinderdev.SwingFling.ArrayAdapter;
import com.example.tinderdev.SwingFling.Cards;
import com.example.tinderdev.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ArrayAdapter arrayAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference usersDb;
    private String currentUID;
    private TextView noUsers;

    ListView listView;
    List<Cards> rowitems;


    public HomeFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        noUsers = root.findViewById(R.id.noUsers);
        mAuth = FirebaseAuth.getInstance();
        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUID = mAuth.getCurrentUser().getUid();

        getUsers();
        rowitems = new ArrayList<Cards>();

        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.item,rowitems);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView)root.findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowitems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Cards obj  =(Cards) dataObject;
                String userID = obj.getUserId();
                usersDb.child(userID).child("connections").child("nope").child(currentUID).setValue(true);
                Toast.makeText(getActivity(), "Rejected!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Cards obj  =(Cards) dataObject;
                String userID = obj.getUserId();
                usersDb.child(userID).child("connections").child("yes").child(currentUID).setValue(true);
                Toast.makeText(getActivity(), "Request Sent!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(getActivity(), "Clicked!",Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    public void getUsers(){
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child("Users");
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists() && !snapshot.child("connections").child("nope").hasChild(currentUID) &&
                        !snapshot.child("connections").child("yes").hasChild(currentUID) &&
                        !snapshot.child("Matches").hasChild(currentUID)){

                    if(!snapshot.getKey().equals(currentUID)) {
                        String profileImageUrl = "default";
                        if(snapshot.child("profileimageUrl").getValue()!=null) {
                            if (!snapshot.child("profileimageUrl").getValue().equals("default")) {
                                profileImageUrl = snapshot.child("profileimageUrl").getValue().toString();
                            }
                        }
                        Cards item = new Cards(snapshot.getKey(), snapshot.child("Name").getValue().toString(), profileImageUrl);
                        rowitems.add(item);
                        arrayAdapter.notifyDataSetChanged();
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
}
