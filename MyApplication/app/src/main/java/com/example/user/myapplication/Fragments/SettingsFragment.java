package com.example.user.myapplication.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.example.user.myapplication.Adapter.PlacesNavigatedAdapter;
import com.example.user.myapplication.MainActivity;
import com.example.user.myapplication.Model.PlaceNavigated;
import com.example.user.myapplication.R;
import com.example.user.myapplication.SplashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import io.paperdb.Paper;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    private LinearLayout llHistory, llPassword, llDelete, llLogout;
    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference mDatabaseReference;
    private String user_id = "";


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("place_navigated");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        llHistory = v.findViewById(R.id.ll_setting_history);
        llPassword = v.findViewById(R.id.ll_setting_password);
        llDelete = v.findViewById(R.id.ll_setting_delete);
        llLogout = v.findViewById(R.id.ll_setting_logout);

        llHistory.setOnClickListener(this);
        llPassword.setOnClickListener(this);
        llDelete.setOnClickListener(this);
        llLogout.setOnClickListener(this);

        if(firebaseUser != null){
            user_id = firebaseUser.getUid();
        }

        return v;
    }

    @Override
    public void onClick(View v) {

        LayoutInflater li = LayoutInflater.from(getActivity());
        View passwordView = li.inflate(R.layout.dialog_reset_password, null);

        switch (v.getId()){
            case R.id.ll_setting_history:
                new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setTitle("Clear Navigation History")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        DataSnapshot userSnapShot = dataSnapshot.child(user_id);

                                        if(userSnapShot != null) {
                                            for (DataSnapshot dataSnapshot1 : userSnapShot.getChildren()) {
                                                dataSnapshot1.getRef().removeValue();
                                            }
                                            Toast.makeText(getActivity(), "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(getActivity(), "There is no data in your navigation history!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getActivity(), "Something is wrong!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            case R.id.ll_setting_password:
                final String email = firebaseUser.getEmail();

                new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setTitle("Reset Password")
                        .setMessage("An reset password link will be sent to your email. Are you confirm?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(getActivity(), "Password Reset Link has been sent to your email address, please check your inbox.", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(getActivity(), "Fail to send Password Reset Link to your Email.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
                break;
            case R.id.ll_setting_delete:
                new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setTitle("Delete Account")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        firebaseUser.delete();

                                        mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                DataSnapshot userSnapShot = dataSnapshot.child(user_id);

                                                if(userSnapShot != null) {
                                                    for (DataSnapshot dataSnapshot1 : userSnapShot.getChildren()) {
                                                        dataSnapshot1.getRef().removeValue();
                                                    }
                                                    Toast.makeText(getActivity(), "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(getActivity(), "There is no data in your navigation history!", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Toast.makeText(getActivity(), "Something is wrong!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Intent i = new Intent(getActivity(),SplashActivity.class);
                                        startActivity(i);
                                        getActivity().finish();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .show();
                break;
            case R.id.ll_setting_logout:
                new MaterialAlertDialogBuilder(getActivity(), R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Confirm",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        FirebaseAuth.getInstance().signOut();
                                        Intent i = new Intent(getActivity(),SplashActivity.class);
                                        startActivity(i);
                                        getActivity().finish();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                })
                        .show();
                break;
        }
    }
}
