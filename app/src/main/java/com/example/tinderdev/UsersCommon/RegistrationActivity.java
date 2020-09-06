package com.example.tinderdev.UsersCommon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinderdev.MainActivity;
import com.example.tinderdev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            //"(?=.*[a-z])" +         //at least 1 lower case letter
            //"(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{4,}" +               //at least 4 characters
            "$");

    private Button mRegister, mGoSignUp;
    private EditText mEmail, mPassword;

    private ImageView image;
    private TextView text1,text2;

    private SharedPreferences sp;

    private FirebaseAuth mAtuh;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);
        mRegister = findViewById(R.id.register);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mGoSignUp = findViewById(R.id.go_back);

        image = findViewById(R.id.image);
        text1 = findViewById(R.id.texta);
        text2 = findViewById(R.id.textb);

        sp = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        mAtuh = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null ){
                    user.reload();
                    if(user.isEmailVerified()){
                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(RegistrationActivity.this, UserDetails.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        };

        mGoSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, Choose_Login_Registration.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View,String>(image,"logo_image");
                pairs[1] = new Pair<View,String>(text1,"logo_name");
                pairs[2] = new Pair<View,String>(text2,"text1");
                pairs[3] = new Pair<View,String>(mEmail,"text2");
                pairs[4] = new Pair<View,String>(mPassword,"text3");
                pairs[5] = new Pair<View,String>(mRegister,"text4");
                pairs[6] = new Pair<View,String>(mGoSignUp,"text5");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(RegistrationActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();
                return;
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    Toast.makeText(RegistrationActivity.this, "Fields are Empty",Toast.LENGTH_SHORT).show();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmail.setError("Please Enter a Valid Email Address");
                }
                else if(!PASSWORD_PATTERN.matcher(password).matches()){
                    Toast.makeText(RegistrationActivity.this,"Password Too Weak!",Toast.LENGTH_SHORT).show();
                }
                else {
                    checkEmail(email);
                    mAtuh.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            try {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                } else {
                                    FirebaseUser fuser = mAtuh.getCurrentUser();
                                    fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegistrationActivity.this, "Verification Email Has been Sent", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegistrationActivity.this, "Email not Sent:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    SharedPreferences.Editor editor = sp.edit();
                                    String s = "No";
                                    editor.putString("UD", s);
                                    editor.apply();

                                    String userID = mAtuh.getCurrentUser().getUid();
                                    DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

                                    Map newPost = new HashMap();
                                    newPost.put("profileimageUrl", "default");
                                    currentUserDB.setValue(newPost);
                                }
                            }
                            catch (FirebaseAuthInvalidCredentialsException |
                                    FirebaseAuthInvalidUserException e)
                            {
                                Toast.makeText(RegistrationActivity.this, "Invalid LoginID / Password", Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseNetworkException e)
                            {
                                Toast.makeText(RegistrationActivity.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseTooManyRequestsException e)
                            {
                                Toast.makeText(RegistrationActivity.this, "Do not Spam Sign In Button .", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(RegistrationActivity.this, "Sign Up Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAtuh.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAtuh.removeAuthStateListener(firebaseAuthStateListener);
    }

    public void checkEmail(String email){
        mAtuh.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                try {
                    boolean check = !task.getResult().getSignInMethods().isEmpty();
                    if(!check){
                    }
                    else{
                        mEmail.setError("Email already Registered");
                    }
                }
                catch (Exception e){
                }
            }
        });
    }
}
