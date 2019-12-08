package com.example.user.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.myapplication.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "signupActivity";
    private EditText etEmail, etPassword;
    private Button btnSignup, btnLogin;
    private ProgressBar pb;
    public static final String INTENT_REPLY = "com.example.user.myapplication.SIGNUP";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etEmail = findViewById(R.id.et_signup_email);
        etPassword = findViewById(R.id.et_signup_password);
        btnLogin = findViewById(R.id.btnSignup_login);
        btnSignup = findViewById(R.id.btnSignup_signup);
        pb = findViewById(R.id.progressBarSignup);

        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, SplashActivity.class);
                i.putExtra(INTENT_REPLY, true);
                startActivity(i);

            }
        });


        btnSignup.setOnClickListener(new View.OnClickListener() {
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

                registerUser(email,password);
            }
        });

    }

    private void registerUser(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    pb.setVisibility(View.GONE);
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SignUpActivity.this, "Email Verification has already sent. Please verify email in order to proceed!", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(SignUpActivity.this, SplashActivity.class);
                            i.putExtra(INTENT_REPLY, "signup");
                            startActivity(i);
                            finish();
                        }
                    });
                }
                else {
                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(),
                            Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
                }
            }
        });

    }
}
