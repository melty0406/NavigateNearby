package com.example.user.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.Adapter.PlacesNavigatedAdapter;
import com.example.user.myapplication.Model.PlaceNavigated;
import com.example.user.myapplication.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout relay1;
    private TextView tvLogo;
    private EditText etEmail, etPassword;
    private ProgressBar pb;
    private Button btnLogin, btnSignUp, btnForgotPassword;
    public FirebaseAuth mAuth;


    private Handler mHandler = new Handler();
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            tvLogo.setVisibility(View.VISIBLE);
            relay1.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        tvLogo = findViewById(R.id.tv_logo);
        relay1 = findViewById(R.id.rl_1);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        pb = findViewById(R.id.progressBar);
        btnLogin = findViewById(R.id.btnLogin_login);
        btnSignUp = findViewById(R.id.btnLogin_signup);
        btnForgotPassword = findViewById(R.id.btn_forgot_password);

        // get Firebase Authentication Instance
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);

                String email, password;
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "[SYSTEM]: Please enter email!", Toast.LENGTH_LONG).show();
                    pb.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "[SYSTEM]: Please enter password!", Toast.LENGTH_LONG).show();
                    pb.setVisibility(View.GONE);
                    return;
                }

                signInUser(email,password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, ForgotPasswordActivity.class));
            }
        });

        Intent i = getIntent();
        if(i.getExtras() == null) {
            mHandler.postDelayed(mRunnable, 2000);
        }
        else{
            tvLogo.setVisibility(View.VISIBLE);
            relay1.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
            updateUI(currentUser);
    }

    public void updateUI(FirebaseUser user){
        if(user.isEmailVerified()) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void signInUser(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()) {
                                Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
                                pb.setVisibility(View.GONE);
                                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                pb.setVisibility(View.GONE);
                                new MaterialAlertDialogBuilder(SplashActivity.this, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                                        .setTitle("System")
                                        .setMessage("Please verify your email first!")
                                        .setPositiveButton("OK",null)
                                        .show();
                            }
                        }
                        else {
                            task.getException().printStackTrace();
                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
                            pb.setVisibility(View.GONE);
                        }
                    }
                });
    }
}