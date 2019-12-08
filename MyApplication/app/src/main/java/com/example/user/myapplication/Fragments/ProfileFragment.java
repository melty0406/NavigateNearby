package com.example.user.myapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.myapplication.Adapter.PlacesNavigatedAdapter;
import com.example.user.myapplication.Model.PlaceNavigated;
import com.example.user.myapplication.Model.PlaceSearch;
import com.example.user.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileFragment extends Fragment {
    private TextView tvUserEmail, tvNavigationtotal, tvRecentSearch;
    private RecyclerView rvPlaces;
    private PlacesNavigatedAdapter adapter;
    private ArrayList<PlaceNavigated> mPlaces;

    private DatabaseReference mDatabaseReference1, mDatabaseReference2;
    private FirebaseUser firebaseUser;
    private String user_id = "";
    private int navigationTotal = 0;
    private int searchTotal = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvUserEmail = v.findViewById(R.id.tv_profile_email);
        tvNavigationtotal = v.findViewById(R.id.tv_navigation_made);
        tvRecentSearch = v.findViewById(R.id.tv_search_made);
        rvPlaces = v.findViewById(R.id.rv_placeNavigated);
        rvPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        mPlaces = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            user_id = firebaseUser.getUid();
        }

        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference().child("place_navigated");
        mDatabaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                navigationTotal = 0;
                DataSnapshot userSnapShot = dataSnapshot.child(user_id);

                if(userSnapShot != null) {
                    for (DataSnapshot dataSnapshot1 : userSnapShot.getChildren()) {
                        PlaceNavigated p = dataSnapshot1.getValue(PlaceNavigated.class);
                        navigationTotal +=1;
                        mPlaces.add(p);
                    }
                }

                String num = String.valueOf(navigationTotal);
                tvNavigationtotal.setText("" + num);
                adapter = new PlacesNavigatedAdapter(mPlaces);
                rvPlaces.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Something is wrong!", Toast.LENGTH_SHORT).show();
            }
        });

        mDatabaseReference2 = FirebaseDatabase.getInstance().getReference().child("place_search");
        mDatabaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchTotal = 0;
                DataSnapshot userSnapShot = dataSnapshot.child(user_id);
                if(userSnapShot != null) {
                    for (DataSnapshot dataSnapshot1 : userSnapShot.getChildren()) {
                        PlaceSearch p = dataSnapshot1.getValue(PlaceSearch.class);
                        searchTotal += 1;
                    }
                }
                tvRecentSearch.setText(String.valueOf(searchTotal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        loadUserInformation();

        return v;
    }

    private void loadUserInformation(){

        if (firebaseUser != null) {
            if(firebaseUser.getEmail() != null){
                String email = firebaseUser.getEmail();
                tvUserEmail.setText(""+email);
            }
        }
    }
}
