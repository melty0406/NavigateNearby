package com.example.user.myapplication.Model;

public class PlaceNavigated {

    //private String id;
    private String placeName;
    private String vicinity;
    private String date;

    public PlaceNavigated(){
        placeName = "";
        vicinity = "";
        date = "";
    }

    public PlaceNavigated(String placeName, String vicinity, String date) {
        this.placeName = placeName;
        this.vicinity = vicinity;
        this.date = date;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}