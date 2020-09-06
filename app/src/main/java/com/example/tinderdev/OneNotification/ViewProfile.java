package com.example.tinderdev.OneNotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tinderdev.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class ViewProfile extends AppCompatActivity {


    private ImageView mProfileImage;
    private TextView mNameField, mDob, mAge, mCountry, mState, mCodingL, mInterest, mGithubL, mCurrentS, mSchool, mCollege, mGender;

    private DatabaseReference mCustomerDatabase;

    String key;
    int position;

    TextView t1,t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        key = getIntent().getExtras().getString("UserID");


        mNameField = findViewById(R.id.full_name);
        mProfileImage = findViewById(R.id.profileImage);

        mDob = findViewById(R.id.ProfileDob);
        mAge = findViewById(R.id.ProfileAge);
        mCountry = findViewById(R.id.ProfileCountry);
        mState = findViewById(R.id.ProfileState);
        mCodingL = findViewById(R.id.ProfileCodingL);
        mInterest = findViewById(R.id.ProfileInterest);
        mGithubL = findViewById(R.id.ProfileGitHub);
        mCurrentS = findViewById(R.id.ProfileCurrentS);
        mSchool = findViewById(R.id.ProfileSchool);
        mCollege = findViewById(R.id.ProfileCollege);
        mGender = findViewById(R.id.ProfileGender);


        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(key);

        getUserInfo();
    }

    private void getUserInfo() {
        mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){
                    Map<String, Object> map = (Map<String, Object>)snapshot.getValue();
                    if (map.get("Name")!=null){
                        mNameField.setText(map.get("Name").toString());
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
                        String profileImageUrl = map.get("profileimageUrl").toString();
                        switch(profileImageUrl){
                            case "default":
                                Glide.with(ViewProfile.this).load(R.drawable.codee).into(mProfileImage);
                                break;
                            default:
                                Glide.with(ViewProfile.this).load(profileImageUrl).into(mProfileImage);
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
