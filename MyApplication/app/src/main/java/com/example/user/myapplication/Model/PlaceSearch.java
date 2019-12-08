package com.example.user.myapplication.Model;

public class PlaceSearch {
    private String placeType;

    public PlaceSearch(){
        placeType = "";
    }

    public PlaceSearch(String placeType) {
        this.placeType = placeType;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }
}
