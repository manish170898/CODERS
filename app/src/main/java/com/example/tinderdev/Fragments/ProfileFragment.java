package com.example.tinderdev.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tinderdev.UsersCommon.Choose_Login_Registration;
import com.example.tinderdev.R;
import com.example.tinderdev.UsersCommon.EditProfile;
import com.example.tinderdev.UsersCommon.Settings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ProfileFragment extends Fragment {


    private ImageView mProfileImage;
    private TextView mNameField, mDob, mAge, mCountry, mState, mCodingL, mInterest, mGithubL, mCurrentS, mSchool, mCollege, mGender;
    private FirebaseAuth mAuth;
    private DatabaseReference mCustomerDatabase;
    private String UserId, Name, profileImageUrl ;

    private CardView mSettings, mEdit;


    public ProfileFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        mNameField = root.findViewById(R.id.full_name);
        mProfileImage = root.findViewById(R.id.profileImage);

        mDob = root.findViewById(R.id.ProfileDob);
        mAge = root.findViewById(R.id.ProfileAge);
        mCountry = root.findViewById(R.id.ProfileCountry);
        mState = root.findViewById(R.id.ProfileState);
        mCodingL = root.findViewById(R.id.ProfileCodingL);
        mInterest = root.findViewById(R.id.ProfileInterest);
        mGithubL = root.findViewById(R.id.ProfileGitHub);
        mCurrentS = root.findViewById(R.id.ProfileCurrentS);
        mSchool = root.findViewById(R.id.ProfileSchool);
        mCollege = root.findViewById(R.id.ProfileCollege);
        mGender = root.findViewById(R.id.ProfileGender);

        mSettings = root.findViewById(R.id.CardSettings);
        mEdit = root.findViewById(R.id.CardEdit);

        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();

        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);

        getUserInfo();



        mSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getContext(), Settings.class);
                startActivity(intent);
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getContext(), EditProfile.class);
                startActivity(intent);
            }
        });


        return root;
    }


    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>)snapshot.getValue();
                    if (map.get("Name")!=null){
                        Name = map.get("Name").toString();
                        mNameField.setText(Name);
                    }
                    if (map.get("DOB")!=null){
                        mDob.setText(map.get("DOB").toString());
                    }
                    if (map.get("Age")!=null){
                        mAge.setText(map.get("Age").toString());
                    }
                    if (map.get("City")!=null){
                        mState.setText(map.get("City").toString());
                    }
                    if (map.get("CodingLanguages")!=null){
                        mCodingL.setText(map.get("CodingLanguages").toString());
                    }
                    if (map.get("Country")!=null){
                        mCountry.setText(map.get("Country").toString());
                    }
                    if (map.get("Gender")!=null){
                        mGender.setText(map.get("Gender").toString());
                    }
                    if (map.get("Interest")!=null){
                        mInterest.setText(map.get("Interest").toString());
                    }
                    if (map.get("Interest")!=null){
                        mInterest.setText(map.get("Interest").toString());
                    }
                    if (map.get("GitHub")!=null){
                        mGithubL.setText(map.get("GitHub").toString());
                    }
                    if (map.get("Status")!=null){
                        mCurrentS.setText(map.get("Status").toString());
                    }
                    if (map.get("School")!=null){
                        mSchool.setText(map.get("School").toString());
                    }
                    if (map.get("College")!=null){
                        mCollege.setText(map.get("College").toString());
                    }

                    if (map.get("profileimageUrl")!=null){
                        profileImageUrl = map.get("profileimageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(getView()).load(R.drawable.codee).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getView()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
