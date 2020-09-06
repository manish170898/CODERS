package com.example.tinderdev.UsersCommon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tinderdev.MainActivity;
import com.example.tinderdev.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserDetails extends AppCompatActivity {

    private TextInputLayout mAgel;

    private TextInputEditText mName, mDob, mAge, mInterest, mCity;
    private AutoCompleteTextView mCountry;
    private MultiAutoCompleteTextView mCoding;

    private Button mNext;
    private RadioGroup mRadioGroup;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        mName = findViewById(R.id.name);
        mDob = findViewById(R.id.dob);
        mAge = findViewById(R.id.age);
        mCountry = findViewById(R.id.country);
        mCity = findViewById(R.id.city);
        mRadioGroup = findViewById(R.id.radioGroup);
        mInterest = findViewById(R.id.interest);
        mCoding = findViewById(R.id.Coding);
        mNext = findViewById(R.id.next);

        mAgel = findViewById(R.id.agel);

        mAgel.setVisibility(View.INVISIBLE);
        mDob.setFocusable(false);

        mAuth = FirebaseAuth.getInstance();


        String[] countries = getResources().getStringArray(R.array.countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,countries);
        mCountry.setAdapter(adapter);


        String s = null;
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, Collections.singletonList(s));
        mCoding.setAdapter(adapter1);
        mCoding.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());



        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = mAuth.getCurrentUser().getUid();
                int selectID = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton =(RadioButton)findViewById(selectID);

                if(mName.getText().toString().isEmpty() ||mDob.getText().toString().isEmpty() || mAge.getText().toString().isEmpty()
                || radioButton.getText().toString().isEmpty() || mCountry.getText().toString().isEmpty() || mCity.getText().toString().isEmpty()
                || mCoding.getText().toString().isEmpty() || mInterest.getText().toString().isEmpty()){

                    Toast.makeText(UserDetails.this,"Fields Are Empty",Toast.LENGTH_LONG).show();
                }
                else{
                    DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                    Map newPost = new HashMap();
                    newPost.put("Name",mName.getText().toString());
                    newPost.put("DOB",mDob.getText().toString());
                    newPost.put("Age",mAge.getText().toString());
                    newPost.put("Gender", radioButton.getText().toString());
                    newPost.put("Country",mCountry.getText().toString());
                    newPost.put("City",mCity.getText().toString());
                    newPost.put("Interest",mInterest.getText().toString());
                    newPost.put("CodingLanguages",mCoding.getText().toString());
                    currentUserDB.setValue(newPost);


                    SharedPreferences sp =getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("UD","Yes");
                    editor.apply();

                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    firebaseUser.reload();
                    if(firebaseUser.isEmailVerified()){
                        Toast.makeText(UserDetails.this,"Verified",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UserDetails.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(UserDetails.this, EmailVerification.class);
                        startActivity(intent);
                        finish();
                    }
                }
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
            mAgel.setVisibility(View.VISIBLE);
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


    public String[] showInput(){
        String input = mCoding.getText().toString().trim();
        String[] singleInputs = input.split(",");
        String toast = "";
        for (int i=0; i<singleInputs.length; i++){
            toast+="Item"+i + singleInputs[i]+ "\n";
        }
        return singleInputs;
    }

}
