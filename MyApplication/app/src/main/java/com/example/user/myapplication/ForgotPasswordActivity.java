package com.example.user.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "forgotPasswordActivity";
    private EditText etEmail;
    private Button btnSend;
    private ProgressBar pb;
    public static final String INTENT_REPLY = "com.example.user.myapplication.FORGOT";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.et_forgot_email);
        btnSend = findViewById(R.id.btn_send);
        pb = findViewById(R.id.pb_forgot);

        mAuth = FirebaseAuth.getInstance() ;

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                String email = etEmail.getText().toString();

                if(!TextUtils.isEmpty(email)){
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                pb.setVisibility(View.GONE);
                                Toast.makeText(ForgotPasswordActivity.this, "Password Reset Email has been sent!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ForgotPasswordActivity.this, SplashActivity.class);
                                i.putExtra(INTENT_REPLY, "forgotpassword");
                                startActivity(i);
                                finish();
                            }
                            else{
                                pb.setVisibility(View.GONE);
                                Toast.makeText(ForgotPasswordActivity.this, "Password Reset Email fail to send!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    pb.setVisibility(View.GONE);
                    Toast.makeText(ForgotPasswordActivity.this, "Please fill in your email!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
