package com.example.user.myapplication.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.myapplication.Adapter.PlacesNearbyAdapter;
import com.example.user.myapplication.Model.PlaceNavigated;
import com.example.user.myapplication.Model.PlaceNearby;
import com.example.user.myapplication.Model.PlaceSearch;
import com.example.user.myapplication.NavigationActivity;
import com.example.user.myapplication.JSONParser.PlaceJSONParser;
import com.example.user.myapplication.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragment extends Fragment implements PlacesNearbyAdapter.OnItemClickListener, LocationListener{

    public double mlatitude = 0.0;
    public double mlongitude = 0.0;
    private boolean locationFound = false;

    private LocationManager locationManager;
    private RecyclerView rvPlaces;
    private ArrayList<PlaceNearby> mPlaces;
    private PlacesNearbyAdapter adapter;
    private MaterialCardView cardBank, cardBus, cardCafe, cardHospital, cardShopping, cardRestaurant;


    public static final String API_KEY_1 = "AIzaSyBYLq6TZbZHOfSXNmDib9At0y9-5jQGyMA";
    public static final String PLACE_LAT = "com.example.user.myapplication.LAT";
    public static final String PLACE_LNG = "com.example.user.myapplication.LNG";
    public static final String PLACE_NAME = "com.example.user.myapplication.NAME";

    private DatabaseReference mDatabaseReference1, mDatabaseReference2;
    private FirebaseUser firebaseUser;
    private String user_id = "";
    private String searchType = "";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            user_id = firebaseUser.getUid();
        }

        mDatabaseReference1 = FirebaseDatabase.getInstance().getReference("place_navigated");
        mDatabaseReference2 = FirebaseDatabase.getInstance().getReference("place_search");

        rvPlaces = v.findViewById(R.id.rv_placesDetails);
        mPlaces = new ArrayList<>();
        adapter = new PlacesNearbyAdapter(mPlaces);
        rvPlaces.setAdapter(adapter);
        rvPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.setOnItemClickListener(this);

        cardBank = v.findViewById(R.id.card_bank);
        cardBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkArrayList(mPlaces);
                searchType = "bank";
                searchNearby(searchType);
            }
        });

        cardBus = v.findViewById(R.id.card_bus_station);
        cardBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkArrayList(mPlaces);
                searchType = "bus_station";
                searchNearby(searchType);
            }
        });

        cardCafe= v.findViewById(R.id.card_cafe);
        cardCafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkArrayList(mPlaces);
                searchType = "cafe";
                searchNearby(searchType);
            }
        });

        cardHospital = v.findViewById(R.id.card_hospital);
        cardHospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkArrayList(mPlaces);
                searchType = "hospital";
                searchNearby(searchType);
            }
        });

        cardShopping = v.findViewById(R.id.card_shopping_mall);
        cardShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkArrayList(mPlaces);
                searchType = "shopping_mall";
                searchNearby(searchType);
            }
        });

        cardRestaurant = v.findViewById(R.id.card_restaurant);
        cardRestaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkArrayList(mPlaces);
                searchType = "restaurant";
                searchNearby(searchType);
            }
        });

        return v;
    }

    private void checkArrayList(ArrayList<PlaceNearby> mList){
        if(mList.size()>0){
            mPlaces.clear();
            adapter = new PlacesNearbyAdapter(mPlaces);
            rvPlaces.setAdapter(adapter);
            rvPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter.setOnItemClickListener(this);
        }
    }

    /**
     * Click function on recycler view
     **/
    @Override
    public void onItemClick(int p) {
        // get Place position and details
        PlaceNearby place = mPlaces.get(p);
        String placeName = place.getPlaceName();
        String placeVicinity = place.getVicinity();
        Double temp_lat = Double.parseDouble(place.getLatitude());
        Double temp_lng = Double.parseDouble(place.getLongitude());

        // insert into database
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd, HH:mm:ss");
        String date = sdf.format(d);

        PlaceNavigated placeNav = new PlaceNavigated(placeName,placeVicinity, date);
        String id = mDatabaseReference1.push().getKey();
        mDatabaseReference1.child(user_id).child(id).setValue(placeNav);

        // start intent
        Intent i = new Intent(getActivity(),NavigationActivity.class);
        i.putExtra(PLACE_NAME, place.getPlaceName());
        i.putExtra(PLACE_LAT, temp_lat);
        i.putExtra(PLACE_LNG, temp_lng);
        startActivity(i);
    }

    /**
     * Search Nearby Function
     */

    public void searchNearby(String type){
        if (mlatitude == 0.0 && mlongitude == 0.0) {
            Toast.makeText(getActivity(), "No location", Toast.LENGTH_SHORT).show();
        } else {

            PlaceSearch placeSearch  = new PlaceSearch(type);
            String id = mDatabaseReference2.push().getKey();
            mDatabaseReference2.child(user_id).child(id).setValue(placeSearch);

            StringBuilder sb1 = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb1.append("location=" + mlatitude + "," + mlongitude);
            sb1.append("&radius=5000");
            sb1.append("&type=" + type);
            sb1.append("&sensor=true");
            sb1.append("&key=" + API_KEY_1);

            // Creating a new non-ui thread task to download json data
            HomeFragment.PlacesTask placesTask = new HomeFragment.PlacesTask();

            // Invokes the "doInBackground()" method of the class PlaceTask
            placesTask.execute(sb1.toString());

            new MaterialAlertDialogBuilder(getActivity(),
                    R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
                    .setTitle("System")
                    .setMessage("Searching nearby place...")
                    .setPositiveButton("OK",null)
                    .show();
        }
    }

    // A method to download json data from url
    @SuppressLint("LongLogTag")
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Exception while downloading url" + e.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    /**
     * A class, to download Google Places
     */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result) {
            HomeFragment.ParserTask parserTask = new HomeFragment.ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            try {
                JSONObject jsonObj = new JSONObject(result);
                String code = jsonObj.getString("status");

                if(code.equals("OVER_QUERY_LIMIT")){
                    Toast.makeText(getActivity(),
                            "API Status: [Over Query Limit]", Toast.LENGTH_SHORT).show();
                }
                else{
                    parserTask.execute(result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, ArrayList<PlaceNearby>> {
        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected ArrayList<PlaceNearby> doInBackground(String... jsonData) {
            ArrayList<PlaceNearby> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct **/
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(ArrayList<PlaceNearby> list) {

            for (int i = 0; i < list.size(); i++) {

                PlaceNearby mPlace = list.get(i);

                double place_lat = Double.parseDouble(mPlace.getLatitude());
                double place_lng = Double.parseDouble(mPlace.getLongitude());

                double x = Math.pow((place_lat-mlatitude),2);
                double y = Math.pow((place_lng-mlongitude),2);
                double distance = Math.sqrt(x+y);

                mPlace.setDistance(distance);

                mPlaces.add(mPlace);
                adapter.notifyDataSetChanged();
            }
        }
    }


    /**
     *  Request updates at startup.
     *  */
    @Override
    public void onResume() {
        super.onResume();
        getLocation();

    }

    /**
     *  Remove the location listener updates when Activity is paused.
     *  */
    @Override
    public void onPause(){
        super.onPause();
        locationManager.removeUpdates(this);
    }

    /**
     * Get current location *
     * */
    public void getLocation() {
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates
                    (LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }


    /**
     * Loading for current location
     * **/
    @Override
    public void onLocationChanged(Location location) {

        if(!locationFound){
            while(mlongitude == 0.0 && mlatitude == 0.0) {

                mlatitude = location.getLatitude();
                mlongitude = location.getLongitude();
            }

            locationFound = true;
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}