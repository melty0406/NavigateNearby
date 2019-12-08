package com.example.user.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.example.user.myapplication.Fragments.HomeFragment;
import com.example.user.myapplication.Fragments.ProfileFragment;
import com.example.user.myapplication.Fragments.SettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


public class MainActivity extends AppCompatActivity implements LocationListener{

    private SpaceNavigationView mNavigationView;
    private boolean centreHomeSelected = false;
    Fragment home_fragment = new HomeFragment();
    Fragment profile_fragment = new ProfileFragment();
    Fragment settings_fragment = new SettingsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationView = findViewById(R.id.space_nav);

        //init space nav
        mNavigationView.initWithSaveInstanceState(savedInstanceState);
        mNavigationView.addSpaceItem(new SpaceItem("PROFILE",R.drawable.ic_person_black_24dp));
        mNavigationView.addSpaceItem(new SpaceItem("SETTINGS",R.drawable.ic_settings_black_24dp));

        // Set Home as Default
        mNavigationView.setCentreButtonSelectable(true);
        mNavigationView.setCentreButtonSelected();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, home_fragment).commit();
        centreHomeSelected = true;

        // Set Onclick listener
        mNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                if(!centreHomeSelected) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, home_fragment).commit();
                    centreHomeSelected = true;
                    mNavigationView.setCentreButtonSelectable(true);
                }
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch(itemIndex){
                    case 0:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,profile_fragment).commit();
                        centreHomeSelected = false;
                        break;
                    case 1:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,settings_fragment).commit();
                        centreHomeSelected = false;
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Toast.makeText(MainActivity.this, "You are currently in this page", Toast.LENGTH_SHORT).show();
            }
        });

        // Check device permission
        checkDeviceLocationPermission();
        checkNetworkConnectionOn();
    }

    /** Check if device allow this application to access GPS **/
    public void checkDeviceLocationPermission(){
        Dexter.withActivity(MainActivity.this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if(response.isPermanentlyDenied()){
                            androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Permission Denied")
                                    .setMessage("Permission to access device location is permanently denied. Please go to the setting to allow the permission")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent i = new Intent();
                                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            i.setData(Uri.fromParts("package",getPackageName(),null));
                                        }
                                    })
                                    .show();
                        }
                        else{
                            Toast.makeText(MainActivity.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .check();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /** Enable WiFi or GPS Service **/
    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enable new provider!" + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Please enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    private void checkNetworkConnectionOn(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for(NetworkInfo ni: netInfo){
            if(ni.getTypeName().equalsIgnoreCase("WIFI")) {
                if (ni.isConnected()) {
                    haveConnectedWifi = true;
                }
            }
            if(ni.getTypeName().equalsIgnoreCase("MOBILE")) {
                if (ni.isConnected()) {
                    haveConnectedMobile = true;
                }
            }
        }

        if(!haveConnectedMobile || !haveConnectedWifi){
            Toast.makeText(this, "Please enable WiFi/Mobile Network!", Toast.LENGTH_SHORT).show();
        }
    }

}
