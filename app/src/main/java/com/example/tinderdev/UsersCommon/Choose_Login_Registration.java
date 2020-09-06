package com.example.tinderdev.UsersCommon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinderdev.MainActivity;
import com.example.tinderdev.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Choose_Login_Registration extends AppCompatActivity {

    private Button mSignUp,mLogin,mForgotPassword;
    private TextInputEditText mEmail, mPassword;

    private ImageView logo_image;
    private TextView text1,text2;

    private String s;

    private FirebaseAuth mAtuh;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose__login__registration);

        mLogin = findViewById(R.id.login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mSignUp = findViewById(R.id.signup);
        mForgotPassword = findViewById(R.id.forgotpassword);

        logo_image = findViewById(R.id.logo_image);
        text1 = findViewById(R.id.text1);
        text2 = findViewById(R.id.text2);

        mAtuh = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null ){
                    user.reload();
                    SharedPreferences sp =getApplicationContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                    String ud = sp.getString("UD","");

                    if(user.isEmailVerified()){
                        if(ud.equals("No")){
                            Intent intent = new Intent(Choose_Login_Registration.this, UserDetails.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent = new Intent(Choose_Login_Registration.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                      }
                    else {
                        if(ud.equals("No")){
                            Intent intent = new Intent(Choose_Login_Registration.this, UserDetails.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Intent intent = new Intent(Choose_Login_Registration.this, EmailVerification.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
            }
        };

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()){
                    mAtuh.signInWithEmailAndPassword(email, password).addOnCompleteListener(Choose_Login_Registration.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            try {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                            }
                            catch (FirebaseAuthInvalidCredentialsException |
                            FirebaseAuthInvalidUserException e)
                            {
                                Toast.makeText(Choose_Login_Registration.this, "Invalid LoginID / Password", Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseNetworkException e)
                            {
                                Toast.makeText(Choose_Login_Registration.this, "Please Check Internet Connection.", Toast.LENGTH_SHORT).show();
                            }
                            catch (FirebaseTooManyRequestsException e)
                            {
                                Toast.makeText(Choose_Login_Registration.this, "Do not Spam Sign In Button .", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(Choose_Login_Registration.this, "Sign Up Error:" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(Choose_Login_Registration.this, "Fields are Empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Choose_Login_Registration.this, RegistrationActivity.class);
                Pair[] pairs = new Pair[7];
                pairs[0] = new Pair<View,String>(logo_image,"logo_image");
                pairs[1] = new Pair<View,String>(text1,"logo_name");
                pairs[2] = new Pair<View,String>(text2,"text1");
                pairs[3] = new Pair<View,String>(mEmail,"text2");
                pairs[4] = new Pair<View,String>(mPassword,"text3");
                pairs[5] = new Pair<View,String>(mLogin,"text4");
                pairs[6] = new Pair<View,String>(mSignUp,"text5");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Choose_Login_Registration.this,pairs);

                startActivity(intent,options.toBundle());
                finish();
                return;
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Choose_Login_Registration.this, ForgotPassword.class);
                startActivity(intent);
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
}
