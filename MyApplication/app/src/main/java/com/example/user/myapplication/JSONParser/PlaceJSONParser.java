package com.example.user.myapplication.JSONParser;

import android.util.Log;

import com.example.user.myapplication.Model.PlaceNearby;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PlaceJSONParser {


    public ArrayList<PlaceNearby> parse (JSONObject jObject){
        // store json arrays
        JSONArray jPlaces = null;

        try {
            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jPlaces);
    }

    private ArrayList<PlaceNearby> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        ArrayList<PlaceNearby> placesList = new ArrayList<PlaceNearby>();
        PlaceNearby placeMap = null;

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placesList;
    }


    private PlaceNearby getPlace(JSONObject jPlace) {

        HashMap<String, String> place = new HashMap<String, String>();
        PlaceNearby mPlace = new PlaceNearby();

        try {

            if(!jPlace.isNull("id")){
                mPlace.setId(jPlace.getString("id"));
            }

            // Extracting Place name, if available
            if (!jPlace.isNull("name")) {
                mPlace.setPlaceName(jPlace.getString("name"));
            }

            // Extracting Place Vicinity, if available
            if (!jPlace.isNull("vicinity")) {
                mPlace.setVicinity(jPlace.getString("vicinity"));
            }

            if (!jPlace.isNull("rating")) {
                mPlace.setRating(jPlace.getString("rating"));
            }

            if (!jPlace.isNull("icon")) {
                mPlace.setIcon(jPlace.getString("icon"));
            }

            mPlace.setLatitude(jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat"));
            mPlace.setLongitude(jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng"));

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("EXCEPTION", e.toString());
        }
        return mPlace;
    }
}
