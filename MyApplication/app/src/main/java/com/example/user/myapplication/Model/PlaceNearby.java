package com.example.user.myapplication.Model;


import android.os.Parcel;
import android.os.Parcelable;

public class PlaceNearby implements Parcelable {

    private String id;
    private String placeName;
    private String vicinity;
    private String rating;
    private String latitude;
    private String longitude;
    private String icon;
    private double distance;

    public PlaceNearby(String id, String placeName, String vicinity, String rating, String latitude, String longitude, String icon, double distance) {
        this.id = id;
        this.placeName = placeName;
        this.vicinity = vicinity;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
        this.distance = distance;
    }

    public PlaceNearby(){
        id = "";
        placeName = "";
        vicinity = "";
        rating = "";
        latitude = "";
        longitude = "";
        icon = "";
        distance = 0.0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    /** Writing Place object data to Parcel */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(placeName);
        dest.writeString(vicinity);
        dest.writeString(rating);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(icon);
        dest.writeDouble(distance);
    }

    /** Initializing Place object from Parcel object */
    private PlaceNearby(Parcel in){
        this.id = in.readString();
        this.placeName = in.readString();
        this.vicinity = in.readString();
        this.rating = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.icon = in.readString();
        this.distance = in.readDouble();
    }

    /** Generates an instance of Place class from Parcel */
    public static final Parcelable.Creator<PlaceNearby> CREATOR = new Parcelable.Creator<PlaceNearby>(){
        @Override
        public PlaceNearby createFromParcel(Parcel source) {
            return new PlaceNearby(source);
        }

        @Override
        public PlaceNearby[] newArray(int size) {
            // TODO Auto-generated method stub
            return null;
        }
    };
}
