package com.example.tinderdev.UsersCommon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.tinderdev.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private EditText mNameField;
    private Button mConfirm;
    private ImageView mProfileImage;
    private FirebaseAuth mAuth;
    private AutoCompleteTextView mCountry, mCurrentS;
    private DatabaseReference mCustomerDatabase;
    private String UserId, Name, profileImageUrl;
    private Uri resultUri;

    private EditText mDob, mAge, mState, mCodingL, mInterest, mGithubL, mSchool, mCollege, mGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mNameField = findViewById(R.id.Name);
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

        mConfirm = findViewById(R.id.Confirm);
        mAuth = FirebaseAuth.getInstance();
        UserId = mAuth.getCurrentUser().getUid();


        mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);

        getUserInfo();

        String[] countries = getResources().getStringArray(R.array.countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
        mCountry.setAdapter(adapter);


        String [] status = getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,status);
        mCurrentS.setAdapter(adapter1);


        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);
            }
        });

        mDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), datePickerListner, mYear, mMonth,mDay);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });


        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        mCurrentS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentS.setText("");
            }
        });

    }

    private DatePickerDialog.OnDateSetListener datePickerListner = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar c =Calendar.getInstance();
            c.set(Calendar.YEAR,year);
            c.set(Calendar.MONTH,month);
            c.set(Calendar.DAY_OF_MONTH,day);

            String format = new SimpleDateFormat("dd MM YYYY").format(c.getTime());
            mDob.setText(format);
            mAge.setText(Integer.toString(calculateAge(c.getTimeInMillis())));
        }
    };

    private int calculateAge(long timeInMillis) {
        Calendar dob = Calendar.getInstance();
        dob.setTimeInMillis(timeInMillis);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR)-dob.get(Calendar.YEAR);

        if(today.get(Calendar.MONTH)<dob.get(Calendar.MONTH)){
            age--;
        }
        else if( today.get(Calendar.MONTH) == dob.get(Calendar.MONTH) && today.get(Calendar.DAY_OF_MONTH)< dob.get(Calendar.DAY_OF_MONTH)){
            age--;
        }

        return  age;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {     // for image intent
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            final Uri imageUri = data.getData();
            resultUri = imageUri;
            mProfileImage.setImageURI(resultUri);
        }
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
                                Glide.with(getApplication()).load(R.mipmap.ic_launcher).into(mProfileImage);
                                break;
                            default:
                                Glide.with(getApplication()).load(profileImageUrl).into(mProfileImage);
                                break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void saveUserInformation() {
        Name = mNameField.getText().toString();

        final Map userInfo = new HashMap();
        userInfo.put("Name",Name);
        userInfo.put("Age",mAge.getText().toString());
        userInfo.put("City",mState.getText().toString());
        userInfo.put("CodingLanguages",mCodingL.getText().toString());
        userInfo.put("Country",mCountry.getText().toString());
        userInfo.put("DOB",mDob.getText().toString());
        userInfo.put("Gender",mGender.getText().toString());
        userInfo.put("Interest",mInterest.getText().toString());
        userInfo.put("GitHub",mGithubL.getText().toString());
        userInfo.put("Status",mCurrentS.getText().toString());
        userInfo.put("School",mSchool.getText().toString());
        userInfo.put("College",mCollege.getText().toString());
        mCustomerDatabase.updateChildren(userInfo);

        if(resultUri!=null){
            final StorageReference filepath = FirebaseStorage.getInstance().getReference().child("profileImages").child(UserId);
            Bitmap bitmap = null;

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();                       //Compress IMage
            bitmap.compress(Bitmap.CompressFormat.JPEG,20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = filepath.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    finish();
                }
            });

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Map newImage = new HashMap();
                            newImage.put("profileimageUrl", uri.toString());
                            mCustomerDatabase.updateChildren(newImage);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            finish();
                        }
                    });
                }
            });
        }
        else{
            finish();
        }
    }
}
